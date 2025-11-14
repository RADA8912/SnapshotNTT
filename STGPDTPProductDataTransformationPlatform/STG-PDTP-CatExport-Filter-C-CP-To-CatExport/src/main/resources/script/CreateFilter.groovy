/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */


import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import src.main.resources.script.ContentObjectBuilder
import src.main.resources.script.MappingLogger
import src.main.resources.script.MaterialBuilder

def Message processData(Message message) {
    List products = message.getBody().value

    List classTreeFlat = message.getProperty('ClassTreePayload').value
    def catalogID = message.getHeader('XTargetPublicationID', String)
    def catalogLanguage = message.getHeader('XTargetLanguage', String)
    Map mediaShapeMapping = new JsonSlurper().parseText(message.getProperty('XMediaShapeMapping'))
    Map mediaShapeTypes = new JsonSlurper().parseText(message.getProperty('XMediaShapeTypes'))
    def sourceLanguage = message.getHeader('XSourceCurrentLanguage', String)
    def sourceCommunicationStructure = message.getHeader('XSourceCommunicationStructure', String)

    // Construct Map for prices and release memory reference to message property
    Map uniquePrices = MaterialBuilder.constructPricesReference(message.getProperty('PricesPayload'), sourceLanguage, message.getHeader('XSourcePriceCurrency', String))
    message.setProperty('PricesPayload', '')

    MappingLogger logger = new MappingLogger(message)

    def outputStream = new ByteArrayOutputStream()
    def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
    MarkupBuilder builder = new MarkupBuilder(indentPrinter)

    FilterBuilder filterBuilder = new FilterBuilder(products, logger, catalogID, catalogLanguage, message, builder, uniquePrices, mediaShapeTypes, mediaShapeMapping, classTreeFlat)
    builder.filters {
        classTreeFlat.findAll { it.externalObjectTypeCode == 'obj_filter_classification' } each { classification ->
            logger.info("--- Begin processing filter class ${classification.code} ---")
            // Check if there are any communication structure areas and articles for the filter class
            List communicationClassReferences = classification.classificationClassReferences.findAll { it.target.classificationSystemType == sourceCommunicationStructure }
            List productsInClass = classification.classificationClassProductReferences.findAll { it.referenceTypeCode == 'ref_filter_classification' }
            if (communicationClassReferences.size() && productsInClass.size()) {
                def currentClassTree = filterBuilder.generateClassTree(classification.code, classTreeFlat)
                builder.filter(name: filterBuilder.formatClassificationClassCode(classification.code)) {

                    filterBuilder.buildCatalog(communicationClassReferences)

                    filterBuilder.buildCriteria(currentClassTree)

                    filterBuilder.buildAdvisor(currentClassTree)

                    filterBuilder.buildMaterials(currentClassTree, productsInClass)

                    filterBuilder.buildReference()

                    filterBuilder.reset()
                }
            } else if (communicationClassReferences.size() == 0) {
                logger.warn("Filter class ${classification.code} skipped as there are no communication structure classes")
            } else if (productsInClass.size() == 0) {
                logger.warn("Filter class ${classification.code} skipped as there are no articles")
            }
        }
    }

    message.setBody(outputStream)
    // Free up references to payloads
    message.setProperty('ClassTreePayload', '')
    message.setProperty('AvailabilityPayload', '')

    if (logger.getEntries().size()) {
        message.setProperty('LogEntries', logger.getEntries())
    }
    return message
}

class FilterBuilder {

    List referencedCOs
    List products
    MappingLogger logger
    String catalogID
    String catalogLanguage
    Message message
    Object builder
    ContentObjectBuilder coBuilder
    Map prices
    Map shapeTypes
    Map shapeMapping
    List allClasses
    String publicationScope
    String classificationSystemType

    FilterBuilder(List products, MappingLogger logger, String catalogID, String catalogLanguage, Message message, Object builder, Map prices, Map shapeTypes, Map shapeMapping, List classTreeFlat) {
        this.logger = logger
        this.products = products
        this.catalogID = catalogID
        this.catalogLanguage = catalogLanguage
        this.message = message
        this.builder = builder
        this.coBuilder = new ContentObjectBuilder(builder, classTreeFlat*.classificationClassContentObjectReferences, message, logger)
        this.prices = prices
        this.referencedCOs = []
        this.shapeTypes = shapeTypes
        this.shapeMapping = shapeMapping
        this.allClasses = classTreeFlat
        this.publicationScope = message.getHeader('XPublicationScope', String)?.toUpperCase()
        this.classificationSystemType = message.getHeader('XSourceCommunicationStructure', String)
    }

    void reset() {
        this.referencedCOs.clear()
        this.coBuilder.resetInlineReferences()
    }

    Map generateClassTree(String currentClassCode, List allClasses) {
        Map currentClass = allClasses.find { it.code == currentClassCode }
        List childClasses = allClasses.findAll { it.parentCode == currentClassCode }
        if (childClasses.size()) {
            List subClasses = []
            childClasses.sort { ref -> ref.sortOrder == null ? 0 : ref.sortOrder }.each { childClass ->
                subClasses.add(generateClassTree(childClass.code, allClasses))
            }
            currentClass.put('subClasses', subClasses)
        }
        return currentClass
    }

    String formatClassificationClassCode(String code) {
        return code?.replace('fclass-','')
    }

    void buildCatalog(List classReferences) {
        builder.catalog(id: this.catalogID, lang: this.catalogLanguage) {
            classReferences.each { ref ->
                this.builder.area(aId: ref.targetCode?.split('_').last().replaceFirst('^0+(?!$)', ''), label: ref.target?.name)
            }
        }
    }

    void buildCriteria(Map classification) {
        def criteriaClass = classification.subClasses.find { it.externalObjectTypeCode == 'obj_filter_criteria' }
        if (criteriaClass) {
            this.builder.criteria {
                criteriaClass.subClasses.findAll { it.externalObjectTypeCode == 'obj_filter_group' }.each { groupClass ->
                    this.builder.group(this.buildGroupAttributes(groupClass)) {
                        groupClass.subClasses.findAll { it.externalObjectTypeCode == 'obj_filter_item' }.each { itemClass ->
                            this.builder.item(this.buildItemAttributes(itemClass))
                        }
                    }
                }
            }
        }
    }

    private Map buildGroupAttributes(Map groupClass) {
        Map attributes = [gID: groupClass.code.split('-').last(), label: groupClass.name]
        def filter = getAttributeValueCodeFromMetaData(groupClass, 'atr_filter_ignore')
        def advisor = getAttributeValueCodeFromMetaData(groupClass, 'atr_filter_use_advisor', '1')
        def type = getAttributeValueCodeFromMetaData(groupClass, 'atr_filter_group_type', 'multi')
        def hideInDetail = getHideInDetail(groupClass)
        def refID = collectRefIDs(groupClass)
        if ((this.publicationScope == 'FILTER' && filter == 'Web18' && this.classificationSystemType == 'COMMUNICATION_DOMESTIC_V_02') || (this.publicationScope != 'FILTER' && filter == 'Web13')) {
            attributes << [filter: '0']
        }
        if (advisor) {
            attributes << [advisor: advisor]
        }
        if (type) {
            attributes << [type: type]
        }
        if (hideInDetail) {
            attributes << [hideInDetail: hideInDetail]
        }
        if (refID) {
            attributes << [refID: refID]
        }
        return attributes
    }

    private Map buildItemAttributes(Map itemClass) {
        Map attributes = [iID: itemClass.code.split('-').last(), label: itemClass.name]
        def filter = getAttributeValueCodeFromMetaData(itemClass, 'atr_filter_in_filter', '1')
        def refID = collectRefIDs(itemClass)
        if (filter) {
            attributes << [filter: filter]
        }
        if (refID) {
            attributes << [refID: refID]
        }
        return attributes
    }

    String getValueFromMetaData(Map classification, String attributeCode, String conditionToSkip = null) {
        def value = classification.classificationClassMetaData.find { it.attributeCode == attributeCode }?.value
        if (conditionToSkip) {
            if (value == conditionToSkip) {
                value = null
            }
        }
        return value
    }

    String getAttributeValueCodeFromMetaData(Map classification, String attributeCode, String conditionToSkip = null) {
        def attribute = classification.classificationClassMetaData.find { it.attributeCode == attributeCode }?.attributeValueCode
        if (conditionToSkip) {
            if (attribute == conditionToSkip) {
                attribute = null
            }
        }
        return attribute
    }

    String getHideInDetail(Map classification) {
        def hideInDetail = getAttributeValueCodeFromMetaData(classification, 'atr_filter_use_detail_tab')
        if (hideInDetail == '0') {
            hideInDetail = '1'
        } else {
            hideInDetail = null
        }
        return hideInDetail
    }

    String collectRefIDs(Map classification) {
        // Store content object references that are found from the tree
        List refList = classification.classificationClassContentObjectReferences.findAll { it.referenceTypeCode == 'ref_filter_attribute' && it.target }.collect { it.target.formattedCode }
        this.referencedCOs.addAll(refList)
        return refList.join(',')
    }

    String collectRefIDs(List references, String answerClassCode) {
        // Store content object references from inline text, as long as the referenced object can be found from cache
        List refList = []
        references.toSet().each { String contentObjectCode ->
            if (this.coBuilder.contentObjectCache.containsKey(contentObjectCode)) {
                refList.add(contentObjectCode)
            } else {
                logger.warn("Inline text referenced object ${contentObjectCode} not found for filter class ${answerClassCode}")
            }
        }
        this.referencedCOs.addAll(refList)
        return refList.join(',')
    }

    void buildAdvisor(Map classification) {
        def advisorClass = classification.subClasses.find { it.externalObjectTypeCode == 'obj_advisor' }
        if (advisorClass) {
            this.builder.advisor {
                advisorClass.subClasses.findAll { it.externalObjectTypeCode == 'obj_advisor_question' }.each { questionClass ->
                    this.builder.question(this.buildQuestionAttributes(questionClass)) {
                        def headlineValue = questionClass.classificationClassMetaData.find { it.attributeCode == 'atr_filter_question' }?.value
                        this.builder.headline {
                            mkp.yieldUnescaped("<![CDATA[${headlineValue ? headlineValue : ''}]]>")
                        }
                        questionClass.subClasses.findAll { it.externalObjectTypeCode == 'obj_advisor_answer' }.each { answer ->
                            this.buildAnswer(answer)
                        }
                    }
                }
            }
        }
    }

    private Map buildQuestionAttributes(Map questionClass) {
        Map attributes = [id: questionClass.code.split('-').last()]
        def label = getValueFromMetaData(questionClass, 'atr_filter_question_label')
        def type = getAttributeValueCodeFromMetaData(questionClass, 'atr_filter_question_type')
        def choose = getAttributeValueCodeFromMetaData(questionClass, 'atr_filter_advisor_choose_type', 'multi')
        def design = getAttributeValueCodeFromMetaData(questionClass, 'atr_filter_question_design_type')
        if (label) attributes << [resultLabel: label]
        if (type) attributes << [type: type]
        if (choose) attributes << [choose: choose]
        if (design) attributes << [design: design]
        return attributes
    }

    void buildAnswer(Map answer) {
        this.builder.answer(id: answer.code.split('-').last()) {
            answer.classificationClassMediaReferences.findAll { it.referenceTypeCode == 'ref_advisor_image' }.each { image ->
                this.builder.m_IMG(type: '25', typeName: 'Milieu') {
                    def url = image.target?.url
                    this.builder.img(id: image.target?.code, src: url) {
                        this.coBuilder.buildRatios(builder,image)
                        this.coBuilder.buildShapes(builder,image,url,'Filter-Answer')
                    }
                }
            }
            answer.classificationClassMetaData.findAll { it.attributeCode == 'atr_filter_answer' }.each { filterAnswer ->
                def attributes = [:]
                Map inlineTexts = this.coBuilder.resolveInlineText(filterAnswer.value, false, answer.code)
                if (inlineTexts.references) {
                    String refIDs = collectRefIDs(inlineTexts.references, answer.code)
                    if (refIDs) {
                        attributes << [refID: refIDs]
                    }
                }
                this.builder.text(attributes) {
                    mkp.yieldUnescaped("<![CDATA[${inlineTexts.text}]]>")
                }
            }
            answer.classificationClassReferences.findAll { it.referenceTypeCode == 'ref_answer_filter_item' }.each { criteria ->
                this.builder.criteria(cID: criteria.targetCode.split('-').last())
            }
            def subAnswers = answer.subClasses.findAll { it.externalObjectTypeCode == 'obj_advisor_answer' }
            if (subAnswers) {
                this.builder.subAnswers {
                    subAnswers.each { subAnswer ->
                        buildAnswer(subAnswer)
                    }
                }
            }
        }
    }

    void buildMaterials(Map classification, List productsInClass) {
        MaterialBuilder materialBuilder = new MaterialBuilder(builder, this.message, this.logger, this.prices)
        def criteriaClass = classification.subClasses.find { it.externalObjectTypeCode == 'obj_filter_criteria' }
        if (productsInClass) {
            this.builder.materials {
                productsInClass.each { product ->
                    Map productDetails = products.find { it.code == product.targetCode }
                    def classificationSource = this.allClasses.find { all ->
                        all.classificationClassProductReferences.find { ref ->
                            ref.referenceTypeCode == 'ref_legend_classification' && ref.targetCode == product.targetCode
                        }
                    }
                    if(classificationSource) productDetails?.classificationClassProductReferences = [['referenceTypeCode': 'ref_legend_classification','source':classificationSource]]
                    Map productDetailsWithTarget = [target: productDetails]
                    def filter = this.buildFilterForMaterialTeaser(criteriaClass, productDetailsWithTarget)
                    materialBuilder.buildMaterialTeaser(productDetailsWithTarget, productDetails, filter)
                }
            }
        }
    }

    private String buildFilterForMaterialTeaser(Map criteriaClass, Map product) {
        def filter = ','
        List<Map> attributes = []
        criteriaClass?.subClasses?.findAll { it.externalObjectTypeCode == 'obj_filter_group' }?.each { group ->
            if (group.classificationClassAttributeAssignments) attributes = attributes + group.classificationClassAttributeAssignments
        }
        product.target?.productFeatures?.each { productFeature ->
            def found = attributes.find { it.attributeCode == productFeature.attributeCode }
            if (found) {
                filter = filter + productFeature.attributeValueCode.split('-').last() + ','
            }
        }
        if (filter == ',') {
            return ',,'
        }
        return filter;
    }

    void buildReference() {
        List filterClasses = this.allClasses.findAll { it.externalObjectTypeCode in ['obj_filter_classification', 'obj_filter_item'] }
        this.coBuilder.buildReference([:], filterClasses, this.referencedCOs, 'Filter')
    }
}