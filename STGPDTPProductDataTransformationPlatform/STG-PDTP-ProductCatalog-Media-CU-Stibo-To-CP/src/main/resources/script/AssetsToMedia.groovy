/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import org.apache.commons.lang3.time.DateUtils
import src.main.resources.script.LanguageConverter

import java.text.SimpleDateFormat

Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlParser().parse(reader)

	Map catalogVersions = new JsonSlurper().parseText(message.getHeader('XCatalogVersions', String))

	def catalogLanguages = message.getHeader('XCatalogLanguages', String)
	def languageMappingEntries = message.getHeader('XLanguageMapping', String)
	LanguageConverter converter = LanguageConverter.newConverter(catalogLanguages, languageMappingEntries)

	Map lifecycleMapping = new JsonSlurper().parseText(message.getHeader('XLifecycleMapping', String))
	Boolean lifecycleValidFromDisabled = message.getProperty('lifecycleValidFromDisabled') ? message.getProperty('lifecycleValidFromDisabled').toBoolean() : false
	Boolean lifecycleValidToDisabled = message.getProperty('lifecycleValidToDisabled') ? message.getProperty('lifecycleValidToDisabled').toBoolean() : false

	List validLanguages = catalogLanguages.split(',').collect()

	// Define target payload
	Writable writable = new StreamingMarkupBuilder().bind { builder ->
		builder.batchParts {
			builder.batchChangeSet1 {
				root.Asset.each { asset ->
					if (checkValidForLifecycle(asset, lifecycleMapping, converter, lifecycleValidFromDisabled, lifecycleValidToDisabled)) {
						builder.batchChangeSetPart1 {
							builder.method('POST')
							builder.uri('Media')
							builder.body {
								builder.Media {
									builder.Media {
										builder.code(asset.@ID)
										builder.catalogVersion_ID(catalogVersions.collect { it.value }.join(','))
										builder.externalObjectTypeCode(asset.@UserTypeID)
										builder.url(getURLValue(asset.Values, asset.@UserTypeID))
										builder.mimeType(getAttributeValue(asset.Values, 'asset.mime-type'))
										builder.filename(getFileNameValue(asset.Values, asset.@UserTypeID))
										builder.fileExtension(getAttributeValue(asset.Values, 'asset.extension'))
										builder.txTerm(asset.Values.ValueGroup.find { it.@AttributeID == 'atr_DM_10025' }?.Value?.getAt(0))
										builder.metaDataAsJson(getAttributeValue(asset.Values, 'atr_csmam_akamai_ratios'))
										builder.publishedFromDate(getDateFromValue(asset, 'atr_publication_valid_from', converter)?.format('yyyy-MM-dd') ?: '1900-01-01')
										builder.publishedUntilDate(getDateFromValue(asset, 'atr_publication_expiry_date', converter)?.format('yyyy-MM-dd') ?: '9999-12-31')
										builder.thumbnailUrl(getAttributeValue(asset.Values, 'atr_videos_thumbnail'))
										builder.texts {
											List<Map> convertedNames = converter.convert(asset.Name.collect().toArray())
											List<Map> convertedDescriptions = getConvertedDescriptions(converter, asset.Values, asset.@UserTypeID)
											List<Map> convertedAlternativeTexts = getConvertedAlternativeTexts(converter, asset.Values)
											validLanguages.each { String currentLanguage ->
												builder.MediaTexts {
													builder.locale(currentLanguage)
													builder.description(convertedDescriptions.find { it.lang == currentLanguage }?.node)
													builder.name(convertedNames.find { it.lang == currentLanguage }?.node)
													builder.originQualifier(convertedNames.find { it.lang == currentLanguage }?.qualifiers)
													builder.alternativeText(convertedAlternativeTexts.find { it.lang == currentLanguage }?.node)
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
		}
	}

	def outputStream = new ByteArrayOutputStream()
	writable.writeTo(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')))
	message.setBody(outputStream)
	return message
}

String getURLValue(values, String type) {
	switch (type) {
		case ['obj_dvs_generic','obj_ProductFactsheet']:
			if (values.ValueGroup.any { it.@AttributeID == 'atr_pim_akamai_link_dvs' }) {
				return values.ValueGroup.find { it.@AttributeID == 'atr_pim_akamai_link_dvs' }.Value[0]?.text()
			} else {
				return values.Value.find { it.@AttributeID == 'atr_pim_akamai_link_dvs' }?.text()
			}
		case 'obj_images':
			return values.Value.find { it.@AttributeID == 'atr_csmam_akamai_link' }?.text()
		case 'obj_videos':
			return values.Value.find { it.@AttributeID == 'atr_videos_akamai_link' }?.text()
	}
}

String getFileNameValue(values, String type) {
	switch (type) {
		case ['obj_dvs_generic','obj_ProductFactsheet']:
			return values.Value.find { it.@AttributeID == 'atr_FILE_NAME' }?.text()
		case 'obj_videos':
			return values.Value.find { it.@AttributeID == 'atr_videos_technical_comment' }?.text()
		default:
			return ''
	}
}

String getAttributeValue(values, String attributeName) {
	return values.Value.find { it.@AttributeID == attributeName }?.text()
}

List<Map> getConvertedDescriptions(LanguageConverter converter, values, String type) {
	def entries
	switch (type) {
		case ['obj_dvs_generic','obj_ProductFactsheet']:
			entries = values.Value.find { it.@AttributeID == 'atr_FILE_DESCRIPTION' }
			break
		case 'obj_images':
			entries = values.Value.find { it.@AttributeID == 'atr_csmam_DKTXT' }
			break
		case 'obj_videos':
			entries = values.Value.find { it.@AttributeID == 'atr_videos_title_original' }
			break
	}
	if (!entries) {
		return converter.convert([].toArray())
	}
	return converter.convert([entries].toArray())
}

List<Map> getConvertedAlternativeTexts(LanguageConverter converter, values) {
	def alternativeTexts = values.ValueGroup.find { it.@AttributeID == 'atr_csmam_EKTXT' }?.findAll { it.name() == 'Value' }
	if (!alternativeTexts) {
		alternativeTexts = values.Value.findAll { it.@AttributeID == 'atr_csmam_EKTXT' }
	}
	if (!alternativeTexts) {
		return converter.convert([].toArray())
	}
	return converter.convert(alternativeTexts.toArray())
}

boolean checkValidForLifecycle(Node asset, Map lifecycleMapping, LanguageConverter converter, boolean validFromDisabled, boolean validToDisabled) {
	def mappingValue = lifecycleMapping[asset.@UserTypeID]
	def dateNow = new Date()
	boolean validToIsOk
	if (mappingValue) {
		//General check
		def value = asset.Values.Value.find { it.@AttributeID == 'atr_lifecycle_asset' && it.@ID == mappingValue }
		if (!value) {
			value = asset.Values.ValueGroup.find { it.@AttributeID == 'atr_lifecycle_asset' }?.Value.find { it.@ID == mappingValue }
		}
		if (!value) {
			return false
		}

		//Valid to
		if (!validToDisabled) {
			def validTo = getDateFromValue(asset, 'atr_publication_expiry_date', converter)
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

Date getDateFromValue(Node asset, String attributeID, LanguageConverter converter) {
	def validDate = asset.Values.Value.find { it.@AttributeID == attributeID }
	if (!validDate) {
		validDate = asset.Values.ValueGroup.find { it.@AttributeID == attributeID }?.Value.find { it.name() == 'Value' }
	}
	if (!validDate) {
		return
	} else {
		def date = new SimpleDateFormat('yyyy-MM-dd').parse(validDate.text())
		return date
	}
}