/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */
package src.main.resources.script

class FeaturesBuilder {

	def build(builder, String nodeName, NodeList inputValues, LanguageConverter converter) {
		// Go through children nodes of Values and build a unique Map filtering out any inherited attributes
		Map uniqueAttributes = [:]
		inputValues[0]?.children()?.each { child ->
			def attributeID = child.@AttributeID
			int inheritedValue = (child.@Inherited ?: 0) as int
			if (!uniqueAttributes.containsKey(attributeID)) {
				uniqueAttributes.put(attributeID, [inherited: inheritedValue, entry: child])
			} else {
				// If there is an existing attribute, select and store the entry with the lowest inherited value 
				Map existingAttribute = uniqueAttributes.get(attributeID)
				if (inheritedValue < existingAttribute.inherited) {
					existingAttribute.inherited = inheritedValue
					existingAttribute.entry = child
				}
			}
		}
		// Process through the map in sequential order, such that the sortOrder follows the original sequence
		uniqueAttributes.eachWithIndex { attribute, index ->
			switch (attribute.value.entry.name()) {
				case 'Value': // */Values/Value
					List<Map> output = converter.convert([attribute.value.entry].toArray())
					output.each {
						buildValue(builder, nodeName, it.node, attribute.key, '1', it.lang, it.qualifiers, index + 1)
					}
					break
				case 'ValueGroup': // */Values/ValueGroup
					List<Map> output = converter.convert(attribute.value.entry.Value.collect().toArray())
					output.each {
						buildValue(builder, nodeName, it.node, attribute.key, '1', it.lang, it.qualifiers, index + 1)
					}
					break
				case 'MultiValue': // */Values/MultiValue
					def languagesCount = [:]
					List<Map> output = []
					// Iterate through children as ValueGroup and Value may not be ordered
					attribute.value.entry.children().each { multiChild ->
						switch (multiChild.name()) {
							case 'Value': // */Values/MultiValue/Value
								output.addAll(converter.convert([multiChild].toArray()))
								break
							case 'ValueGroup': // */Values/MultiValue/ValueGroup
								output.addAll(converter.convert(multiChild.Value.collect().toArray()))
								break
						}
					}
					output.each {
						int currentLangCount = (languagesCount.get(it.lang) ?: 0) + 1
						languagesCount.put(it.lang, currentLangCount)
						buildValue(builder, nodeName, it.node, attribute.key, currentLangCount, it.lang, it.qualifiers, index + 1)
					}
					break
			}
		}
	}

	def buildValue(builder, String nodeName, inputValue, attrCode, multiCode, lang, origin, int count) {
		if(inputValue?.text()?.trim()) {
			builder."${nodeName}" {
				attributeCode(attrCode)
				attributeUnitCode(inputValue.@UnitID ?: null)
				attributeValueCode(inputValue.@ID ?: null)
				multiValuePosition(multiCode)
				languageCode(lang)
				originQualifier(origin)
				sortOrder(count)
				value(inputValue)
			}
		}
	}
}