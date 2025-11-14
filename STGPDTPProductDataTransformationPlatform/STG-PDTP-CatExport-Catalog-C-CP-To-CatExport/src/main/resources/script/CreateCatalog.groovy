/*
* Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
*/


import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import src.main.resources.script.ClassificationClass

Message processData(Message message) {
	new Catalog(message).buildCatalogXML()
	return message
}

class Catalog {
	final Message message
	final rootImmediateChildrenClassCodes = []
	final classCodeToClassMap = [:]
	def rootClassCode = ""
	def langIsoCode = ""

	Catalog(Message message) {
		this.message = message
		rootClassCode = message.getHeader("XClassCommRoot", String)
		langIsoCode = message.getHeader("XSourceCurrentLanguage", String)
	}

	def buildCatalogXML() {
		rebuildStructure()
		def outputStream = new ByteArrayOutputStream()
		def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
		MarkupBuilder builder = new MarkupBuilder(indentPrinter)

		builder.catalog(id: message.getHeader('XTargetPublicationID', String),
				lang: message.getHeader('XTargetLanguage', String),
				country: message.getHeader("XTargetCountry", String)) {
			rootImmediateChildrenClassCodes.each {
				buildArea(builder, classCodeToClassMap.get(it) as ClassificationClass)
			}
		}
		message.setBody(outputStream)
	}

	def rebuildStructure() {
		List jsonClassifications = message.getBody().value
		addClassificationsToMap(jsonClassifications)
		createHierarchyInClassMap(jsonClassifications)
	}

	def addClassificationsToMap(json) {
		json.each { classificationClassJson ->
			final classificationClass = new ClassificationClass(classificationClassJson)
			classCodeToClassMap.put(classificationClassJson.code, classificationClass)

			if (rootClassCode == classificationClassJson.parentCode) {
				rootImmediateChildrenClassCodes << classificationClassJson.code
			}
		}
	}

	def createHierarchyInClassMap(json) {
		json.each { classificationClassJson ->
			final parentCode = classificationClassJson.parentCode as String
			final classificationClass = classCodeToClassMap.get(classificationClassJson.code)

			if (classCodeToClassMap.containsKey(parentCode)) {
				classCodeToClassMap.get(parentCode).children << classificationClass
			}
		}
	}

	def buildArea(builder, ClassificationClass classificationClass) {
		def areaXMLAttributes = [:]
		// Strip prefix and leading zeros from id
		String areaId = classificationClass.json.code.replaceAll(/(\w+_)*0*(\d+)/, '$2')
		areaXMLAttributes.put("id", areaId)
		areaXMLAttributes.put("label", classificationClass.json?.name)
		// TODO - Workaround implemented for MIELEC2P-1576, to be undone in MIELEC2P-1516
		if (classificationClass.json.externalObjectTypeCode.endsWith('_langNeutral')) {
			buildProduct(builder, classificationClass)
			classificationClass.children.each { child -> buildArea(builder, child as ClassificationClass) }
		} else {
			builder.area(areaXMLAttributes) {
				buildProduct(builder, classificationClass)
				classificationClass.children.each { child -> buildArea(builder, child as ClassificationClass) }
			}
		}
	}

	static def buildProduct(builder, ClassificationClass classificationClass) {
		if (classificationClass.json.classificationClassProductReferences) {
			classificationClass.json.classificationClassProductReferences.findAll { it.target }.sort { ref -> ref.sortOrder == null ? 0 : ref.sortOrder }.each { productRef ->
				def productXMLAttributes = [:]
				def mainMatNo = productRef.target?.formattedCode
				if (mainMatNo) {
					productXMLAttributes.put("mainMatNo", mainMatNo)
				}

				def productName = nameFromProduct(productRef)
				if (!productName.isEmpty()) {
					productXMLAttributes.put("label", productName)
				}

				builder.product(productXMLAttributes)
			}
		}
	}

	static nameFromProduct(productRef) {
		try {
			return productRef.target.productFeatures[0].formattedValue
		} catch (ignored) {
			return ""
		}
	}
}