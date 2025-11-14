import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import src.main.resources.script.ClassificationClass
import src.main.resources.script.ContentObjectBuilder
import src.main.resources.script.Headers
import src.main.resources.script.MappingLogger

/*

* Copyright (c) 2021 Miele & Cie. KG - All rights reserved.

*/

Message processData(Message message) {
	List jsonClassifications = message.getBody().value

	Map classificationReferenceMap = [:]
	List legendClasses = []
	MappingLogger logger = new MappingLogger(message)

	jsonClassifications.each { jsonClassification ->
		def classification = new ClassificationClass(jsonClassification)
		classificationReferenceMap.put(jsonClassification.code, classification)

		if (jsonClassification.externalObjectTypeCode == 'obj_legend_classification') {
			legendClasses << classification
		}
	}

	// For the root legend classes (that are without parentCode), drill down the tree to recursively populate the children
	legendClasses.findAll { !it.json.parentCode }.each { legendClass ->
		populateChildrenOfGivenTree(legendClass.json.code, jsonClassifications, classificationReferenceMap)
	}

	def outputStream = new ByteArrayOutputStream()
	def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
	MarkupBuilder builder = new MarkupBuilder(indentPrinter)

	ContentObjectBuilder contentObjectBuilder = new ContentObjectBuilder(builder, jsonClassifications*.classificationClassContentObjectReferences, message, logger)
	Taxonomy taxonomy = new Taxonomy(new Headers(message), builder, legendClasses, contentObjectBuilder, message, logger)
	builder.taxonomies {
		legendClasses.each { legendClass ->
			taxonomy.buildXML(legendClass)
		}
	}

	message.setBody(outputStream)
	if (logger.getEntries().size()) {
		message.setProperty('LogEntries', logger.getEntries())
	}
	return message
}

void populateChildrenOfGivenTree(String parentCode, List jsonClassifications, Map classificationReferenceMap) {
	def children = jsonClassifications.findAll { it.parentCode == parentCode }
	children.each { child ->
		def childMap = classificationReferenceMap.get(child.code)
		def parentMap = classificationReferenceMap.get(parentCode)
		if (parentMap) {
			parentMap.children << childMap
			populateChildrenOfGivenTree(child.code, jsonClassifications, classificationReferenceMap)
		}
	}
}


class Taxonomy {
	final Headers headers
	final Message message
	final MarkupBuilder builder
	final ContentObjectBuilder coBuilder
	final List coReferences
	final MappingLogger logger
	final List legendClasses

	Taxonomy(Headers headers, MarkupBuilder builder, List legendClasses, ContentObjectBuilder coBuilder, message, MappingLogger logger) {
		this.headers = headers
		this.builder = builder
		this.coBuilder = coBuilder
		this.message = message
		this.logger = logger
		this.legendClasses = legendClasses
		this.coReferences = []
	}

	void buildXML(ClassificationClass classificationClass) {
		if (classificationClass.json.classificationClassProductReferences?.size()) {
			logger.info("--- Begin processing taxonomy class ${classificationClass.json.code} ---")
			builder.taxonomy(['id': classificationClass.json.code.replace('cls_', '')]) {
				classificationClass.children.sort { it.json.sortOrder }.each {
					buildGroup(it)
				}
				if (coReferences) {
					// Pass list of all the CO formatted codes that were referenced in the rows (of classification class)
					coBuilder.buildReference([:], [], this.coReferences, 'Tax')
				}
			}
            coReferences.clear()
			coBuilder.resetInlineReferences()
		}
	}

	void buildGroup(classificationClass) {
		if (classificationClass.json.externalObjectTypeCode != "obj_legend_group") {
			return
		}

		Map groupElementAttributes = ["gID": classificationClass.json.code]

		def localizedName = classificationClass.json?.name
		if (localizedName) {
			groupElementAttributes << ["label": localizedName]
		} else {
			groupElementAttributes << ["label": ""]
			logNoTextFound(classificationClass)
		}
		builder.group(groupElementAttributes) {
			classificationClass.children.sort { it.json.sortOrder }.each { buildRow(it) }
		}
	}

	void buildRow(classificationClass) {
		Map rowElementAttributes = [:]
		def refId = getReferenceIDs(classificationClass)
		if (refId) {
			rowElementAttributes << ["refID": refId]
		}
		def localizedName = classificationClass.json?.name

		if (localizedName) {
			rowElementAttributes << ["col1": localizedName]
		} else {
			rowElementAttributes << ["col1": ""]
			logNoTextFound(classificationClass)
		}

		builder.row(rowElementAttributes) {
			List attributeReferences = classificationClass.json.classificationClassAttributeAssignments as List<?>
			attributeReferences.unique(false) { a, b -> a.code == b.code ? 0 : 1 }.each { buildEntryDef(classificationClass.json.code) }
		}
	}

	void logNoTextFound(classificationClass) {
		logger.log("[WARNING] No localized name found for classification class with ID: ${classificationClass.json.code}")
	}

	void buildEntryDef(code) {
		builder.entryDef(["name": code]) {}
	}

	private String getReferenceIDs(ClassificationClass classificationClass) {
		def attr
		classificationClass.json.classificationClassContentObjectReferences.find { ref ->
			attr = ref.classificationClassContentObjectReferenceMetaData.find { meta ->
				meta.attributeCode == 'atr_legend_content_object_position'
			}?.attributeValueCode
		}
		if(attr == '1') {
			List refIDs = classificationClass.json.classificationClassContentObjectReferences.collect { it.target?.formattedCode }
			this.coReferences.addAll(refIDs)
			return refIDs.join(',')
		} else {
			return ''
		}
	}
}