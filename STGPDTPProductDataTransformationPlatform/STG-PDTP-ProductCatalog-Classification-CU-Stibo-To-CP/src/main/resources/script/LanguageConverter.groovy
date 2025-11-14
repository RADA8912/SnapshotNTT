/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

package src.main.resources.script

import groovy.json.JsonSlurper

class LanguageConverter {
	final String catalogLanguages
	final Map<String, Map> languageMapping

	static LanguageConverter newConverter(String catalogLanguages, String languageMappingEntries) {
		return new LanguageConverter(catalogLanguages, languageMappingEntries)
	}

	private LanguageConverter() {}

	private LanguageConverter(String catalogLanguages, String languageMappingEntries) {
		this.catalogLanguages = catalogLanguages
		// Build a map containing the entries of the static mapping of the languages
		Map<String, String> languageEntries = new JsonSlurper().parseText(languageMappingEntries)
		this.languageMapping = languageEntries.collectEntries { String mapKey, String mapValue ->
			def values = mapValue.split('#')
			int priority = values[0] as int
			List languages = values[1].split(',').collect()
			[(mapKey): ['priority': priority, 'languages': languages]]
		}
	}

	List<Map> convert(Object[] inputNodes) {
		List<Map> entries = inputNodes.collect { node ->
			Map qualifiers = [:]
			if (node.@QualifierID) {
				qualifiers.put('QualifierID', node.@QualifierID)
			}
			if (node.@LOVQualifierID) {
				qualifiers.put('LOVQualifierID', node.@LOVQualifierID)
			}
			if (node.@DerivedContextID) {
				qualifiers.put('DerivedContextID', node.@DerivedContextID)
			}
			['qualifiers': qualifiers, 'value': node]
		}
		return convert(entries).collect { key, map ->
			['lang': key, 'node': inputNodes[map.index], 'qualifiers': map.qualifiers]
		}
	}

	Map<String, Map> convert(List<Map> entries) {
		Map<String, Map> output = [:]
		// Create a map for tracking the current highest priority (lowest number)
		// for each of the language in the catalog
		Map<String, Integer> catalogLangPriorityTracker = this.catalogLanguages.split(',')
				.collectEntries { [(it): null] }

		// For each of the input entries, lookup the mapping to determine the priority and language(s)
		for (int index = 0; index < entries?.size(); index++) {
			Map entry = entries.get(index)
			// If there is no entry for the qualifier value in the static mapping, skip the entry
			boolean qualifierMappingMissing = entry.qualifiers.any { qualifierName, qualifierValue ->
				!languageMapping.containsKey(qualifierValue)
			}
			if (qualifierMappingMissing) {
				continue
			}
			switch (entry.qualifiers.size()) {
				case 0:
					// If there are no qualifiers, then the value is valid for all the
					// languages of the catalog
					catalogLangPriorityTracker.each { language, priority ->
						output.put(language, ['value': entry.value, 'qualifiers': 'No qualifier', 'index': index])
					}
					break
				default:
					String singleLanguage
					Map<String, Integer> entryPriorities = [:]
					// Get the total priority (per relevant language) for the entry, summing up if there are more than
					// on qualifier
					entry.qualifiers.each { qualifierName, qualifierValue ->
						// For each of the qualifier in the entry, find the target mapped language
						// and filter out only those that are in the catalog
						List targetLanguages = languageMapping.get(qualifierValue).languages.findAll {
							catalogLangPriorityTracker.containsKey(it)
						}
						// When there are more than 1 qualifier, and one of them maps to only 1 language,
						// then we will only filter the priority for that language
						if (targetLanguages?.size() == 1) {
							singleLanguage = targetLanguages[0]
						}
						// Sum up each qualifier's priority into the entry's priority
						targetLanguages.each { String language ->
							int qualifierPriority = languageMapping.get(qualifierValue).priority
							if (entryPriorities.containsKey(language)) {
								int existingPriority = entryPriorities.get(language)
								entryPriorities.put(language, existingPriority + qualifierPriority)
							} else {
								entryPriorities.put(language, qualifierPriority)
							}
						}
					}
					if (singleLanguage) {
						entryPriorities = entryPriorities.findAll { it.key == singleLanguage }
					}
					entryPriorities.each { language, entryPriority ->
						// Compare the priority of the entry against the stored priority in the tracker
						def currentHighestPriority = catalogLangPriorityTracker.get(language)
						if (!currentHighestPriority || entryPriority < currentHighestPriority) {
							// If no priority stored yet in the tracker
							// OR the entry priority is higher (lower number) than the stored priority
							// Then update the priority in the tracker and put the entry as the one
							// selected for the particular language
							catalogLangPriorityTracker.put(language as String, entryPriority)
							output.put(language, ['value': entry.value, 'qualifiers': entry.qualifiers.collect().join(' and '), 'index': index])
						}
					}
					break
			}
		}
		return output
	}
}