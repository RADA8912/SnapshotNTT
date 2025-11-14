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
    Map root = message.getBody()

    Map classTreeFlat = message.getProperty('ClassTreePayload')
    def communicationRootCode = message.getHeader('XClassCommRoot', String)
    def catalogID = message.getHeader('XTargetPublicationID', String)
    def catalogLanguage = message.getHeader('XTargetLanguage', String)
    def catalogCountry = message.getHeader('XTargetCountry', String)
    def sourceLanguage = message.getHeader('XSourceCurrentLanguage', String)

    // Construct Map for prices and release memory reference to message property
    Map uniquePrices = MaterialBuilder.constructPricesReference(message.getProperty('PricesPayload'), sourceLanguage, message.getHeader('XSourcePriceCurrency', String))
    message.setProperty('PricesPayload', '')

    Map shapeMapping = new JsonSlurper().parseText(message.getProperty('XMediaShapeMapping'))

    MappingLogger logger = new MappingLogger(message)

    def articles = root.value.findAll { it.externalObjectTypeCode == 'obj_article' }
    def products = root.value.findAll { it.externalObjectTypeCode == 'obj_product' }
    // Build a hierarchy tree for classification classes starting from communication root
    Map classTreeDeep = generateClassTree(communicationRootCode, classTreeFlat.value)

    def outputStream = new ByteArrayOutputStream()
    def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
    MarkupBuilder builder = new MarkupBuilder(indentPrinter)

    MaterialBuilder matBuilder = new MaterialBuilder(builder, message, logger, uniquePrices)
    ContentObjectBuilder coBuilder = new ContentObjectBuilder(builder, articles*.productContentObjectReferences + classTreeFlat.value*.classificationClassContentObjectReferences, message, logger)
    TaxonomyBuilder taxBuilder = new TaxonomyBuilder(builder, classTreeFlat.value, logger)
    MediaBuilder mediaBuilder = new MediaBuilder(builder, message, shapeMapping, logger, coBuilder)
    builder.Products {
        // Process only the articles
        articles.each { product ->
            logger.info("--- Begin processing product ${product.code} ---")
            def productName = getAttribute(product, 'productFeatures', 'atr_Product_Name', 1)
            if (!productName || !productName?.formattedValue) {
                logger.log("[WARNING] No value for attribute atr_Product_Name found for product code - ${product.code}")
            }
            builder.productDetail(mainMatNo: product.formattedCode, label: productName?.formattedValue) {
                catalog(id: catalogID, lang: catalogLanguage, country: catalogCountry) {
                    buildArea(builder, classTreeDeep, product.code)
                }
                // Material
                builder.material(constructMaterialAttributes(product)) {
                    //Price
                    def price = matBuilder.buildPrice(builder, product, logger)
                    //Disposibility
                    buildDisposibility(builder, product)
                    //Shop
                    matBuilder.buildShop(builder, product, price, logger)
                    // Media
                    def mediaRefExists = product.productMediaReferences.size()
                    if (mediaRefExists) {
                        mediaBuilder.setMediaReferences(product.productMediaReferences)
                        builder.media {
                            mediaBuilder.buildCAD()
                            mediaBuilder.buildTechnicalDocument()
                            mediaBuilder.buildAR()
                            mediaBuilder.buildWithoutGrouping('m_ZEN')
                            mediaBuilder.buildImage()
                            mediaBuilder.buildVideo()
                            mediaBuilder.buildWithoutGrouping('m_DS')
                            mediaBuilder.buildWithoutGrouping('m_ZGP')
                        }
                    }
                    Map taxEntries = taxBuilder.processClassTree(product)
                    logger.debug("Taxonomy references = ${taxEntries.references}")

                    // Benefits
                    List filterClasses = classTreeFlat.value.findAll { it.externalObjectTypeCode in ['obj_filter_classification', 'obj_filter_item'] }
                    coBuilder.buildBenefits(product, 'ProductDetail-Benefit', filterClasses)
                    // Short texts
                    coBuilder.buildShortTexts(product.productContentObjectReferences, product)
                    // Reference
                    coBuilder.buildReference(product, filterClasses, taxEntries.references, 'ProductDetail')

                    //Topics
                    builder.topics {
                        mediaBuilder.buildAward()
                        matBuilder.buildPromotion(builder, product)
                    }

                    // Included accessories
                    buildMetaDataAttributes(builder, 'includedAssessories', 'includedAcessories', classTreeFlat.value, product, taxBuilder)

                    //Eco
                    buildEco(builder, classTreeFlat.value, product, taxBuilder)

                    // Taxonomy
                    if (taxEntries.legendClassCode) {
                        taxBuilder.buildEntries(taxEntries.legendClassCode)
                    }

                    // Matching Materials
                    if (matBuilder.matchingMaterialExists(product)) {
                        builder.matchingMaterials {
                            matBuilder.buildByType(product, 'additionalAccessories', false)
                            matBuilder.buildByType(product, 'includedAccessories', false)
                            matBuilder.buildByType(product, 'sellMandatoryAccessoriesCare', false)
                            matBuilder.buildByType(product, 'sellRecommendedAccessoriesCare', false)
                            matBuilder.buildByType(product, 'sellRecommendedMSC', false)
                            matBuilder.buildByType(product, 'matchingProducts', false)
                            matBuilder.buildByType(product, 'sellAdditionalService', false)
                        }
                    }
                    // Product notice
                    buildProductNotice(builder, product)
                }
                // Chooser
                if (product.parentCode) {
                    buildChooser(builder, product, products, articles, shapeMapping, logger)
                }
                buildFilter(builder, classTreeFlat.value, product.code)
            }
            coBuilder.resetInlineReferences()
        }
    }

    message.setBody(outputStream)
    // Free up references to payloads
    message.setProperty('ClassTreePayload', '')
    message.setProperty('AvailabilityPayload', '')
    message.setProperty('PromotionsPayload', '')

    if (logger.getEntries().size()) {
        message.setProperty('LogEntries', logger.getEntries())
    }
    return message
}

Map generateClassTree(String currentClassCode, List allClasses) {
    Map currentClass = allClasses.find { it.code == currentClassCode }
    List childClasses = allClasses.findAll { it.parentCode == currentClassCode }
    if (childClasses.size()) {
        List subClasses = []
        childClasses.each { childClass ->
            subClasses.add(generateClassTree(childClass.code, allClasses))
        }
        currentClass.put('subClasses', subClasses)
    }
    return currentClass
}

void buildArea(builder, Map currentClass, String productCode) {
    currentClass.subClasses.each { Map childClass ->
        if (productInClassTree(childClass, productCode)) {
            // Strip prefix and leading zeros from id
            String areaId = childClass.code.replaceAll(/(\w+_)*0*(\d+)/, '$2')
            // TODO - Workaround implemented for MIELEC2P-1576, to be undone in MIELEC2P-1516
            if (childClass.externalObjectTypeCode.endsWith('_langNeutral')) {
                buildArea(builder, childClass, productCode)
            } else {
                builder.area(id: areaId, label: childClass.name) {
                    buildArea(builder, childClass, productCode)
                }
            }
        }
    }
}

boolean productInClassTree(Map currentClass, String productCode) {
    def productCodeFound = currentClass.classificationClassProductReferences.any {
        it.targetCode == productCode
    }
    if (productCodeFound) {
        return true
    } else {
        for (Map childClass : currentClass.subClasses) {
            if (productInClassTree(childClass, productCode)) {
                return true
            }
        }
    }
    return false
}

void buildFilter(builder, List classes, String productCode) {
    def filterClasses = classes.findAll { cl -> cl.classificationClassProductReferences.find { ref -> ref.targetCode == productCode && ref.referenceTypeCode == 'ref_filter_classification' } }
    filterClasses.each {
        def name = getFilterName(it.code)
        builder.filter(name: name) {
            mkp.yieldUnescaped("<!--<rde-dm:import><keywords>filter:${name}</keywords></rde-dm:import>-->")
        }
    }
}

private String getFilterName(String code) {
    return code?.replace('fclass-', '')
}

Map getAttribute(Map entity, String attributeNodeName, String attributeCode, int multiValuePosition) {
    def attribute = [:]
    attribute = entity."${attributeNodeName}".find {
        it.attributeCode == attributeCode && it.multiValuePosition == multiValuePosition
    }
    return attribute
}

Map constructMaterialAttributes(Map product) {
    Map attributes = [matNo: product.formattedCode]
    addAttributeValue(attributes, 'ean', getAttribute(product, 'productFeatures', 'atr_EAN11', 1))
    addAttributeValue(attributes, 'artNo', getAttribute(product, 'productFeatures', 'atr_BISMT', 1))
    addAttributeValue(attributes, 'division', getAttribute(product, 'productFeatures', 'atr_SPART', 1))
    addAttributeValue(attributes, 'product_hierarchy', getAttribute(product, 'productFeatures', 'atr_PRDHA', 1))
    addAttributeValue(attributes, 'productType', getAttribute(product, 'productFeatures', 'atr_ZZGPRODTYP', 1))
    return attributes
}

void addAttributeValue(Map attributes, String attributeName, Map attribute) {
    if (attribute) {
        attributes << [(attributeName): attribute.formattedValue]
    }
}

void buildChooser(Object builder, Map product, List products, List articles, Map shapeMapping, MappingLogger logger) {
    def parentCode = product.parentCode
    def parentNode = products.find { it.code == parentCode }
    if (parentNode) {
        def varietyAttr = getAttribute(parentNode, 'productFeatures', 'atr_variety_defining_attribute', 1)
        if (varietyAttr) {
            def nameAttribute = getAttribute(product, 'productFeatures', varietyAttr.attributeValueCode, 1)
            builder.chooser(type: varietyAttr.attributeValueCode.replace('atr_', ''), label: nameAttribute?.attribute?.name) {
                def siblings = articles.findAll { it.parentCode == parentCode }
                Set uniqueCodes = []
                siblings.each { article ->
                    if (!uniqueCodes.contains(article.code)) { // process only unique ones
                        uniqueCodes.add(article.code)
                        def attr = getAttribute(article, 'productFeatures', varietyAttr.attributeValueCode, 1)
                        if (!attr) {
                            logger.log("[WARNING] Variety defining attribute ${varietyAttr.attributeValueCode} not found for product code - ${article.code}")
                        }
                        // Get the color chooser from media reference
                        def colorChooser = article.productMediaReferences.find { it.referenceTypeCode == 'ref_pim_color_chooser' }
                        def colorImg = (colorChooser) ? "${colorChooser.target?.url}${shapeMapping.get('Z19')}" : ''
                        chooserMaterial(matNo: article.formattedCode, label: attr?.formattedValue, img: colorImg)
                    }
                }
            }
        }
    }
}

void buildDisposibility(Object builder, Map product) {
    def deliveryAttribute = getAttribute(product, 'productFeatures', 'atr_CA_DELIVERY_TIME', 1)
    if (deliveryAttribute?.formattedValue) {
        builder.disposibility1(deliveryAttribute.formattedValue)
    }
}

void buildEco(Object builder, List classTreeFlat, Map product, TaxonomyBuilder taxBuilder) {
    Map attributes = [:]
    def icon = product.productMediaReferences.find { it.referenceTypeCode == 'ref_pim_onlinelabel' }?.target?.url
    if (icon) {
        attributes << ['icon': icon]
    } else {
        return
    }
    builder.eco(attributes) {
        def children = getLegendChildren(classTreeFlat, product.code, 'eco_information')
        if (children) {
            children.each { child ->
                builder.row(col1: child.name) {
                    Map entryDefAttributes = [:]
                    entryDefAttributes << ['name': child.code]
                    def refIds = taxBuilder.getRefidsForClass(child)
                    if(refIds){
                        entryDefAttributes << ['refID': refIds]
                    }
                    builder.entryDef(entryDefAttributes)
                }
            }
        }
    }
}

void buildMetaDataAttributes(Object builder, String attributeValueCode, String nodeName, List classTreeFlat, Map product, TaxonomyBuilder taxBuilder) {
    Map attributes = [:]
    def children = getLegendChildren(classTreeFlat, product.code, attributeValueCode)
    if (children) {
        builder."${nodeName}"(attributes) {
            children.each { child ->
                builder.row(col1: child.name) {
                    Map entryDefAttributes = [:]
                    entryDefAttributes << ['name': child.code]
                    def refIds = taxBuilder.getRefidsForClass(child)
                    if(refIds){
                        entryDefAttributes << ['refID': refIds]
                    }
                    builder.entryDef(name: child.code)
                }
            }
        }
    }
}

void buildProductNotice(Object builder, Map product) {
    def productNoticeNode = getAttribute(product, 'productFeatures', 'atr_product_notice', 1)
    if (productNoticeNode) {
        builder.productNotice { mkp.yieldUnescaped("<![CDATA[${productNoticeNode.formattedValue}]]>") }
    }
}

private List getLegendChildren(List classTreeFlat, String code, String attributeValueCode) {
    List children = []
    def legendClass = classTreeFlat.find { classificationClass ->
        classificationClass.externalObjectTypeCode == 'obj_legend_classification' &&
                classificationClass.classificationClassProductReferences.find { productRef ->
                    productRef.targetCode == code && productRef.referenceTypeCode == 'ref_legend_classification'
                }
    }
    if(legendClass) {
        def groupClass = classTreeFlat.find { it.parentCode == legendClass.code && it.externalObjectTypeCode == 'obj_legend_group' && it.classificationClassMetaData.find { it.attributeCode == 'atr_legend_type' && it.attributeValueCode == attributeValueCode } }
        if(groupClass){
            children = classTreeFlat.findAll { it.parentCode == groupClass.code }
        }
    }
    return children
}


class MediaBuilder {
    final Object builder
    List mediaReferences
    final Map<String, List> mediaTypeMapping
    final Map mediaDocTypeTranslation
    final Map shapeTypes
    final Map shapeMapping
    final MappingLogger logger
    final ContentObjectBuilder contentObjectBuilder

    MediaBuilder(Object builder, Message message, Map shapeMapping, MappingLogger logger, ContentObjectBuilder contentObjectBuilder) {
        this.builder = builder
        Map mediaMapping = new JsonSlurper().parseText(message.getProperty('XMediaDocumentTypeMapping'))
        this.mediaTypeMapping = mediaMapping.collectEntries {
            // Include non-breaking space (\p{Z}) or whitespace (\s) after comma
            [(it.key): it.value.split(/,[\p{Z}\s]*/).toList()]
        }

        this.mediaDocTypeTranslation = new JsonSlurper().parseText(message.getProperty('XMediaDocumentTypeTranslation'))
        this.shapeTypes = new JsonSlurper().parseText(message.getProperty('XMediaShapeTypes'))
        this.shapeMapping = shapeMapping
        this.logger = logger
        this.contentObjectBuilder = contentObjectBuilder
    }

    void buildCAD() {
        // Get all the media references for CAD
        // And group all media references based on the unique types
        Map<String, List> referencesByType = [:]
        getMediaRefsByElement('m_CAD').each { mediaRef ->
            // TODO - des_DOKAR
//			addReferenceByType(referencesByType, mediaRef, getAttribute(mediaRef.media.mediaVersion[0], 'mediaAttributeValue', 'des_DOKAR', 1)?.value)
            addReferenceByType(referencesByType, mediaRef, 'Z3D')
        }
        // Build the output grouping the media by the unique types
        referencesByType.each { elementGroupType, mediaRefList ->
            if (this.checkIfBuildMediaDoc(mediaRefList)) {
                builder.m_CAD(type: elementGroupType) {
                    mediaRefList.each { mediaRef ->
                        buildMediaDoc(mediaRef)
                    }
                }
            }
        }
    }

    void buildAR() {
        Map<String, List> referencesByType = [:]
        getMediaRefsByElement('m_AR').each { mediaRef ->
            addReferenceByType(referencesByType, mediaRef, 'Z3A')
        }
        // Build the output grouping the media by the unique types
        referencesByType.each { elementGroupType, mediaRefList ->
            if (this.checkIfBuildMediaDoc(mediaRefList)) {
                builder.m_AR(type: elementGroupType) {
                    mediaRefList.each { mediaRef ->
                        buildMediaDoc(mediaRef)
                    }
                }
            }
        }
    }

    void buildTechnicalDocument() {
        // Get all the media references for PTD
        // Build the output for each media
        getMediaRefsByElement('m_PTD').each { mediaRef ->
            if (!mediaRef.target) {
                logger.log("[WARNING] No target found for media code - ${mediaRef.targetCode}")
            }
            def elementAttributeType = mediaRef.target?.txTerm
            if (!elementAttributeType) {
                logger.log("[WARNING] No value for txTerm found for media code - ${mediaRef.targetCode}")
            }
            def elementAttributeTypeName = mediaDocTypeTranslation.get(mediaRef.referenceTypeCode) //getAttribute(mediaRef.media.mediaVersion[0], 'mediaAttributeValue', 'des_ASSET_TYPE', 1)?.value
            if (this.checkIfBuildMediaDoc([mediaRef])) {
                builder.m_PTD(type: elementAttributeType, typeName: elementAttributeTypeName) {
                    buildMediaDoc(mediaRef)
                }
            }
        }
    }

    void buildImage() {
        // Get all the media references for IMG
        def referenceTypesPerElement = mediaTypeMapping.get('m_IMG')
        // And group all media references based on the unique types
        Map<String, List> referencesByType = [:]
        mediaReferences.each { mediaRef ->
            addReferenceByType(referencesByType, mediaRef, mediaRef.referenceTypeCode)
        }
        String primageProductImageCode = ''
        // Build the output grouping the media by the unique types, sorted according to the value mapping order
        if (referencesByType.size()) {
            referenceTypesPerElement.each { String refType ->
                def typeComponents = refType.split('#')
                def referenceTypeCode = typeComponents[0]
                def groupType = typeComponents[1]
                def mediaRefList = referencesByType.get(referenceTypeCode)
                Set processedMediaCodes = []
                mediaRefList.each { mediaRef ->
                    if (!processedMediaCodes.contains(mediaRef.targetCode) && mediaRef.targetCode != primageProductImageCode) {
                        // Save the PrimaryProductImage, and skip exposing it for any subsequent ref_factual_image
                        if (referenceTypeCode == 'PrimaryProductImage') {
                            primageProductImageCode = mediaRef.targetCode
                        }
                        // If the media code has been processed before, track it for skipping later
                        processedMediaCodes.add(mediaRef.targetCode)
                        builder.m_IMG(type: groupType, typeName: mediaDocTypeTranslation.get(referenceTypeCode)) {
                            if (groupType == '14') {
                                if (mediaRef.target?.description && mediaRef.target?.alternativeText) {
                                    builder.text { mkp.yieldUnescaped("<![CDATA[<P>${mediaRef.target?.description}</P><P></P><P>${mediaRef.target?.alternativeText}</P>]]>") }
                                } else if (mediaRef.target?.description && !mediaRef.target?.alternativeText) {
                                    builder.text { mkp.yieldUnescaped("<![CDATA[<P>${mediaRef.target?.description}</P>]]>") }
                                } else if (!mediaRef.target?.description && mediaRef.target?.alternativeText) {
                                    builder.text { mkp.yieldUnescaped("<![CDATA[<P>${mediaRef.target?.alternativeText}</P>]]>") }
                                }
                            }
                            if (!mediaRef.target) {
                                logger.log("[WARNING] No target found for media code - ${mediaRef.targetCode}")
                            }
                            String url = mediaRef.target?.url
                            img(id: mediaRef.targetCode, src: url) {
                                this.contentObjectBuilder.buildRatios(builder, mediaRef)
                                this.contentObjectBuilder.buildShapes(builder, mediaRef, url, 'ProductDetail-Image-' + groupType)
                            }
                        }
                    }
                }
            }
        }
    }

    void buildVideo() {
        // Get all the media references for a specific element type
        def referencesPerElement = getMediaRefsByElement('m_VIDEO')
        if (referencesPerElement.size()) {
            // Build the output for each media
            builder.m_VIDEO {
                referencesPerElement.each { mediaRef ->
                    if (!mediaRef.target) {
                        logger.log("[WARNING] No target found for media code - ${mediaRef.targetCode}")
                    }
                    video(vid: mediaRef.targetCode) {
                        def thumbnailUrl = mediaRef.target?.thumbnailUrl
                        if (!thumbnailUrl) {
                            logger.log("[WARNING] No value for thumbnailUrl found for media code - ${mediaRef.targetCode}")
                        }
                        def url = mediaRef.target?.url
                        if (!url) {
                            logger.log("[WARNING] No url found for media code - ${mediaRef.targetCode}")
                        }
                        shape {
                            format(src: thumbnailUrl, type: 'URLTHUMB')
                            format(src: url, type: 'URLVIDEO')
                        }
                    }
                }
            }
        }
    }

    void buildAward() {
        def referencesPerElement = getMediaRefsByElement('io_AWARD')
        if (referencesPerElement.size()) {
            builder.io_AWARD {
                referencesPerElement.each { mediaRef ->
                    if (!mediaRef.target) {
                        logger.log("[WARNING] No target found for media code - ${mediaRef.targetCode}")
                    }
                    def url = mediaRef.target?.url
                    builder.img(id: mediaRef.targetCode) {
                        this.contentObjectBuilder.buildRatios(builder, mediaRef)
                        this.contentObjectBuilder.buildShapes(builder, mediaRef, url, 'ProductDetail-Award')
                    }
                }
            }
        }
    }

    void buildWithoutGrouping(String mediaElementType) {
        // Get all the media references for a specific element type
        def referencesPerElement = getMediaRefsByElement(mediaElementType)
        if (referencesPerElement.size()) {
            // Build the output for each media
            if (this.checkIfBuildMediaDoc(referencesPerElement)) {
                builder."${mediaElementType}" {
                    referencesPerElement.each { mediaRef ->
                        buildMediaDoc(mediaRef)
                    }
                }
            }
        }
    }

    private void addReferenceByType(Map<String, List> referencesByType, Map mediaRef, String type) {
        if (type) {
            if (referencesByType.containsKey(type)) {
                List refs = referencesByType.get(type)
                refs.add(mediaRef)
            } else {
                referencesByType.put(type, [mediaRef])
            }
        }
    }

    private void buildMediaDoc(Map mediaRef) {
        if (!mediaRef.target) {
            logger.log("[WARNING] No target found for media code - ${mediaRef.targetCode}")
        }
        def docType = mediaRef.target?.fileExtension
        def url = mediaRef.target?.url
        if (url) {
            builder.doc(
                    label: mediaDocTypeTranslation.get(mediaRef.referenceTypeCode),
                    type: docType,
                    src: url)
        }
    }

    private boolean checkIfBuildMediaDoc(List mediaRefList) {
        boolean someHasUrl = false
        mediaRefList.each { mediaRef ->
            if (!mediaRef.target?.url) {
                logger.log("[WARNING] No url found for media code - ${mediaRef.targetCode}")
            } else {
                someHasUrl = true
            }
        }
        return someHasUrl
    }

    private List getMediaRefsByElement(String elementType) {
        List result = []
        mediaReferences.findAll { it.referenceTypeCode in mediaTypeMapping.get(elementType) }.each {
            if (it.target) {
                result << it
            } else {
                logger.log("[WARNING] No target found for media reference - ${it.targetCode}")
            }
        }
        return result
    }
}

class TaxonomyBuilder {
    final Object builder
    final MappingLogger logger
    List entries
    Map classAttributes
    Map<String, List> contentObjectReferences
    final List legendClassTree
    final Map<String, List> productToLegendClasses

    TaxonomyBuilder(Object builder, List allClasses, MappingLogger logger) {
        this.builder = builder
        this.logger = logger
        this.legendClassTree = generateLegendTree(allClasses)
        this.productToLegendClasses = buildProductToLegendClassMap(allClasses)
    }

    private List generateLegendTree(List allClasses) {
        List legendClasses = allClasses.findAll { it.externalObjectTypeCode == 'obj_legend_classification' }
        legendClasses.each { Map legendClass ->
            List groupClasses = allClasses.findAll { it.parentCode == legendClass.code && it.externalObjectTypeCode == 'obj_legend_group' }
            groupClasses.each { Map groupClass ->
                List attributeClasses = allClasses.findAll { it.parentCode == groupClass.code && it.externalObjectTypeCode == 'obj_legend_attribut' }
                groupClass.put('attributeClasses', attributeClasses)
            }
            legendClass.put('groupClasses', groupClasses)
        }
        return legendClasses
    }

    private Map<String, List> buildProductToLegendClassMap(List allClasses) {
        Map output = [:]
        allClasses.each { Map classification ->
            classification.classificationClassProductReferences.findAll { it.referenceTypeCode == 'ref_legend_classification' }.each {
                if (output.containsKey(it.targetCode)) {
                    List classes = output.get(it.targetCode)
                    classes.add(classification)
                } else {
                    output.put(it.targetCode, [classification])
                }
            }
        }
        return output
    }

    Map processClassTree(Map product) {
        def matchingClasses = this.productToLegendClasses.get(product.code)
        def output = [:]
        // If found, get the first legend class and build the taxonomy based on that class
        if (matchingClasses?.size()) {
            def legendClassCode = matchingClasses[0].code
            if (matchingClasses.size() > 1) {
                logger.log("[WARNING] More than one ref_legend_classification for product code - ${product.code}")
            }
            classAttributes = getClassificationAttributes(legendClassCode)
            entries = getMatchingAttributes(product.productFeatures)
            contentObjectReferences = extractContentObjectReferences()
            output.put('legendClassCode', legendClassCode)
            output.put('references', contentObjectReferences.collect { it.value }.flatten())
        } else {
            output.put('legendClassCode', '')
            output.put('references', [])
        }
        return output
    }

    String getRefidsForClass(Object child) {
        List refIds = []
        this.entries.each { feature ->
            if(this.classAttributes.get(feature.attributeCode)?.code == child.code) {
                def cos = this.contentObjectReferences.get(feature.attributeCode)
                if(cos) {
                    refIds.addAll(cos)
                }
            }
        }
        return refIds.sort().join(',')
    }

    void buildEntries(String legendClassCode) {
        if (entries) {
            builder.taxonomy(tID: legendClassCode.replace('cls_', '')) {
                entries.each { prodAttr ->
                    if (checkIfCreateEntryNode(prodAttr)) {
                        entry(constructEntryAttributes(prodAttr))
                    }
                }
            }
        }
    }

    private Map getClassificationAttributes(String legendClassCode) {
        Map output = [:]
        def legendClass = this.legendClassTree.find { it.code == legendClassCode }
        if (legendClass) {
            legendClass.groupClasses.each { groupClass ->
                groupClass.attributeClasses.each { attrClass ->
                    attrClass.classificationClassAttributeAssignments.each { attrRef ->
                        if (!output.containsKey(attrRef.attributeCode)) {
                            output.put(attrRef.attributeCode, attrClass)
                        }
                    }
                }
            }
        }
        return output
    }

    private List getMatchingAttributes(List productFeatures) {
        List output = []
        productFeatures.each { feature ->
            if (classAttributes.containsKey(feature.attributeCode)) {
                output.add(feature)
            }
        }
        return output
    }

    private Map extractContentObjectReferences() {
        Map output = [:]
        entries.each { feature ->
            def classAttr = classAttributes.get(feature.attributeCode)
            List matchingRefs = []
            classAttr.classificationClassContentObjectReferences.each { coRef ->
                def position = coRef.classificationClassContentObjectReferenceMetaData.find { it.attributeCode == 'atr_legend_content_object_position' }?.attributeValueCode
                def displaylegendConditionAttribute = coRef.classificationClassContentObjectReferenceMetaData.find { it.attributeCode == 'atr_legend_display_condition_attribute' && it.multiValuePosition == 1 }?.value
                List displayLegendConditionValues = coRef.classificationClassContentObjectReferenceMetaData.findAll { it.attributeCode == 'atr_legend_display_condition_value' }.collect { it.value }
                if (((feature.attributeCode == displaylegendConditionAttribute || (displaylegendConditionAttribute == null && feature.attributeValueCode == '1')) && feature.attributeValueCode in displayLegendConditionValues) && position != '1') {
                    matchingRefs.add(coRef)
                }
            }
            if (matchingRefs) {
                output.put(feature.attributeCode, matchingRefs.findAll { it.target?.formattedCode }.collect { it.target.formattedCode })
            }
        }
        return output
    }

    private Map constructEntryAttributes(Map productAttribute) {
        def classAttr = classAttributes.get(productAttribute.attributeCode)
        Map entryAttributes = [name: classAttr.code]
        if (!classAttr.classificationClassMetaData.any { it.attributeCode == 'atr_display_yes_as_bullet' && it.attributeValueCode == '01' && it.multiValuePosition == 1 }) {
            entryAttributes << [value: productAttribute.formattedValue]
        }
        List coRefs = contentObjectReferences.get(productAttribute.attributeCode)
        if (coRefs) {
            entryAttributes << [refID: coRefs.sort().join(',')]
        }
        entryAttributes << [attrID: productAttribute.attributeCode]
        return entryAttributes
    }

    private boolean checkIfCreateEntryNode(Map productAttribute) {
        def classAttr = classAttributes.get(productAttribute.attributeCode)
        def display = classAttr.classificationClassMetaData.findAll { it.attributeCode == 'atr_display_yes_as_bullet' && it.multiValuePosition == 1 }
        if (!display) {
            return true
        }
        if (display.find { it.attributeValueCode == '01' }) {
            return productAttribute.attributeValueCode == '1'
        } else {
            if (productAttribute.formattedValue) {
                return true
            } else {
                return false
            }
        }
    }
}