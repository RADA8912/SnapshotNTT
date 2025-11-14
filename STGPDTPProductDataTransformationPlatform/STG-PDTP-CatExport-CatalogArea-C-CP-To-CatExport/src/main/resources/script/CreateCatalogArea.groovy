/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder
import src.main.resources.script.ContentObjectBuilder
import src.main.resources.script.MappingLogger

def Message processData(Message message) {
	Map inputRoot = message.getBody()

	def catalogID = message.getHeader('XTargetPublicationID', String)
	def catalogLanguage = message.getHeader('XTargetLanguage', String)
	def catalogCountry = message.getHeader('XTargetCountry', String)
	Map filterClasses = message.getProperty('filterClasses')

	def catalogTreeBuilder = new CatalogTreeBuilder(inputRoot, catalogID, catalogLanguage, catalogCountry)
	def communicationRootCode = message.getHeader('XClassCommRoot', String)
	def childClasses = inputRoot.value.findAll { it.parentCode == communicationRootCode }
	List communicationClasses = catalogTreeBuilder.getClassListFromParentCode(childClasses)

	MappingLogger logger = new MappingLogger(message)

	def outputStream = new ByteArrayOutputStream()
	def indentPrinter = new IndentPrinter(new BufferedWriter(new OutputStreamWriter(outputStream, 'UTF-8')), '', false)
	MarkupBuilder builder = new MarkupBuilder(indentPrinter)

	// TODO - Workaround implemented for MIELEC2P-1576, to be undone in MIELEC2P-1516
	List reducedClasses = communicationClasses.findAll { !it.externalObjectTypeCode.endsWith('_langNeutral') }

	List coReferences = communicationClasses*.classificationClassContentObjectReferences + communicationClasses*.classificationClassProductReferences*.target*.productContentObjectReferences
    ContentObjectBuilder contentObjectBuilder = new ContentObjectBuilder(builder, coReferences, message, logger)
	// Define target payload
	builder.Areas {
		reducedClasses.each { Map classificationClass ->
			logger.info("--- Begin processing classification ${classificationClass.code} ---")
			builder.areaDetail(id: CatalogTreeBuilder.convertAreaId(classificationClass.code), label: classificationClass.name) {
				List allProductReferences = getAllProductReferences(classificationClass, inputRoot)
				// Benefits
				contentObjectBuilder.buildBenefits(allProductReferences, 'CatalogArea-ProductBenefit', filterClasses.value)

				// Topics
				List classCORefs = classificationClass.classificationClassContentObjectReferences
				List specialRefs = contentObjectBuilder.getContentObjectRefsByTypeCode(classCORefs, 'special_topic')
				List branchThemeRefs = contentObjectBuilder.getContentObjectRefsByTypeCode(classCORefs, 'sector_theme')
				List refThemeRefs = contentObjectBuilder.getContentObjectRefsByTypeCode(classCORefs, 'reference')
				if (specialRefs || branchThemeRefs || refThemeRefs) {
					builder.topics {
						contentObjectBuilder.buildContentObjectReferences('io_SPECIAL', specialRefs, 'CatalogArea-Special')
						contentObjectBuilder.buildContentObjectReferences('io_BRANCHTHEME', branchThemeRefs, 'CatalogArea-Branchtheme')
						contentObjectBuilder.buildContentObjectReferences('io_REFTHEME', refThemeRefs, 'CatalogArea-Reftheme')
					}
				}
				// Reference
				contentObjectBuilder.buildReference()

				// Catalog
				catalogTreeBuilder.build(builder, classificationClass, communicationRootCode)
				//filter
				contentObjectBuilder.mainFilters.toUnique().each { mainFilter ->
					builder.filter(name:mainFilter)
				}
				contentObjectBuilder.mainFilters?.clear()
			}
			contentObjectBuilder.resetInlineReferences()
		}
	}

	message.setBody(outputStream)
	// Free up references to payloads
	message.setProperty('filterClasses', '')

	if (logger.getEntries().size()) {
		message.setProperty('LogEntries', logger.getEntries())
	}
	return message
}

List getAllProductReferences(Map classificationClass, Map root) {
	def references = []
	if (classificationClass.classificationClassProductReferences.size()) {
		references.addAll(classificationClass.classificationClassProductReferences)
	}
	root.value.findAll { it.parentCode == classificationClass.code }.each { Map childClass ->
		List childPR = getAllProductReferences(childClass, root)
		if (childPR.size()) {
			references.addAll(childPR)
		}
	}
	return references
}

class CatalogTreeBuilder {
	final List allClasses
	final String catalogID
	final String catalogLanguage
	final String catalogCountry

	CatalogTreeBuilder(Map root, String catalogID, String catalogLanguage, String catalogCountry) {
		this.allClasses = root.value
		this.catalogID = catalogID
		this.catalogLanguage = catalogLanguage
		this.catalogCountry = catalogCountry
	}

	void build(Object builder, Map currentClass, String rootClassCode) {
		Stack catalogTree = []
		getCatalogTree(currentClass, catalogTree, rootClassCode)
		// Root catalog node
		builder.catalog(id: catalogID, lang: catalogLanguage, country: catalogCountry) {
			// Recursively build the area and all its descendants
			buildCatalogArea(builder, catalogTree)
		}
	}

	List getClassListFromParentCode(List classes) {
		def outputClasses = []
		classes.each { currentClass ->
			outputClasses.add(currentClass)
			def childClasses = allClasses.findAll { it.parentCode == currentClass.code }
			outputClasses.addAll(getClassListFromParentCode(childClasses))
		}
		return outputClasses
	}

	private void getCatalogTree(Map classificationClass, Stack tree, String rootClassCode) {
		// Recursively find the ancestors of the current class up to the class root and store it in a LIFO stack
		if (classificationClass.code != rootClassCode) {
			// TODO - Workaround implemented for MIELEC2P-1576, to be undone in MIELEC2P-1516
			tree.push([id: classificationClass.code, name: classificationClass.name, externalObjectTypeCode: classificationClass.externalObjectTypeCode])
			Map parentClass = allClasses.find { it.code == classificationClass.parentCode }
			if (parentClass) {
				getCatalogTree(parentClass, tree, rootClassCode)
			}
		}
	}

	private void buildCatalogArea(Object builder, Stack catalogTree) {
		// Build the area by taking the entries out one at a time from the LIFO stack
		def currentClass = catalogTree.pop()
		// TODO - Workaround implemented for MIELEC2P-1576, to be undone in MIELEC2P-1516
		if (currentClass.externalObjectTypeCode.endsWith('_langNeutral')) {
			// If there are still entries in the stack, then create the next level node,
			// Otherwise, it is a leaf node, then create the child areas if any
			if (catalogTree.size()) {
				buildCatalogArea(builder, catalogTree)
			} else {
				allClasses.findAll { it.parentCode == currentClass.id && !it.externalObjectTypeCode.endsWith('_langNeutral') }.each { Map childClass ->
					areaChild(id: convertAreaId(childClass.code), label: childClass.name)
				}
			}
		} else {
			builder.area(id: convertAreaId(currentClass.id), label: currentClass.name) {
				// If there are still entries in the stack, then create the next level node,
				// Otherwise, it is a leaf node, then create the child areas if any
				if (catalogTree.size()) {
					buildCatalogArea(builder, catalogTree)
				} else {
					// TODO - Workaround implemented for MIELEC2P-1576, to be undone in MIELEC2P-1516
					allClasses.findAll { it.parentCode == currentClass.id && !it.externalObjectTypeCode.endsWith('_langNeutral') }.each { Map childClass ->
						areaChild(id: convertAreaId(childClass.code), label: childClass.name)
					}
				}
			}
		}
	}

	static String convertAreaId(String id) {
		// Strip prefix and leading zeros from id
		return id.replaceAll(/(\w+_)*0*(\d+)/, '$2')
	}
}