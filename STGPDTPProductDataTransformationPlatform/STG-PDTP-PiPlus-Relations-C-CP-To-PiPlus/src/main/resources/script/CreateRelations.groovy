/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import src.main.resources.script.MappingLogger

def Message processData(Message message) {
	Map root = message.getBody()
	Map classTree = message.getProperty('ClassTreePayload')
	Map articleTypeMapping = new JsonSlurper().parseText(message.getProperty('XPiPlusArticleRefTypeMapping'))

	def targetPublicationID = message.getHeader('XTargetPublicationID', String)
	def targetLanguage = message.getHeader('XTargetLanguage', String)
	def targetCountry = message.getHeader('XTargetCountry', String)
	def exportDate = message.getHeader('XExportDate', String)

	MappingLogger logger = new MappingLogger(message)

	def outputStream = new ByteArrayOutputStream()
	def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
	MarkupBuilder builder = new MarkupBuilder(indentPrinter)

	builder.MATERIAL_RELATIONS(country: targetCountry, language: targetLanguage, date: exportDate, version: '3.1', catalog: targetPublicationID) {
		root.value.findAll { it.target }.each { productRef ->
			MATERIALS {
				SOURCE_MATERIAL(productRef.source.formattedCode)
				SOURCE_PRODUCTGROUP(getClassForArticle(classTree.value, productRef, 'source', logger)?.name)
				RELATION_TYPE(articleTypeMapping.get(productRef.referenceTypeCode))
				TARGET_MATERIAL(productRef.target?.formattedCode)
				TARGET_PRODUCTGROUP(getClassForArticle(classTree.value, productRef, 'target', logger)?.name)
			}
		}
	}

	message.setBody(outputStream)

	if (logger.getEntries().size()) {
		message.setProperty('LogEntries', logger.getEntries())
	}
	return message
}

Map getClassForArticle(List references, Map refNode, String nodeName, MappingLogger logger) {
	if (!refNode.get(nodeName)) {
		logger.log("[WARNING] No ${nodeName} node found for targetCode = ${refNode.targetCode}, type = ${refNode.referenceTypeCode} ")
		return [:]
	} else {
		def classProdRef = references.find { it.targetCode == refNode.get(nodeName).code }
		if (classProdRef) {
			return classProdRef.source
		} else {
			logger.log("[WARNING] No classification class reference of type ref_PIM_PI17_link found for article ${refNode.get(nodeName).code}")
			return [:]
		}
	}
}

String getRelationType(Map articleTypeMapping, String refCode, MappingLogger logger) {
	def relationType = articleTypeMapping.get(refCode)
	if (relationType) {
		return relationType
	} else {
		logger.log("[WARNING] No Article References Type Mapping found for ${refCode}")
		return ''
	}
}