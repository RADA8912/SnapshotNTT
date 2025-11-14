/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.util.slurpersupport.GPathResult

Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	def catalogBaseCode = message.getHeader('XCatalogBaseCode', String)

	StringBuilder missingMappings = new StringBuilder()

	// Extract content object prefix
	def contentObjectPrefix = root.ValueMapping.find { it.mappingGroupCode.text() == 'entity.product-or-contentobject-prefix' }.value.text()
	setValueMapping(message, missingMappings, 'XContentObjectPrefix', contentObjectPrefix)

	// Extract catalog languages
	def catalogLanguages = root.ValueMapping.find { it.mappingGroupCode.text() == 'catalog.to.languages' && it.code.text() == catalogBaseCode }.value.text()
	setValueMapping(message, missingMappings, 'XCatalogLanguages', catalogLanguages)

	// Extract language mappings
	def languageMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == "step-qualifier.to.languages.${catalogBaseCode}" }
	setValueMapping(message, missingMappings, 'XLanguageMapping', convertMappingToString(languageMappings))

	// Extract attribute type mapping
	def attributeTypeMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'attribute.types' }
	setValueMapping(message, missingMappings, 'XAttributeTypeMapping', convertMappingToString(attributeTypeMappings))

	//Extract lifecycle mapping
	def lifecycleMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'entity.lifecycle' }
	setValueMapping(message, missingMappings, 'XLifecycleMapping', convertMappingToString(lifecycleMappings))

	//Extract classification system type mapping
	def classSystemTypeRootIdMappings = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'classification-root-id.to.system-type' }
	setValueMapping(message, missingMappings, 'XClassSystemTypeRootIdMapping', convertMappingToString(classSystemTypeRootIdMappings))

	//Extract catalog to classification system type mapping
	def catalogToClassSystemTypeMapping = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'catalog.to.classification-system-type' }
	setValueMapping(message, missingMappings, 'XCatalogToClassSystemTypeMapping', convertMappingToString(catalogToClassSystemTypeMapping))

	//Extract forbidden characters mapping
	def forbiddenCharactersMapping = root.ValueMapping.findAll { it.mappingGroupCode.text() == 'catalog.forbidden.characters' }
	setValueMapping(message, 'XForbiddenCharactersMapping', convertMappingToString(forbiddenCharactersMapping))

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