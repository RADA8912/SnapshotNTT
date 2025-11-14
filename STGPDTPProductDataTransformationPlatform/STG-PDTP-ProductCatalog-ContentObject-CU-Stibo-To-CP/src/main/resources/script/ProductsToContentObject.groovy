/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import org.apache.commons.lang3.time.DateUtils
import src.main.resources.script.FeaturesBuilder
import src.main.resources.script.LanguageConverter
import src.main.resources.script.MappingLogger

import java.text.SimpleDateFormat

Message processData(Message message) {
    byte[] content = message.getBody(byte[])
    def root = new XmlParser().parse(new InputStreamReader(new ByteArrayInputStream(content), 'UTF-8'))

    MappingLogger logger = new MappingLogger(message)
    
    Map catalogVersions = new JsonSlurper().parseText(message.getHeader('XCatalogVersions', String))
    def catalogLanguages = message.getHeader('XCatalogLanguages', String)
    def languageMappingEntries = message.getHeader('XLanguageMapping', String)

    Map lifecycleMapping = new JsonSlurper().parseText(message.getHeader('XLifecycleMapping', String))
    Boolean lifecycleValidFromDisabled = message.getProperty('lifecycleValidFromDisabled') ? message.getProperty('lifecycleValidFromDisabled').toBoolean() : false
    Boolean lifecycleValidToDisabled = message.getProperty('lifecycleValidToDisabled') ? message.getProperty('lifecycleValidToDisabled').toBoolean() : false
    Map forbiddenCharactersMapping = new JsonSlurper().parseText(message.getHeader('XForbiddenCharactersMapping', String) ?: '{}')
    logger.debug("forbiddenCharactersMapping = ${forbiddenCharactersMapping}")
    LanguageConverter converter = LanguageConverter.newConverter(catalogLanguages, languageMappingEntries)
    FeaturesBuilder featuresBuilder = new FeaturesBuilder()

    List<String> validLanguages = catalogLanguages.split(',').collect()

    String contentObjectPrefix = message.getHeader('XContentObjectPrefix', String)
    def prefixes = contentObjectPrefix.split(',').toList()

    def products = root.'**'.findAll { it.name() == 'Product' && isContentObject(it.@ID.toString(), prefixes) }

    // Define target payload
    Writable writable = new StreamingMarkupBuilder().bind { builder ->
        builder.batchParts {
            builder.batchChangeSet1 {
                products.each { Node product ->
                    if (checkValidForLifecycle(product, lifecycleMapping, converter, lifecycleValidFromDisabled, lifecycleValidToDisabled)) {
                        String contentObjectType = product.Values.Value.find { it.@AttributeID == 'atr_content_object_type' }?.@ID
                        builder.batchChangeSetPart1 {
                            method('POST')
                            uri('ContentObject')
                            builder.body {
                                builder.ContentObject {
                                    builder.ContentObject {
                                        builder.catalogVersion_ID(catalogVersions.collect { it.value }.join(','))
                                        builder.code(product.@ID)
                                        builder.externalObjectTypeCode(product.@UserTypeID)
                                        builder.contentObjectTypeCode(contentObjectType)
                                        builder.formattedCode(formatCode(product.@ID))
                                        builder.parentCode((product.@UserTypeID == 'obj_subgeneric_content_object') ? product.parent().@ID : '')
                                        builder.publishedFromDate(getDateFromValue(product, 'atr_publication_valid_from_country', converter)?.format('yyyy-MM-dd') ?: '1900-01-01')
                                        builder.publishedUntilDate(getDateFromValue(product, 'atr_publication_expiry_date_country', converter)?.format('yyyy-MM-dd') ?: '9999-12-31')
                                        List<Map> convertedNames = converter.convert(product.Name.collect().toArray())
                                        def convertedLongTexts = getLongTextEntries(product, contentObjectType, converter)
                                        def convertedShortTexts = getShortTextEntries(product, contentObjectType, converter)
                                        def convertedSubTitleTexts = getSubTitleTextEntries(product, contentObjectType, converter)
                                        def convertedTitleTexts = getTitleTextEntries(product, contentObjectType, converter)
                                        builder.texts {
                                            validLanguages.each { currentLanguage ->
                                                builder.ContentObjectTexts {
                                                    builder.locale(currentLanguage)
                                                    builder.name(convertedNames.find { it.lang == currentLanguage }?.node)
                                                    builder.originQualifier(convertedNames.find { it.lang == currentLanguage }?.qualifiers)
                                                    builder.longText(removeForbiddenCharacters(forbiddenCharactersMapping,convertedLongTexts.get(currentLanguage), product.@ID, logger))
                                                    builder.shortText(removeForbiddenCharacters(forbiddenCharactersMapping,convertedShortTexts.get(currentLanguage), product.@ID, logger))
                                                    builder.subTitleText(removeForbiddenCharacters(forbiddenCharactersMapping,convertedSubTitleTexts.get(currentLanguage), product.@ID, logger))
                                                    builder.titleText(removeForbiddenCharacters(forbiddenCharactersMapping,convertedTitleTexts.get(currentLanguage), product.@ID, logger))
//												remarkText() // TODO - specification to be confirmed
                                                }
                                            }
                                        }
                                        builder.contentObjectFeatures {
                                            featuresBuilder.build(builder, 'ContentObjectFeature', product.Values, converter)
                                        }
                                        builder.contentObjectMediaReferences {
                                            Set<String> uniqueAssets = []
                                            product.AssetCrossReference.each { mediaRef ->
                                                List languages = (mediaRef.@QualifierID) ? converter.languageMapping[mediaRef.@QualifierID]?.languages : ['']
                                                languages.each { refLanguageCode ->
                                                    String key = "${mediaRef.@AssetID}_${mediaRef.@Type}_${refLanguageCode}"
                                                    if (!uniqueAssets.contains(key)) {
                                                        uniqueAssets.add(key)
                                                        builder.ContentObjectMediaReference {
                                                            builder.targetCode(mediaRef.@AssetID)
                                                            builder.referenceTypeCode(mediaRef.@Type)
                                                            builder.languageCode(refLanguageCode)
//													sortOrder() // TODO - specification to be confirmed
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        builder.contentObjectReferences {
                                            product.ProductCrossReference.findAll { isContentObject(it.@ProductID.toString(), prefixes) }.each { contentObjRef ->
                                                builder.ContentObjectReference {
                                                    builder.targetCode(contentObjRef.@ProductID)
                                                    builder.referenceTypeCode(contentObjRef.@Type)
                                                    builder.sortOrder(contentObjRef.MetaData.Value.find { it.@AttributeID == 'atr_sort_order' })
                                                }
                                            }
                                            buildInlineReference(builder, convertedLongTexts, validLanguages[0], 'inline_reference_longText')
                                            buildInlineReference(builder, convertedShortTexts, validLanguages[0], 'inline_reference_shortText')
                                            buildInlineReference(builder, convertedSubTitleTexts, validLanguages[0], 'inline_reference_subTitleText')
                                            buildInlineReference(builder, convertedTitleTexts, validLanguages[0], 'inline_reference_titleText')
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    def outputStream = new ByteArrayOutputStream()
    writable.writeTo(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')))
    message.setBody(outputStream)

    def messageLog = messageLogFactory?.getMessageLog(message)
    if (messageLog) {
        List logEntries = logger.getEntries()
        if (logEntries.size()) {
            StringBuilder sb = new StringBuilder()
            logEntries.each { sb << "${it}\r\n" }
            messageLog.addAttachmentAsString('MappingLogs', sb.toString(), 'text/plain')
        }
    }
    return message
}

boolean isContentObject(String id, List prefixes) {
    String inputPrefix = id.replaceFirst(/(\w+)_0*(\d+)_\d+/, '$1')
    return (inputPrefix in prefixes)
}

Map<String, Node> getLongTextEntries(Node product, String contentObjectType, LanguageConverter converter) {
	switch (contentObjectType) {
		case ['product_benefit']:
			return getAttributeEntries(product, 'atr_specific_content_glossar_text', converter)
		case ['guide_theme', 'special_topic', 'introduction_text', 'sector_theme', 'reference']:
			return getAttributeEntries(product, 'atr_generic_content_longtext', converter)
		case ['patent_information', 'legal_information']:
			return getAttributeEntries(product, 'atr_describing_content_longtext', converter)
		case ['description_text']:
			return getAttributeEntries(product, 'atr_specific_content_long_text', converter)
		case ['info_module']:
			return getAttributeEntries(product, 'atr_subgeneric_content_object_longtext', converter)
		default:
			return [:]
	}
}

Map<String, String> getShortTextEntries(Node product, String contentObjectType, LanguageConverter converter) {
    switch (contentObjectType) {
        case ['product_benefit']:
            return getAttributeEntries(product, 'atr_specific_content_middle_text', converter)
        case ['short_positioning', 'design_type', 'product_feature']:
            return getAttributeEntries(product, 'atr_specific_content_short_text', converter)
        default:
            return [:]
    }
}

Map<String, String> getSubTitleTextEntries(Node product, String contentObjectType, LanguageConverter converter) {
    switch (contentObjectType) {
        case ['product_benefit']:
            return getAttributeEntries(product, 'atr_specific_content_headline', converter)
        case ['guide_theme', 'special_topic', 'introduction_text', 'sector_theme', 'reference']:
            return getAttributeEntries(product, 'atr_generic_content_headline', converter)
        case ['info_module']:
            return getAttributeEntries(product, 'atr_subgeneric_content_headline', converter)
        default:
            return [:]
    }
}

Map<String, String> getTitleTextEntries(Node product, String contentObjectType, LanguageConverter converter) {
    switch (contentObjectType) {
        case ['product_benefit', 'description_text', 'info_module']:
            return getAttributeEntries(product, 'atr_specific_content_name', converter)
        case ['guide_theme', 'special_topic', 'introduction_text', 'sector_theme', 'reference']:
            return getAttributeEntries(product, 'atr_generic_content_name', converter)
        case ['patent_information', 'legal_information']:
            return getAttributeEntries(product, 'atr_describing_content_name', converter)
        default:
            return [:]
    }
}

Map<String, Node> getAttributeEntries(Node product, String attributeName, LanguageConverter converter) {
	def entries = product.Values.ValueGroup.find { it.@AttributeID == attributeName }?.Value?.collect()?.toArray()
	if (!entries?.size()) {
		def value = product.Values.Value.find { it.@AttributeID == attributeName }
		if (value) {
			entries = [value].toArray()
		} else {
			entries = [].toArray()
		}
	}
	List<Map> output = converter.convert(entries)
	return output.collectEntries{[(it.lang): it.node] }
}

String removeForbiddenCharacters(Map forbiddenCharactersMapping, Object value, String coCode, MappingLogger logger) {
    if(!value) return;
    String result = value.text()
    // Convert to list using Unicode code point hex format - example \u000A
    List forbiddenCharacters = forbiddenCharactersMapping.get('ALL')?.split(';')?.collect { "\\u${it}" }
    if (forbiddenCharacters) {
        // Build pattern containing all forbidden characters for an OR search 
        String matchPattern = forbiddenCharacters.join('')
        if (result.toCharArray().any() { it ==~ /[${matchPattern}]/ }) {
            logger.debug("Unicode character(s) ${forbiddenCharacters} found in text of content object - ${coCode}")
            forbiddenCharacters.each {character ->
                if (result.toCharArray().any() { it ==~ /${character}/ }) {
                    logger.debug("Entry before removal of ${character} - ${result}")
                    result = result.replaceAll(character, '')
                    logger.debug("Entry after removal of ${character} - ${result}")
                }
            }
        }
    }
    return result
}

String formatCode(String input) {
    return input?.replaceFirst(/(\w+)_0*(\d+)_\d+/, '$2-$1')
}

List getInlineRefs(String inputText) {
    def inlineReferences = []
    String regexPattern = /#([^#]+)#<inRef\.objid>([^<]+)<\/inRef\.objid>/
    def matcher = (inputText =~ regexPattern)
    matcher.size().times {
        inlineReferences << matcher[it][2]
    }
    return inlineReferences.toUnique()
}

void buildInlineReference(Object builder, Map<String, Node> texts, String language, String refTypeCode) {
	Node node = texts.get(language)
	getInlineRefs(node?.text()).eachWithIndex { refCode, index ->
		builder.ContentObjectReference {
			builder.targetCode(refCode)
			builder.referenceTypeCode(refTypeCode)
			builder.sortOrder(index)
		}
	}
}

boolean checkValidForLifecycle(Node product, Map lifecycleMapping, LanguageConverter converter, boolean validFromDisabled, boolean validToDisabled) {
    def mappingValue = lifecycleMapping[product.@UserTypeID]
    def dateNow = new Date()
    boolean validFromIsOk, validToIsOk
    if (mappingValue) {
        //General check
        def value = product.Values.Value.find { it.@AttributeID == 'atr_lifecycle_content_object' && it.@ID == mappingValue }
        if (!value) {
            value = product.Values.ValueGroup.find { it.@AttributeID == 'atr_lifecycle_content_object' }?.Value.find { it.@ID == mappingValue }
        }
        if (!value) {
            return false
        }

        //Valid to
        if (!validToDisabled) {
            def validTo = getDateFromValue(product, 'atr_publication_expiry_date_country', converter)
            if (validTo) {
                if (dateNow.before(validTo) || DateUtils.isSameDay(validTo,dateNow)) {
                    validToIsOk = true
                }
            } else {
                validToIsOk = true
            }
        } else {
            validToIsOk = true
        }

        if (validToIsOk) {
            return true
        }

    } else {
        return true;
    }
}

Date getDateFromValue(Node product, String attributeID, LanguageConverter converter) {
    def validDates = product.Values.Value.findAll { it.@AttributeID == attributeID }
    Map<Integer, Node> priorityMap = [:]
    if (!validDates) {
        validDates = product.Values.ValueGroup.find { it.@AttributeID == attributeID }?.Value.findAll { it.name() == 'Value' }
    }
    if (!validDates) {
        return
    } else {
        validDates.each { validDate ->
            priorityMap.put(converter.languageMapping[validDate.@QualifierID].priority, validDate)
        }
        def sortedKeys = priorityMap.sort()*.key
        def nodeValue = priorityMap[sortedKeys[0]].text()
        def date = new SimpleDateFormat('yyyy-MM-dd').parse(nodeValue)
        return date
    }
}