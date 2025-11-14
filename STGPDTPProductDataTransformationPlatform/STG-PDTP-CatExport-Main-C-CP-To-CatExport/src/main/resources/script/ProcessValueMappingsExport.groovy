/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.util.slurpersupport.GPathResult

Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)
	def classificationSystemType = message.getHeader('XSourceCommunicationStructure', String)

	def catExportMappings = root.Message2.ValueMapping.ValueMapping.findAll { it.contextCode.text() == 'catexport.mapping' }
	def inboundMappings = root.Message2.ValueMapping.ValueMapping.findAll { it.contextCode.text() == 'step.inbound.mapping' }

	StringBuilder missingMappings = new StringBuilder()

	// Extract Media Shape Map
	def languageMappings = catExportMappings.findAll { it.mappingGroupCode.text() == 'element.to.shapes' }
	setValueMapping(message, missingMappings, 'XMediaShapeTypes', convertMappingToString(languageMappings))

	// Extract Media Shape Ratios Map
	def mediaShapeRatios = catExportMappings.findAll { it.mappingGroupCode.text() == 'element.to.ratio' }
	setValueMapping(message, missingMappings, 'XMediaShapeRatios', convertMappingToString(mediaShapeRatios))

	// Extract Media Shape to URI Param
	def attributeTypeMappings = catExportMappings.findAll { it.mappingGroupCode.text() == 'image-shape-type.to.url-param' }
	setValueMapping(message, missingMappings, 'XMediaShapeMapping', convertMappingToString(attributeTypeMappings))

	// Extract Media Shape to Ratio URI Param
	def mediaShapeRatioMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'image-shape-type.to.url-param.with.ratios' }
	setValueMapping(message, missingMappings, 'XMediaShapeRatioMapping', convertMappingToString(mediaShapeRatioMapping))

	// Extract Media Document Type Mapping
	def mediaDocTypeMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'media-element.to.media-reference-type' }
	setValueMapping(message, missingMappings, 'XMediaDocumentTypeMapping', convertMappingToString(mediaDocTypeMapping))

	// Extract Media Document Type Translation
	def mediaDocTypeTranslation = catExportMappings.findAll { it.mappingGroupCode.text() == 'localized.media-reference-type.to.name' }
	setValueMapping(message, missingMappings, 'XMediaDocumentTypeTranslation', convertLocalizedMappingToString(mediaDocTypeTranslation))

	// Extract Article References Type Mapping
	def articleTypeMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'matching-material.to.product-reference-type' }
	setValueMapping(message, missingMappings, 'XArticleRefTypeMapping', convertMappingToString(articleTypeMapping))

	// Extract Article Reference Type Translation
	def articleTypeTranslation = catExportMappings.findAll { it.mappingGroupCode.text() == 'localized.article-reference-type.to.name' }
	setValueMapping(message, missingMappings, 'XArticleRefTypeTranslation', convertLocalizedMappingToString(articleTypeTranslation))

	// Extract Classification Type mapping
	def classificationSystemTypeMapping = inboundMappings.findAll { it.mappingGroupCode.text() == 'classification-root-id.to.system-type' }
	setValueMapping(message, missingMappings, 'XClassCommRoot', classificationSystemTypeMapping.find { it.value.text() == classificationSystemType }.code.text())

	//Extract CatExport Shop ID mapping
	def shopIdMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'target.group.to.shoplink.id' }
	setValueMapping(message, missingMappings, 'XShopIdMapping', convertMappingToString(shopIdMapping))

	//Extract CatExport Shop Link mapping
	def shopLinkMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'shop.id.to.shoplink.url' }
	setValueMapping(message, missingMappings, 'XShopLinkMapping', convertLocalizedMappingToString(shopLinkMapping))

	//Extract CatExport Shop Link Is Intern mapping
	def shopLinkIsInternMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'shop.id.to.shoplink.type' }
	setValueMapping(message, missingMappings, 'XShopLinkIsInternMapping', convertMappingToString(shopLinkIsInternMapping))

	//Extract CatExport Currency-ISO to Currency-Symbol
	def displayCurrencyMapping = catExportMappings.findAll { it.mappingGroupCode.text() == 'isocurrency.to.displaycurrency' }
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