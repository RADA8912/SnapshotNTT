/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

class ContentObjectBuilder {
    final Object builder
    final Map<String, Map> contentObjectCache
    final Map shapeTypes
    final Map shapeRatios
    final Map shapeMapping
    final Map shapeRatioMapping
    final MappingLogger logger
    final Set cumulativeReferencedCOs
    public List<String> mainFilters = []

    ContentObjectBuilder(Object builder, List coReferences, Message message, MappingLogger logger) {
        this.builder = builder
        this.shapeTypes = new JsonSlurper().parseText(message.getProperty('XMediaShapeTypes'))
        this.shapeRatios = new JsonSlurper().parseText(message.getProperty('XMediaShapeRatios'))
        this.shapeMapping = new JsonSlurper().parseText(message.getProperty('XMediaShapeMapping'))
        this.shapeRatioMapping = new JsonSlurper().parseText(message.getProperty('XMediaShapeRatioMapping'))
        this.logger = logger
        // Cache all available content objects for lookup later when resolving inline reference and output generation of nodes
        this.contentObjectCache = cacheContentObjects(coReferences)
        this.cumulativeReferencedCOs = []
    }

    private Map cacheContentObjects(List coReferences) {
        Map cache = [:]
        List contentObjects = coReferences.flatten().findAll { it?.target }*.target
        cache.putAll(contentObjects.collectEntries { [(it.formattedCode): it] })
        if (contentObjects*.contentObjectReferences) {
            cache.putAll(cacheContentObjects(contentObjects*.contentObjectReferences))
        }
        return cache
    }

    void buildBenefits(List productReferences, String shapeElement, List filterClasses) {
        if (contentObjectTypeExists(productReferences, 'ref_product_benefit_reference')) {
            logger.debug('Building benefit Content Objects')
            builder.benefits {
                // Create a benefit category for each unique category, with list of content objects in it
                getBenefitCategories(productReferences).each { category ->
                    benefitCategory(bgID: category.benefitID, label: category.label) {
                        logger.debug("Building benefit category ${category.benefitID}")
                        category.coRefs.sort { it.sortOrder }.each { coRef ->
                            buildSingleCONode('io_PRODBEN', coRef, shapeElement, category.product, filterClasses, false, false, false)
                        }
                    }
                }
            }
        }
    }

    void buildBenefits(Map product, String shapeElement, List filterClasses) {
        // Nest the product as a single (nested) entry in a List
        List prodRef = [[target: product]]
        buildBenefits(prodRef, shapeElement, filterClasses)
    }

    void buildContentObjectReferences(String nodeName, List coReferences, String shapeElement) {
        // This is called from Catalog-Area so we do not need to pass on the product which
        // is used in resolving inline reference to attributes
        coReferences.each { coRef ->
            buildSingleCONode(nodeName, coRef, shapeElement)
        }
    }

    private boolean contentObjectTypeExists(List productReferences, String refTypeCode) {
        return productReferences.any { prodRef ->
            prodRef.target?.productContentObjectReferences?.any { it.referenceTypeCode == refTypeCode }
        }
    }

    void buildSingleCONode(String nodeName, Map coRef, String shapeElement) {
        buildSingleCONode(nodeName, coRef, shapeElement, null, [], false, false, false)
    }

    void buildSingleCONode(String nodeName, Map coRef, String shapeElement, Map product, List filterClasses, boolean skipInlineRefRecursion, boolean includeVideo, boolean includeIMGTag) {
        logger.debug("Building Content Object node ${coRef.target.code}")
        // Create each node based on the content object details
        builder."${nodeName}"(constructContentObjectNodeAttributes(coRef, filterClasses, product, nodeName)) {
            // Texts
            buildTexts(builder, coRef, product, skipInlineRefRecursion)
            // Images
            buildImages(builder, coRef, shapeElement, includeIMGTag)
            // Video - applicable for info detail
            if (includeVideo) {
                buildVideos(builder, coRef)
            }
        }
    }

    void buildSingleBenefitNode(Map coRef, String shapeElement) {
        logger.debug("Building Content Object node ${coRef.target.code}")
        builder.io_PRODBEN(constructContentObjectNodeAttributes(coRef, [], null, 'io_PRODBEN')) {
            // Benefit Category - applicable for InfoDetail
            buildBenefitCategory(coRef.target, builder)
            // Texts
            buildTexts(builder, coRef, null, false)
            // Images
            buildImages(builder, coRef, shapeElement, true)
            // Video - applicable for InfoDetail
            buildVideos(builder, coRef)
            // Long text
            if (coRef.target.longText) {
                builder.modules {
                    builder.ioM_GLOSSAR {
                        Map longTextContent = resolveInlineText(coRef.target.longText, false, coRef.target.code)
                        builder.text(getNodeRefID(longTextContent.references)) { mkp.yieldUnescaped(addCData(longTextContent.text)) }
                    }
                }
            }
        }
    }

    void buildSingleNodeWithModules(String nodeName, Map coRef, String shapeElement, List modules, String longTextShapeElement) {
        logger.debug("Building Content Object node ${coRef.target.code}")
        builder."${nodeName}"(constructContentObjectNodeAttributes(coRef, [], null, nodeName)) {
            // Texts
            buildTexts(builder, coRef, null, false)
            // Images
            buildImages(builder, coRef, shapeElement, true)
            // Video - applicable for InfoDetail
            buildVideos(builder, coRef)
            // Modules
            if (modules) {
                builder.modules {
                    modules.each { module ->
                        buildSingleCONode('ioM_LONGTEXT', [target: module], longTextShapeElement, null, [], false, true, true)
                    }
                }
            }
        }
    }

    private void buildTexts(MarkupBuilder builder, Map coRef, Map product, boolean skipInlineRefRecursion) {
        // Process texts - extracting inline references and performing replacements
        Map titleTextContent = resolveInlineText(coRef.target.titleText, skipInlineRefRecursion, coRef.target.code)
        Map subTitleTextContent = resolveInlineText(coRef.target.subTitleText, skipInlineRefRecursion, coRef.target.code)
        Map shortTextContent = resolveInlineText(coRef.target.shortText, skipInlineRefRecursion, coRef.target.code)
        Map longTextContent = (coRef.target.shortText) ? [:] : resolveInlineText(coRef.target.longText, skipInlineRefRecursion, coRef.target.code)
        // Replace inline attribute if product features are available
        if (product) {
            titleTextContent.text = replaceInlineAttribute(titleTextContent.text, product, coRef.target)
            subTitleTextContent.text = replaceInlineAttribute(subTitleTextContent.text, product, coRef.target)
            shortTextContent.text = replaceInlineAttribute(shortTextContent.text, product, coRef.target)
            longTextContent.text = (longTextContent.text) ? replaceInlineAttribute(longTextContent.text, product, coRef.target) : ''
        }

        // Generate text nodes output
        if (titleTextContent.text) {
            // Handle ampersand and non-breaking space conversion since it is unescaped and not wrapped in CData
            def labelContent = titleTextContent.text.replaceAll('&nbsp;', ' ').replaceAll('&', '&amp;')
            builder.label(getNodeRefID(titleTextContent.references)) { mkp.yieldUnescaped(labelContent) }
        }
        if (subTitleTextContent.text) {
            builder.headline(getNodeRefID(subTitleTextContent.references)) { mkp.yieldUnescaped(addCData(subTitleTextContent.text)) }
        }
        if (shortTextContent.text) {
            builder.text(getNodeRefID(shortTextContent.references)) { mkp.yieldUnescaped(addCData(shortTextContent.text)) }
        } else if (longTextContent.text) {
            builder.text(getNodeRefID(longTextContent.references)) { mkp.yieldUnescaped(addCData(longTextContent.text)) }
        }
    }

    private void buildImages(MarkupBuilder builder, Map coRef, String shapeElement, boolean includeIMGTag) {
        List images = getMediaReference(coRef, 'ref_content_object_image', !includeIMGTag)
        if (images) {
            if (includeIMGTag) { // applicable for InfoDetail
                builder.m_IMG(type: '06', typeName: 'Detail') {
                    images.each { image ->
                        buildSingleImage(builder, image, shapeElement)
                    }
                }
            } else {
                images.each { image ->
                    buildSingleImage(builder, image, shapeElement)
                }
            }
        }
    }

    private void buildSingleImage(MarkupBuilder builder, Map mediaRef, String shapeElement) {
        def url = getURL(mediaRef)
        builder.img(id: mediaRef.targetCode, src: url) {
            buildRatios(builder, mediaRef)
            if (url) {
                buildShapes(builder, mediaRef, url, shapeElement)
            }
        }
    }

    private void buildVideos(MarkupBuilder builder, Map coRef) {
        List videos = getMediaReference(coRef, 'ref_content_object_video', false)
        if (videos) {
            builder.m_VIDEO() {
                videos.each { vid ->
                    video(vid: vid.target?.code) {
                        shape {
                            format(src: vid.target?.url, type: 'URLVIDEO')
                            format(src: vid.target?.thumbnailUrl, type: 'URLTHUMB')
                        }
                    }
                }
            }
        }
    }

    void buildBenefitCategory(contentObject, builder) {
        List allAttributes = contentObject.contentObjectFeatures
        List foundAttributes = allAttributes.findAll { it.attributeCode == 'atr_DM_10117' && it.multiValuePosition == 1 }
        Map benefitCategoryAttributes = [:]
        if (foundAttributes.isEmpty()) {
            this.logger.log("[WARNING] No attribute found for benefitCategory element of contentObject ${contentObject.code}")
            benefitCategoryAttributes << [bgID: "", label: ""]
        } else {
            benefitCategoryAttributes << [bgID: foundAttributes[0]?.attributeValueCode, label: foundAttributes[0]?.value]
        }
        builder.benefitCategory(benefitCategoryAttributes) {}
    }

    void buildShapes(Object builder, Map image, String url, String shapeElement) {
        List shapes = shapeTypes.get(shapeElement)?.split(',')?.toList()
        shapes.each {
            String ratio = shapeRatios.get(shapeElement)
            Map<String, String> metadata = new JsonSlurper().parseText(image.target?.metaDataAsJson ?: '{}')
            String ratioFromMap = metadata?.get(ratio)?.replace(';', '&')
            if (image.target?.metaDataAsJson && ratio && ratioFromMap) {
                String src = "${url}${shapeRatioMapping.get(it)}"
                src = src.concat("&${ratioFromMap}")
                builder.shape(src: src, type: it)
                //fallback
            } else {
                builder.shape(src: "${url}${shapeMapping.get(it)}", type: it)
            }
        }
    }

    void buildRatios(Object builder, Map image) {
        if (image.target?.metaDataAsJson) {
            Map<String, String> metadata = new JsonSlurper().parseText(image.target?.metaDataAsJson)
            builder.ratios {
                metadata.each { ratio ->
                    builder.ratio(type: ratio.key) { mkp.yieldUnescaped(ratio.value.replace(';', '&amp;')) }
                }
            }
        }
    }

    List getContentObjectRefsByTypeCode(List contentObjectReferences, String typeCode) {
        List contentObjectRefs = []
        Set coCodes = []
        // Get all unique content objects
        contentObjectReferences?.each {
            if (it.target?.contentObjectTypeCode == typeCode && !coCodes.contains(it.targetCode)) {
                coCodes.add(it.targetCode)
                contentObjectRefs.add(it)
            }
        }
        return contentObjectRefs
    }

    void buildShortTexts(List coReferences, Map product) {
        buildShortText(coReferences, 'io_SHORTPOS', 'ref_short_positioning_reference', product, true, 1)
        buildShortText(coReferences, 'io_DESIGNTYPE', 'ref_design_type_reference', product, false, 1)
        buildShortText(coReferences, 'io_PRODFEAT', 'ref_product_feature_reference', product, false, 9999)
    }

    private void buildShortText(List coReferences, String nodeName, String refTypeCode, Map product, boolean mandatory, int maxCount) {
        List filteredCOReferences = coReferences.findAll { it.referenceTypeCode == refTypeCode && it.target }
        if (filteredCOReferences.size()) {
            if (maxCount && filteredCOReferences.size() > maxCount) {
                logger.log("[WARNING] More than ${maxCount} ${nodeName} references found for product code - ${product.code}")
            }
            filteredCOReferences.toSorted { a, b -> a.sortOrder <=> b.sortOrder ?: a.targetCode <=> b.targetCode }.eachWithIndex { Map coRef, int idx ->
                if (idx < maxCount) {
                    if (coRef.target.shortText) {
                        logger.debug("Building short text for Content Object node ${coRef.target.code}")
                        Map shortTextContent = resolveInlineText(coRef.target.shortText, false, coRef.target.code)
                        shortTextContent.text = replaceInlineAttribute(shortTextContent.text, product, coRef.target)
                        if (shortTextContent.text) {
                            builder."${nodeName}"(getNodeRefID(shortTextContent.references)) { mkp.yieldUnescaped(addCData(shortTextContent.text)) }
                        }
                    }
                }
            }
        } else if (mandatory) {
            logger.log("[WARNING] No ${nodeName} found for product code - ${product.code}")
        }
    }

    private List<Map> getBenefitCategories(List productReferences) {
        List<Map> benefitCategories = []
        // Get all unique benefit categories
        // Then get all unique content object in each category
        productReferences.each { prodRef ->
            def benefitContentObjectRefs = prodRef.target?.productContentObjectReferences?.findAll {
                it.referenceTypeCode == 'ref_product_benefit_reference'
            }
            benefitContentObjectRefs.each { Map prodCORef ->
                def benefitCategoryAttribute = getAttribute(prodCORef, 'atr_DM_10117', 1)
                if (benefitCategoryAttribute) {
                    Map benefitCategory = benefitCategories.find { it.benefitID == benefitCategoryAttribute.attributeValueCode }
                    if (benefitCategory) {
                        def category = benefitCategory.coRefs
                        if (!category.any { it.targetCode == prodCORef.targetCode }) {
                            category.add(prodCORef)
                        }
                    } else {
                        benefitCategories.add([benefitID: benefitCategoryAttribute.attributeValueCode, label: benefitCategoryAttribute.value, product: prodRef.target, coRefs: [prodCORef]])
                    }
                }
            }
        }
        return benefitCategories.sort { it.label }
    }

    void buildReference() {
        buildReference([:], [], [], '')
    }

    void buildReference(Map product, List filterClasses, List directReferences, String shapePrefix) {
        // For directReferences, process through all of the texts to extract further inline references
        if (directReferences) {
            directReferences.toSet().each { coFormattedCode ->
                Map contentObject = this.contentObjectCache.get(coFormattedCode)
                if (contentObject) {
                    resolveChildReferences(contentObject)
                }
            }
        }
        if (this.cumulativeReferencedCOs || directReferences) {
            logger.debug('Building reference Content Objects')
            Set referencedCOCodes = (this.cumulativeReferencedCOs + directReferences).toSet().sort()
            logger.debug("Inline references = ${this.cumulativeReferencedCOs}")
            logger.debug("Other direct references  = ${directReferences}")
            logger.debug("Total references = ${referencedCOCodes}")

            builder.reference {
                referencedCOCodes.each { coFormattedCode ->
                    Map contentObject = this.contentObjectCache.get(coFormattedCode)
                    if (contentObject) {
                        switch (contentObject.contentObjectTypeCode) {
                            case 'description_text':
                                buildSingleCONode('io_INFO', [target: contentObject], "${shapePrefix}-Ref-Info", product, filterClasses, true, false, false)
                                break
                            case 'product_benefit':
                                buildSingleCONode('io_PRODBEN', [target: contentObject], "${shapePrefix}-Ref-Benefit", product, filterClasses, true, false, false)
                                break
                            case 'product_feature':
                                buildSingleCONode('io_PRODFEAT', [target: contentObject], '', product, filterClasses, true, false, false)
                                break
                            case 'special_topic':
                                buildSingleCONode('io_SPECIAL', [target: contentObject], "${shapePrefix}-Ref-Info", product, filterClasses, true, false, false)
                                break
                            case 'patent_information':
                                logger.debug("Building Content Object node ${contentObject.code}")
                                builder.io_FOOTER(ioID: coFormattedCode) { mkp.yieldUnescaped(addCData(contentObject.longText)) }
                                break
                            default:
                                logger.warn("Referenced object not exposed for Content Object code ${coFormattedCode} with contentObjectTypeCode = ${contentObject.contentObjectTypeCode}")
                                break
                        }
                    } else {
                        logger.warn("Referenced object not found for Content Object code ${coFormattedCode}")
                    }
                }
            }
        }
    }

    private List getMediaReference(Map contentObjectRef, String referenceType, boolean getSingleEntry) {
        List mediaReferences = contentObjectRef.target?.contentObjectMediaReferences?.findAll { it.referenceTypeCode == referenceType }?.sort { it.targetCode }
        if (getSingleEntry) {
            if (mediaReferences?.size() > 1) {
                logger.warn("More than 1 ${referenceType} found for content object code - ${contentObjectRef.target.code}")
            }
            return mediaReferences?.size() ? [mediaReferences[0]] : []
        } else {
            return mediaReferences
        }
    }

    private String replaceInlineAttribute(String input, Map product, Map contentObject) {
        String regexPattern = /<inRef\.attrid>([^<]+)<\/inRef\.attrid>/
        def matcher = (input =~ regexPattern)
        matcher.size().times {
            def matchedLine = matcher[it][0]
            def attribute = matcher[it][1]
            def prodFeature = product.productFeatures.find { it.attributeCode == attribute }
            if (prodFeature) {
                def replacement = (prodFeature.attributeUnit?.name) ? "${prodFeature.formattedValue}&nbsp;${prodFeature.attributeUnit.name}" : prodFeature.formattedValue
                input = input.replaceFirst(matchedLine, replacement)
            } else {
                logger.log("[WARNING] Could not resolve inline attribute ${attribute} for contentObject ${contentObject.code} of product code - ${product.code}")
                // Fallback replacement
                input = input.replaceAll('inRef.attrid', 'inRef_attrid')
            }
        }
        return input
    }

    private Map constructContentObjectNodeAttributes(Map contentObjectRef, List filterClasses, Map product, String nodeName) {
        Map attributes = [ioID: contentObjectRef.target?.formattedCode ? contentObjectRef.target?.formattedCode : formatCode(contentObjectRef.target.code)]
        def onlyMiele = getAttribute(contentObjectRef, 'atr_DM_10116', 1)
        if (onlyMiele?.attributeValueCode == '1') {
            attributes << [onlyMiele: '1']
        }
        if ((contentObjectRef.target?.longText && contentObjectRef.target?.shortText) || contentObjectRef.target?.firstSubContentObject) {
            attributes << [detail: '1']
        }

        def highlight = getAttribute(contentObjectRef, 'atr_DM_10131', 1)
        if (highlight?.attributeValueCode == '1') {
            attributes << [highlight: '1']
        }
        def corefeature = getAttribute(contentObjectRef, 'atr_DM_10130', 1)
        if (corefeature?.attributeValueCode == '1') {
            attributes << [corefeature: '1']
        }
        def video = getMediaReference(contentObjectRef, 'ref_content_object_video', true)
        if (video) {
            attributes << [video: video[0].targetCode]
        }
        def mention = getAttribute(contentObjectRef, 'atr_DM_10137', 1)
        if (mention) {
            attributes << [mention: mention.value]
        }
        if (contentObjectRef.sortOrder) {
            attributes << [sequence_no: contentObjectRef.sortOrder]
        }
        def mainFilterCode = filterClasses.find { cl -> cl.externalObjectTypeCode == 'obj_filter_classification' && cl.classificationClassProductReferences.find { ref -> ref.targetCode == product?.code } }?.code
        def mainFilter = this.getFilterName(mainFilterCode)
        List filterItemClasses = filterClasses.findAll { all -> all.externalObjectTypeCode == 'obj_filter_item' && all.classificationClassContentObjectReferences.find { ref -> ref.targetCode == contentObjectRef.target.code } && all.code.contains("filter_item_${mainFilter}") }.sort { it.sortOrder }
        if (filterItemClasses.size() > 1) {
            logger.log("[WARNING] More than 1 obj_filter_item class found for content object code - ${contentObjectRef.target.code}")
        }
        if (mainFilter && filterItemClasses.size()) {
            attributes << [filter: mainFilter, filterKey: this.getFilterItemKey(filterItemClasses[0].code)]
            this.mainFilters.push(mainFilter)
        }
        if (builder.current == 'topics') {
            if (nodeName == 'io_BRANCHTHEME') {
                attributes << [layout: 'horizontal']
                return attributes
            } else if (nodeName == 'io_REFTHEME') {
                return attributes
            } else if (!contentObjectRef.sortOrder || contentObjectRef.sortOrder?.toInteger() > 5) {
                attributes << [layout: 'big']
            } else {
                attributes << [layout: 'stage']
            }
        }

        return attributes
    }

    private Map getNodeRefID(List references) {
        if (references) {
            return [refID: references.join(',')]
        } else {
            return [:]
        }
    }

    private Map getAttribute(Map contentObjectRef, String attributeCode, int multiValuePosition) {
        def attribute = contentObjectRef.target?.contentObjectFeatures?.find {
            it.attributeCode == attributeCode && it.multiValuePosition == multiValuePosition
        }
        return attribute
    }

    private String getURL(Map mediaReference) {
        return mediaReference.target?.url
    }

    private String formatCode(String input) {
        return input?.replaceFirst(/(\w+)_0*(\d+)_\d+/, '$2-$1')
    }

    private String getFilterName(String code) {
        return code?.replace('fclass-', '')
    }

    private String getFilterItemKey(String code) {
        return code?.split('-')?.last()
    }

    private String addCData(String input) {
        return "<![CDATA[${input}]]>"
    }

    void resetInlineReferences() {
        this.cumulativeReferencedCOs.clear()
    }

    Map resolveInlineText(String inputText, boolean skipRecursion, String parentCode) {
        Map output = [:]
        List inlineReferences = []
        String outputText = inputText
        String regexPattern = /#([^#]+)#<inRef\.objid>([^<]+)<\/inRef\.objid>/
        def matcher = (inputText =~ regexPattern)
        matcher.size().times {
            String formattedCode = formatCode(matcher[it][2])
            Map referencedContentObject = this.contentObjectCache.get(formattedCode)
            if (referencedContentObject) {
                inlineReferences << formattedCode
                outputText = updateText(outputText, formattedCode, matcher[it], true)
                if (!skipRecursion) {
                    // Logging and further recursive processing are skipped when called from buildReference
                    logger.debug("Inline reference ${formattedCode} found for ${parentCode}")
                    this.cumulativeReferencedCOs.add(formattedCode)
                    logger.debug("Processing texts for ${formattedCode} of ${parentCode}")
                    resolveChildReferences(referencedContentObject)
                }
            } else {
                outputText = updateText(outputText, formattedCode, matcher[it], false)
                if (!skipRecursion) { // Logging is skipped when called from buildReference
                    logger.warn("Inline text referenced object ${formattedCode} not found for ${parentCode}")
                }
            }
        }
        output.put('text', outputText)
        output.put('references', inlineReferences)
        return output
    }

    private void resolveChildReferences(Map contentObject) {
        if (contentObject.titleText) {
            resolveInlineText(contentObject.titleText, false, contentObject.code)
        }
        if (contentObject.subTitleText) {
            resolveInlineText(contentObject.subTitleText, false, contentObject.code)
        }
        if (contentObject.shortText) {
            resolveInlineText(contentObject.shortText, false, contentObject.code)
        }
        if (!contentObject.shortText && contentObject.longText) {
            resolveInlineText(contentObject.longText, false, contentObject.code)
        }
    }

    private String updateText(String text, String code, List matchedLine, boolean referenceFound) {
        def label = matchedLine[1]
        def prefix = matchedLine[2].take(3)
        if (referenceFound) {
            switch (prefix) {
                case 'ZFN':
                    return text.replace(matchedLine[0], "<a href=\"#FOOTER_${code}\" class=\"link-footnote\">${label}</a>")
                case 'ZPV':
                    return text.replace(matchedLine[0], "<a href=\"#PRODBEN_${code}\" class=\"mouseover\">${label}</a>")
                case 'ZPB':
                    return text.replace(matchedLine[0], "<a href=\"#PRODFEAT_${code}\" class=\"mouseover\">${label}</a>")
                case 'ZIO':
                    return text.replace(matchedLine[0], "<a href=\"#INFO_${code}\" class=\"mouseover\">${label}</a>")
                case 'ZST':
                    return text.replace(matchedLine[0], "<a href=\"#SPECIAL_${code}\" class=\"mouseover\">${label}</a>")
            }
        } else {
            switch (prefix) {
                case 'ZFN':
                    return text.replace(matchedLine[0], '')
                default:
                    return text.replace(matchedLine[0], label)
            }
        }
    }
}