/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

import java.text.NumberFormat
import java.text.SimpleDateFormat

/**
 * @author philipp.dreyer@cbs-consulting.de
 */
class ProductBuilder {
    final MappingLogger logger
    final List products
    final Map recommendedRetailPrices
    final Map directSellingPrices
    final String sourcePriceType
    final String currency
    final Map<String, Map> productToClassReferences
    final Map<String, List> programLinesReferences
    final Map<String, List> mediaTypeMapping
    final List legendClassTree
    final NumberFormat localeCurrencyFormatter
    final Map parentClassReferences
    final Set multiValuedAttributes

    Object builder
    Map article
    List proMediaRef
    List contentObject
    Map features

    ProductBuilder(List products, Message message, MappingLogger logger, Map classTree) {
        this.logger = logger
        this.products = products

        this.currency = message.getHeader('XSourcePriceCurrency', String)
        def sourceLanguage = message.getHeader('XSourceCurrentLanguage', String)
        this.localeCurrencyFormatter = getCurrencyFormatter(sourceLanguage)
        this.sourcePriceType = determinePriceType(message.getHeader('XSourcePriceType', String))
        this.recommendedRetailPrices = constructPricesReference(message.getProperty('PricesPayload'), this.currency, 'RECOMMENDED_RETAIL_PRICE')
        this.directSellingPrices = constructPricesReference(message.getProperty('PricesPayload'), this.currency, 'DIRECT_SELLING_PRICE')
        this.programLinesReferences = constructProgramLinesLookup(message.getProperty('ProgramLinesPayload'))
        Map mediaMapping = new JsonSlurper().parseText(message.getProperty('XMediaDocumentTypeMapping'))
        this.mediaTypeMapping = mediaMapping.collectEntries {
            // Include non-breaking space (\p{Z}) or whitespace (\s) after comma
            [(it.key): it.value.split(/,[\p{Z}\s]*/).toList()]
        }

        def targetGroup = message.getHeader('XTargetGroup', String)
        Map classificationProdRefMapping = new JsonSlurper().parseText(message.getProperty('XClassificationProductRefTypeMapping'))
        this.legendClassTree = getProductLegendTree(classTree.value)
        this.productToClassReferences = constructProductToClassRefs(classTree, classificationProdRefMapping, targetGroup)
        this.parentClassReferences = constructParentClassRefs(classTree)
        this.multiValuedAttributes = extractMultiValuedAttributes(message.getProperty('MultiValuedAttributesPayload').value)
    }
    
    String determinePriceType(String sourcePriceType) {
        switch (sourcePriceType) {
            case ['NONE', 'RECOMMENDED_RETAIL_PRICE', 'DIRECT_SELLING_PRICE']:
                return sourcePriceType
            default:
                return 'ALL'
        }
    }

    Set extractMultiValuedAttributes(List attributeList) {
        return attributeList.collect { it.code }.toSet()
    }

    void setArticleDetails(Object builder, Map article) {
        this.builder = builder
        this.article = article
        this.proMediaRef = article.productMediaReferences
        this.features = article.productFeatures.collectEntries {
            String key = "${it.attributeCode}_${it.multiValuePosition}"
            [(key): it]
        }
        Map piFeatures = article?.productFeaturesPiStructure?.collectEntries {
            String key = "${it.attributeCode}_${it.multiValuePosition}"
            [(key): it]
        }
        this.features.putAll(piFeatures)
        Map legendFeatures = article?.productFeaturesLegendsStructure?.collectEntries {
            String key = "${it.attributeCode}_${it.multiValuePosition}"
            [(key): it]
        }
        this.features.putAll(legendFeatures)
        this.contentObject = article.productContentObjectReferences
    }

    Map getFeatureByCodeAndPosition(String attributeCode, int position) {
        String key = "${attributeCode}_${position}"
        return this.features.get(key)
    }

    NumberFormat getCurrencyFormatter(String sourceLanguage) {
        // Get currency formatter according to locale
        def lang = sourceLanguage.split('_')
        Locale locale = new Locale(lang[0], lang[1])
        Currency currentCurrency = Currency.getInstance(locale)
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale)
        numberFormatter.setMinimumFractionDigits(currentCurrency.getDefaultFractionDigits())
        return numberFormatter
    }

    Map constructPricesReference(Map priceRoot, String currency, String priceTypeCode) {
        return priceRoot.value.findAll { it.currencyCode == currency && it.priceTypeCode == priceTypeCode }.collectEntries { [(it.productCode): it.value] }
    }

    Map<String, List> constructProgramLinesLookup(Map programLinesRoot) {
        Map<String, List> output = [:]
        if (programLinesRoot && programLinesRoot.value) {
            programLinesRoot.value.each {
                if (output.containsKey(it.productCode)) {
                    List lines = output.get(it.productCode)
                    lines.add(it.programLineCode)
                } else {
                    output.put(it.productCode, [it.programLineCode])
                }
            }
        }
        return output
    }

    private Map<String, Map> constructProductToClassRefs(Map classTree, Map classificationProdRefMapping, String targetGroup) {
        Map output = [:]
        Map pimLink = [:]
        Map commStructure = [:]
        Map legendsStructure = [:]
        classTree.value.each { classification ->
            classification.classificationClassProductReferences.each { prodRef ->
                if (prodRef.referenceTypeCode == classificationProdRefMapping.get('PI_STRUCTURE')) {
                    pimLink.put(prodRef.targetCode, classification)
                }
                String commStructureGroup = "COMMUNICATION_STRUCTURE_${targetGroup}"
                if (prodRef.referenceTypeCode == classificationProdRefMapping.get(commStructureGroup)) {
                    if (commStructure.containsKey(prodRef.targetCode)) {
                        this.logger.log("[WARNING] Article code - ${prodRef.targetCode} already linked to classification ${commStructure.get(prodRef.targetCode).code}. Skipping entry for current classification ${classification.code}")
                    } else {
                        commStructure.put(prodRef.targetCode, classification)
                    }
                }
                if (prodRef.referenceTypeCode == classificationProdRefMapping.get('LEGENDS_STRUCTURE')) {
                    if (legendsStructure.containsKey(prodRef.targetCode)) {
                        this.logger.log("[WARNING] More than 1 ${prodRef.referenceTypeCode} for article code - ${prodRef.targetCode}")
                    } else {
                        legendsStructure.put(prodRef.targetCode, this.legendClassTree.find { it.code == classification.code })
                    }
                }
            }
        }
        output.put('PI_STRUCTURE', pimLink)
        output.put('COMMUNICATION_STRUCTURE', commStructure)
        output.put('LEGENDS_STRUCTURE', legendsStructure)
        return output
    }

    private Map constructParentClassRefs(Map classTree) {
        Map parentClassReferences = [:]
        classTree.value.each { classification ->
            // Build map from child to parent classification for fast lookup
            if (classification.parentCode) {
                Map parentClass = classTree.value.find { it.code == classification.parentCode }
                parentClassReferences.put(classification.code, parentClass)
            } else {
                parentClassReferences.put(classification.code, null)
            }
        }
        return parentClassReferences
    }

    String getAreaID(Map classification) {
        // Look up parent classes until it hits the first language specific class
        if (classification) {
            Map parentClass = this.parentClassReferences.get(classification.code)
            if (parentClass) {
                logger.debug("Classification ${classification.code} belongs to parent class ${parentClass.code}")
                if (parentClass.externalObjectTypeCode.endsWith('_langSpecific')) {
                    return parentClass.code.replaceAll(/\w+_(\d+)/, '$1') // Remove prefix
                } else {
                    return getAreaID(parentClass)
                }
            } else {
                logger.debug("Classification ${classification.code} does not have any parent class")
                return ''
            }
        } else {
            return ''
        }
    }

    private List getProductLegendTree(List allClasses) {
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

    Map<String, List> processPiDataGroups() {
        Map output = [:]
        Map piClass = this.productToClassReferences.get('PI_STRUCTURE').get(this.article.code)
        List basics = []
        List energy = []
        List smart = []
        List features = []
        List mc = []
        piClass?.classificationClassAttributeAssignments.each { attribute ->
            def piCategory = attribute.classificationClassAttributeAssignmentMetaData.find { it.attributeCode == 'atr_PI_CATEGORY' }
            def piOrder = attribute.classificationClassAttributeAssignmentMetaData.find { it.attributeCode == 'atr_PIM_PI_ORDER' }
            def piKey = attribute.classificationClassAttributeAssignmentMetaData.find { it.attributeCode == 'atr_PI_PROPERTY_KEY' }
            String attributeValue = (!this.multiValuedAttributes.contains(attribute.attributeCode)) ? getFeatureByCodeAndPosition(attribute.attributeCode, 1)?.formattedValue : getMultiValuedContent(attribute.attributeCode)

            switch (piCategory?.value) {
                case 'Basics':
                    basics.add([pos: piOrder?.value as int, name: piKey?.value, text: attributeValue])
                    break
                case 'Energy Label Information':
                    energy.add([pos: piOrder?.value as int, name: piKey?.value, text: attributeValue])
                    break
                case 'Smart Information':
                    smart.add([pos: piOrder?.value as int, name: piKey?.value, text: attributeValue])
                    break
                case 'Features':
                    features.add([pos: piOrder?.value as int, name: piKey?.value, text: attributeValue])
                    break
                case 'MC':
                    mc.add([pos: piOrder?.value as int, name: piKey?.value, text: addURLParameters(piKey?.value, attributeValue)])
                    break
                default:
                    this.logger.log("[WARNING] No atr_PI_CATEGORY found for attribute ${attribute.attributeCode} of class ${piClass.code} for article ${this.article.code}")
            }
        }
        output.put('Basics', basics.sort { it.pos })
        output.put('Energy Label Information', energy.sort { it.pos })
        output.put('Smart Information', smart.sort { it.pos })
        output.put('Features', features.sort { it.pos })
        output.put('MC', mc.sort { it.pos })
        return output
    }

    String addURLParameters(String attributeName, String attributeValue) {
        switch (attributeName) {
            case 'PRODUCT_IMAGE':
                return (attributeValue) ? "${attributeValue}?impolicy=z-boxed&d=1400" : ''
            case { it.startsWith('ADD_IMAGE') }:
                return (attributeValue) ? "${attributeValue}?impolicy=z-boxed&d=1400" : ''
            default:
                return attributeValue
        }
    }

    String getMultiValuedContent(String attributeCode) {
        List filteredPiFeatures = this.article?.productFeaturesPiStructure?.findAll { it.attributeCode == attributeCode }
        String sortedValue = filteredPiFeatures*.formattedValue.sort().join(', ')
        return sortedValue
    }
    
    void buildProductDetails(Map unitMapping) {
        Map legendClass = this.productToClassReferences.get('LEGENDS_STRUCTURE').get(this.article.code)
        if (legendClass) {
            legendClass.groupClasses.each { groupClass ->
                groupClass.attributeClasses.each { attrClass ->
                    attrClass.classificationClassAttributeAssignments.each { attrRef ->
                        Map matchingFeature = getFeatureByCodeAndPosition(attrRef.attributeCode, 1)
                        if (matchingFeature) {
                            def convertedUnit = unitMapping.get(matchingFeature.attributeUnitCode)
                            builder.ATTRIBUTE(pos: attrClass.sortOrder,
                                    group: groupClass.name,
                                    name: matchingFeature.attributeCode.replace('atr_c_', ''),
                                    value: matchingFeature.formattedValue,
                                    unit: convertedUnit ?: matchingFeature.attributeUnitCode)
                                    { mkp.yieldUnescaped(setCDATA(attrClass.name)) }
                        }
                    }
                }
            }
        }
    }

    int buildSpecificData(int counter) {
        List programLinesForProduct = this.programLinesReferences.get(this.article.code)
        5.times {
            def lineCode = (programLinesForProduct?.size() > it) ? programLinesForProduct.get(it) : ''
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "PROGRAMMREIHE_${it + 1}") { mkp.yieldUnescaped(setCDATA(lineCode)) }
        }
        def content = (getFeatureByCodeAndPosition('atr_CA_MATERIAL_FOOT_NOTE', 1))?.formattedValue
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'PRODTYPE_FOOTNOTE') { mkp.yieldUnescaped(setCDATA(content)) }

        return counter
    }

//region helper methods
    Object getPrice(String priceTypeCode) {
        if (this.sourcePriceType == 'ALL' || this.sourcePriceType == priceTypeCode) {
            def atrPrice = getFeatureByCodeAndPosition('atr_ZZ2CAT_NPRICE', 1)
            def retrievedPrice
            if (priceTypeCode == 'RECOMMENDED_RETAIL_PRICE') {
                retrievedPrice = recommendedRetailPrices.get(article.code)
            } else if (priceTypeCode == 'DIRECT_SELLING_PRICE') {
                retrievedPrice = directSellingPrices.get(article.code)
            }
            if (retrievedPrice != null && (atrPrice?.formattedValue || retrievedPrice != 0)) {
                return retrievedPrice
            } else {
                logger.log("[WARNING] ${priceTypeCode} not found for product code - ${article.code}")
            }
        } else {
            return null
        }
    }

    String setCDATA(String s) {
        if (!s) {
            return "<![CDATA[]]>"
        } // CDATA gets suppressed when there is not at least a space within inner brackets
        else {
            return '<![CDATA[' + s + ']]>'
        }
    }

    String countFormatter(int counter) {
        return String.format("%05d", counter)
    }

    List getSubType(ArrayList entity, String attributeKey, String searchValue) {
        def object = entity.findAll { key ->
            key."${attributeKey}" == searchValue
        }
        return object
    }

    List getAllFeaturesWithCode(ArrayList rootNode, String referenceTypeCode) {
        List objects = new ArrayList()
        rootNode.findAll { childNode -> childNode.referenceTypeCode == referenceTypeCode }.each {
            objects.add(it)
        }

        return objects.sort {
            it.sortOrder
        }
    }
//endregion

//region implementation finished
/**
 * For each attribute of Product → ClassificationClassProductReference → ClassificationClass
 *
 * where ClassificationClass is a child (any level) of the {PI_STRUCTURE} classification.
 * and ... → ClassificationClassAttributeAssignment →
 * ClassificationClassAttributeAssignmentMetaData.attributeCode = "atr_PI_CATEGORY" and ClassificationClassAttributeAssignmentMetaData.value = "Basics"
 * Assumption: There should only be one assignment to a class of the {PI_STRUCTURE_17.0} classification. If there are more, take the first one.
 *
 */
    int buildPiAttributes(List attributes) {
        int counter = 0
        attributes.each { attribute ->
            builder.ATTRIBUTE(pos: countFormatter(attribute.pos), name: attribute.name) { mkp.yieldUnescaped(setCDATA(attribute.text)) }
            counter = attribute.pos
        }
        return counter + 1
    }

    int buildBenefitsSorted(int counter) {
        counter = buildBenefits_USP_Pictures(counter)
        counter = buildBenefits_Price(counter)
        counter = buildBenefits_Energylabel_Icon(counter)
        counter = buildBenefits_Award(counter)
        counter = buildBenefits_Manual(counter)
        counter = buildBenefits_EU_Datasheet(counter)
        counter = buildBenefits_EU_Label(counter)
        counter = buildBenefits_AttributeSets(counter)
        counter = buildBenefits_AddedInfo(counter)
        counter = buildBenefits_Hierarchie(counter)
        return counter
    }

/**
 * Builds the <i>USP_{Nr}_Picture</i> and the the <i>USP_{Nr}_Icon</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>. This contains 14 times a pair of each.
 * <br/><br/>
 * <b>TODO: USP_xx_Icon not specified</b>
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_USP_Pictures(int counter) {
        def content
        List proBenRefs = getSubType(contentObject, 'referenceTypeCode', 'ref_product_benefit_reference')
        List imgSources = new ArrayList()
        if (proBenRefs.size() > 0) {
            proBenRefs?.each { proBenRef ->
                imgSources.addAll(getSubType(proBenRef.target?.contentObjectMediaReferences, 'referenceTypeCode', 'ref_content_object_image'))
            }
        }
        def list = imgSources
        14.times {
            if (it < list.size()) {
                def obj = list[it]
                if (obj) {
                    content = (content) ? "${content}?impolicy=z-derivate&imwidth=442" : ''
                } else {
                    logger.log("[WARNING] No entry found for USP_${it + 7}_Picture nor USP_${it + 7}_Icon - ${article.code}")
                    content = ''
                } //take first one - same in catExport
            } else {
                content = ''
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "USP_${it + 7}_Picture") { mkp.yieldUnescaped(setCDATA(content)) }
            //Currently not available in PIM
            //create empty attributes 14 times the columns starting from 7 up to 20 with empty CDATA
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "USP_${it + 7}_Icon") { mkp.yieldUnescaped(setCDATA('')) }
        }
        return counter
    }
/**
 * Builds the <i>Price</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_Price(int counter) {
        // Recommended retail price
        def price = getPrice('RECOMMENDED_RETAIL_PRICE')
        def formattedPrice = (price) ? "${this.localeCurrencyFormatter.format(price)} ${this.currency}" : ''
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Price") { mkp.yieldUnescaped(setCDATA(formattedPrice)) }
        // Direct selling price
        price = getPrice('DIRECT_SELLING_PRICE')
        formattedPrice = (price) ? "${this.localeCurrencyFormatter.format(price)} ${this.currency}" : ''
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Miele_Direct_Selling_Price") { mkp.yieldUnescaped(setCDATA(formattedPrice)) }
        return counter
    }
/**
 * Builds the <i>Energylabel_Icon</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_Energylabel_Icon(int counter) {
        def content
        List onlinelabels = getSubType(proMediaRef, 'referenceTypeCode', 'ref_pim_onlinelabel')
        content = onlinelabels[0]?.target?.url
        content = (content) ? "${content}?impolicy=z-boxed&d=191" : ''
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Energylabel_Icon') { mkp.yieldUnescaped(setCDATA(content)) }
        return counter
    }
/**
 * Builds the <i>Award_${Nr}</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>. Contained always three times (even with null values).
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_Award(int counter) {
        List awards = getSubType(proMediaRef, 'referenceTypeCode', 'ref_pim_award')
        3.times {
            if (it < awards.size()) {
                def content = awards[it]?.target?.url
                content = (content) ? "${content}?impolicy=z-boxed&d=191" : ''
                builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Award_${it + 1}") { mkp.yieldUnescaped(setCDATA(content)) }
            } else {
                builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Award_${it + 1}") { mkp.yieldUnescaped(setCDATA('')) }
            }
        }
        return counter
    }
/**
 * Builds the <i>Manual</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_Manual(int counter) {
        def content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_user_manual')[0]?.target?.url
        // fall back for manual
        if (!content) {
            content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_operating_installation_instructions')[0]?.target?.url
        }
        if (!content) {
            content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_operating_instructions_ac')[0]?.target?.url
        }
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Manual') { mkp.yieldUnescaped(setCDATA(content)) }
        return counter
    }
/**
 * Builds the <i>EU-Datasheet</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_EU_Datasheet(int counter) {
        def content
        content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_eu_pi_data_sheet_pdf')[0]?.target?.url
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'EU-Datasheet') { mkp.yieldUnescaped(setCDATA(content)) }
        return counter
    }
/**
 * Builds the <i>Energylabel_PDF</i>, <i>Energylabel_PNG</i> and <i>EU-Onlinelabel</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_EU_Label(int counter) {
        def content
        content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_energylabel_to_product_pdf')[0]?.target?.url
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'EU-Energylabel_PDF') { mkp.yieldUnescaped(setCDATA(content)) }
        content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_energylabel_to_product_png')[0]?.target?.url
        content = (content) ? "${content}?impolicy=z-boxed&d=191" : ''
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'EU-Energylabel_PNG') { mkp.yieldUnescaped(setCDATA(content)) }
        content = getSubType(proMediaRef, 'referenceTypeCode', 'ref_pim_onlinelabel')[0]?.target?.url
        content = (content) ? "${content}?impolicy=z-boxed&d=191" : ''
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'EU-Onlinelabel') { mkp.yieldUnescaped(setCDATA(content)) }
        return counter
    }
/**
 * Builds the <i>PRODUCT BENEFIT DETAILS</i> content as <i>ATTRIBUTE</i> sets within <i>BENEFITS</i>.<br/>
 * Each set consists of the following attributes:
 * <br/><br/>
 * <ol>
 *     <li>Productbenefit${Nr}_ID</li>
 *     <li>Productbenefit${Nr}_picture</li>
 *     <li>Productbenefit${Nr}_video</li>
 *     <li>Productbenefit${Nr}_label</li>
 *     <li>Productbenefit${Nr}_headline</li>
 *     <li>Productbenefit${Nr}_text</li>
 *     <li>Productbenefit${Nr}_longtext</li>
 *     <li>Productbenefit${Nr}_footnote</li>
 *     <li>Productbenefit${Nr}_category</li>
 *     <li>Productbenefit${Nr}_highlight</li>
 *     <li>Productbenefit${Nr}_corefeature</li>
 *     <li>Productbenefit${Nr}_sequence_no</li>
 *     <li>Productbenefit${Nr}_onlyMiele</li>
 * </ol>
 * <br/>
 * Such list gets created 40 times fixed, regardless of lack of data with numbering incrementing on repetition level.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_AttributeSets(int counter) {

        List probenrefs = getAllFeaturesWithCode(contentObject, 'ref_product_benefit_reference')

        def content = ''
        40.times {

            // ID
            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.formattedCode
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_ID") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            // pic
            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.contentObjectMediaReferences.find { entity -> entity.referenceTypeCode == 'ref_content_object_image' }?.target?.url
            }
            content = (content) ? "${content}?impolicy=z-derivate&imwidth=442" : ''
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_picture") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            // vid
            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.contentObjectMediaReferences.find { entity -> entity.referenceTypeCode == 'ref_content_object_video' }?.target?.url
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_video") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            // label
            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.titleText
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_label") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            // headline
            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.subTitleText
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_headline") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.shortText
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_text") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.longText
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_longtext") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''
            if (it < probenrefs.size()) {
                def inlineText = probenrefs[it].target?.contentObjectReferences.find { it.referenceTypeCode == 'inline_reference_titleText' && it.target?.contentObjectTypeCode == 'patent_information' }
                if (inlineText?.target?.longText) {
                    content = inlineText?.target?.longText
                } else {
                    content = probenrefs[it].target?.titleText
                }
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_footnote") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.contentObjectFeatures.find { cof -> cof?.attributeCode == 'atr_DM_10117' }?.value?.toString()
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_category") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.contentObjectFeatures.find { cof -> cof?.attributeCode == 'atr_DM_10131' }?.value?.toString() //atr_DM_131
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_highlight") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.contentObjectFeatures.find { cof -> cof?.attributeCode == 'atr_DM_10130' }?.value?.toString() //atr_DM_130
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_corefeature") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].sortOrder?.toString()
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_sequence_no") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

            if (it < probenrefs.size()) {
                content = probenrefs[it].target?.contentObjectFeatures.find { cof -> cof?.attributeCode == 'atr_DM_10116' && cof?.multiValuePosition == 1 }?.attributeValueCode?.toString()
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productbenefit${it + 1}_onlyMiele") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''

        }
        return counter
    }
/**
 * Builds the <i>Added_Info${Nr}_ID</i>, <i>Added_Info${Nr}_label</i> and <i>Added_Info${Nr}_text</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 * Gets built ten times with numbering incrementing on repetition level.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_AddedInfo(int counter) {
        List contentObjectList = getSubType(contentObject, 'referenceTypeCode', 'ref_product_benefit_reference')
        def id, label, text
        10.times {

            if (it < contentObjectList.size()) {
                id = contentObjectList[it].target?.formattedCode
                label = contentObjectList[it].target?.titleText
                text = contentObjectList[it].target?.longText
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Added_Info${it + 1}_ID") { mkp.yieldUnescaped(setCDATA(id)) }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Added_Info${it + 1}_label") { mkp.yieldUnescaped(setCDATA(label)) }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Added_Info${it + 1}_text") { mkp.yieldUnescaped(setCDATA(text)) }

            id = ''
            label = ''
            text = ''
        }
        return counter
    }
/**
 * Builds the <i>Hierarchie_label</i> and <i>Hierarchie_id</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildBenefits_Hierarchie(int counter) {
        Map piClass = this.productToClassReferences.get('COMMUNICATION_STRUCTURE').get(this.article.code)
        List hierarchyStack = getHierarchyForClass(piClass)
        if (hierarchyStack.size() == 0) {
            this.logger.log("[WARNING] Hierarchy determination unsuccessful for article ${this.article.code} in classification ${piClass?.code} with stack ${hierarchyStack}")
        }
        def content = hierarchyStack.collect { it.label }.join('\\')
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Hierarchie_label') { mkp.yieldUnescaped(setCDATA(content)) }

        content = hierarchyStack.collect { it.id }.join('\\')
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Hierarchie_id') { mkp.yieldUnescaped(setCDATA(content)) }

        return counter
    }

    List getHierarchyForClass(Map classification) {
        List output = []
        if (classification) {
            Map parentClass = this.parentClassReferences.get(classification?.code)
            if (parentClass) {
                List parentHierarchy = getHierarchyForClass(parentClass)
                if (parentHierarchy.size()) {
                    output.addAll(parentHierarchy)
                }
            }
            if (classification.externalObjectTypeCode.endsWith('_langSpecific')) {
                // Strip prefix and leading zeros from id
                output.add([label: classification?.name, id: classification?.code?.replaceAll(/(\w+_)*0*(\d+)/, '$2')])
            }
        }
        return output
    }
/**
 * Builds ten times <i>ProductvideoXX</i>, 40 times <i>Installation_drawingXX</i> and <i>Added_Info${Nr}_text</i> content as <i>ATTRIBUTE</i>s within <i>BENEFITS</i>.
 * Gets built ten times with numbering incrementing on repetition level.
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildAdditionalProductData_Media(int counter) {
        List videos = []
        List refTypes = this.mediaTypeMapping.get('PI_PLUS_m_VIDEO')
        refTypes.each { refType ->
            videos.addAll(getSubType(proMediaRef, 'referenceTypeCode', refType))
        }

        def content = ''
        10.times {
            if (it < videos.size()) {
                content = videos[it]?.target?.url
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Productvideo${it + 1}") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''
        }

        List installationDrawings = new ArrayList()
        installationDrawings.addAll(getSubType(proMediaRef, 'referenceTypeCode', 'ref_installation_drawings'))
        40.times {
            if (it < installationDrawings.size()) {
                content = installationDrawings[it]?.target?.url
            }
            content = (content) ? "${content}?impolicy=z-derivate&imwidth=442" : ''
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Installation_drawing${it + 1}") { mkp.yieldUnescaped(setCDATA(content)) }
            content = ''
        }

        List warranty = new ArrayList()
        warranty.addAll(getSubType(proMediaRef, 'referenceTypeCode', 'ref_warranty_conditions'))
        if (warranty.size() > 0) {
            content = warranty[0]?.target?.url
        }
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Guarantee_booklet1') { mkp.yieldUnescaped(setCDATA(content)) }
        content = ''

        List waterproof = new ArrayList()
        waterproof.addAll(getSubType(proMediaRef, 'referenceTypeCode', 'ref_waterproof_warranty'))
        if (waterproof.size() > 0) {
            content = waterproof[0]?.target?.url
        }
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Water_protection_guarantee_1') { mkp.yieldUnescaped(setCDATA(content)) }

        return counter
    }
/**
 * Builds six times <i>ProdFeat${it + 1}_text</i> and <i>ProdFeat${it + 1}_reference</i> with empty CDATA when no data found. Additionally one <i>Designtype</i> and one <i>Shortpos</i> are created
 *
 * @param counter needed for exact counting
 * @return new counter value
 */
    int buildAdditionalProductData_References(int counter) {
        List proFeatRef = new ArrayList<>()
        proFeatRef.addAll(getSubType(contentObject, 'referenceTypeCode', 'ref_product_feature_reference'))

        def text = ''
        def refer = ''

        6.times {
            if (it < proFeatRef.size()) {
                text = proFeatRef[it]?.target?.shortText
                refer = proFeatRef[it]?.target?.formattedCode
            }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "ProdFeat${it + 1}_text") { mkp.yieldUnescaped(setCDATA(text)) }
            text = ''
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "ProdFeat${it + 1}_reference") { mkp.yieldUnescaped(setCDATA(refer)) }
            refer = ''
        }

        def designtype = (getSubType(contentObject, 'referenceTypeCode', 'ref_design_type_reference'))[0]?.target?.shortText
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Designtype') { mkp.yieldUnescaped(setCDATA(designtype)) }
        def shortpos = (getSubType(contentObject, 'referenceTypeCode', 'ref_short_positioning_reference'))[0]?.target?.shortText
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Shortpos') { mkp.yieldUnescaped(setCDATA(shortpos)) }

        return counter
    }

    int buildAdditionalProductData_Legal_Datasheet(int counter) {

        List mDS = []
        List refTypes = this.mediaTypeMapping.get('PI_PLUS_m_DS')
        refTypes.each { refType ->
            mDS.addAll(getSubType(proMediaRef, 'referenceTypeCode', refType))
        }

        7.times {
            def refTypeCode = ''
            def url = ''
            if (it < mDS.size()) {
                refTypeCode = mDS[it]?.referenceTypeCode
                url = mDS[it]?.target?.url
            }

            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Legal_Datasheet${it + 1}_Type") { mkp.yieldUnescaped(setCDATA(refTypeCode)) }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: "Legal_Datasheet${it + 1}") { mkp.yieldUnescaped(setCDATA(url)) }
        }

        def content = (getFeatureByCodeAndPosition('atr_VMSTA', 1))?.formattedValue
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Sales_Status') { mkp.yieldUnescaped(setCDATA(content)) }
        
        content = new SimpleDateFormat('yyyyMMdd').format(new SimpleDateFormat('yyyy-MM-dd').parse(article.publishedFromDate))
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Valid_From') { mkp.yieldUnescaped(setCDATA(content)) }

        content = new SimpleDateFormat('yyyyMMdd').format(new SimpleDateFormat('yyyy-MM-dd').parse(article.publishedUntilDate))
        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Valid_To') { mkp.yieldUnescaped(setCDATA(content)) }

        def parentProduct = products.find { it.code == article.parentCode }
        if (parentProduct) {
            def definingAttr = parentProduct.productFeatures.find { it.attributeCode == 'atr_variety_defining_attribute' && it.multiValuePosition == 1 }
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Web_Chooser_Attribute') { mkp.yieldUnescaped(setCDATA(definingAttr?.attributeValueCode?.replace('atr_', ''))) }
        } else {
            builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Web_Chooser_Attribute') { mkp.yieldUnescaped(setCDATA('')) }
        }

        builder.ATTRIBUTE(pos: countFormatter(counter++), name: 'Price_per_Unit') { mkp.yieldUnescaped(setCDATA(calculatePricePerUnit())) }

        return counter
    }

    String calculatePricePerUnit() {
        def price = getPrice('RECOMMENDED_RETAIL_PRICE')
        def vpreh = getFeatureByCodeAndPosition('atr_VPREH', 1)
        def inhal = getFeatureByCodeAndPosition('atr_INHAL', 1)
        def inhme = getFeatureByCodeAndPosition('atr_INHME_shortText', 1)
        if ((price as BigDecimal) && (vpreh?.value as BigDecimal) && (inhal?.value as BigDecimal) && inhme?.formattedValue) {
            BigDecimal pricePerUnit = (price as BigDecimal) * (vpreh?.value as BigDecimal) / (inhal?.value as BigDecimal)
            return "${vpreh.value} ${inhme.formattedValue} = ${this.localeCurrencyFormatter.format(pricePerUnit)} ${this.currency}"
        } else {
            logger.log("[WARNING] Price_per_Unit cannot be calculated for product code - ${article.code}. Price='${price}', atr_VPREH='${vpreh?.value}', atr_INHAL='${inhal?.value}', atr_INHME_shortText='${inhme?.formattedValue}' ")
            return ''
        }
    }
//endregion

}