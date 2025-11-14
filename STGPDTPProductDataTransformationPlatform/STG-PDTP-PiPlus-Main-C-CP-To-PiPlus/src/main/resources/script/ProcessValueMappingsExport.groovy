/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.util.slurpersupport.GPathResult

Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	StringBuilder missingMappings = new StringBuilder()

	// Extract Article References Type
	def languageMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'product-reference.to.matching-material.type' }
	setValueMapping(message, missingMappings, 'XPiPlusArticleRefTypeMapping', convertMappingToString(languageMappings))

	// Extract Unit Mapping
	def unitMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'pim.to.piplus-unit' }
	setValueMapping(message, missingMappings, 'XUnitMapping', convertMappingToString(unitMappings))

	// Extract PiPlu Media Document Type Mapping
	def mediaDocTypeMapping = root.ValueMapping.findAll { it.contextCode.text() == 'piplus.mapping' && it.mappingGroupCode.text() == 'media-element.to.media-reference-type'}
	setValueMapping(message, missingMappings, 'XMediaDocumentTypeMapping', convertMappingToString(mediaDocTypeMapping))

	// Extract Classification Product Reference Type mapping
	def classificationProdRefMapping = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'classificationProductReference.referenceTypeCode' }
	setValueMapping(message, missingMappings, 'XClassificationProductRefTypeMapping', convertMappingToString(classificationProdRefMapping))

	// Extract Media Shape Map
	def mediaShapeTypes = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'element.to.shapes' }
	setValueMapping(message, missingMappings, 'XMediaShapeTypes', convertMappingToString(mediaShapeTypes))

	// Extract Media Shape to URI Param
	def attributeTypeMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'image-shape-type.to.url-param' }
	setValueMapping(message, missingMappings, 'XMediaShapeMapping', convertMappingToString(attributeTypeMappings))

	// Extract Media Document Type Translation
	def mediaDocTypeTranslation = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'localized.media-reference-type.to.name' }
	setValueMapping(message, missingMappings, 'XMediaDocumentTypeTranslation', convertLocalizedMappingToString(mediaDocTypeTranslation))

	// Extract Article References Type Mapping
	def articleTypeMapping = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'matching-material.to.product-reference-type' }
	setValueMapping(message, missingMappings, 'XArticleRefTypeMapping', convertMappingToString(articleTypeMapping))

	// Extract Article Reference Type Translation
	def articleTypeTranslation = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'localized.article-reference-type.to.name' }
	setValueMapping(message, missingMappings, 'XArticleRefTypeTranslation', convertLocalizedMappingToString(articleTypeTranslation))

	//Extract CatExport Currency-ISO to Currency-Symbol
	def displayCurrencyMapping = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'isocurrency.to.displaycurrency' }
	setValueMapping(message, 'XDisplayCurrencyMapping', convertMappingToString(displayCurrencyMapping))

	if (missingMappings) {
		message.setProperty('ValueMappingError', 'true')
		def messageLog = messageLogFactory?.getMessageLog(message)
		if (messageLog) {
			messageLog.addAttachmentAsString('Errors', missingMappings.toString(), 'text/plain')
		}
	}

	return message
}

void setValueMapping(Message message, StringBuilder sb, String headerName, String value) {
	if (value) {
		message.setHeader(headerName, value)
	} else {
		sb << "Missing value mapping for header ${headerName}\r\n"
	}
}

void setValueMapping(Message message, String headerName, String value) {
	if (value) {
		message.setHeader(headerName, value)
	}
}

String convertMappingToString(GPathResult node) {
	Map mappingEntries = node.collectEntries {
		[(it.code.text()): it.value.text()]
	}
	return (mappingEntries.size()) ? JsonOutput.toJson(mappingEntries) : ''
}

String convertLocalizedMappingToString(GPathResult node) {
	Map mappingEntries = node.collectEntries {
		[(it.code.text()): it.texts.ValueMapping_texts.value.text()]
	}
	return (mappingEntries.size()) ? JsonOutput.toJson(mappingEntries) : ''
}