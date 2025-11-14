/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder

Message processData(Message message) {
	// Process response from Catalog query
	def reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	def catalogBaseCode = message.getHeader('XCatalogBaseCode', String)
	def ctryCode = message.getHeader('XCountryCode', String)
	boolean publicationSplit = message.getHeader('XPublicationSplit', Boolean)
	boolean importMasterCatalog = message.getHeader('XImportMasterCatalog', Boolean)

	// Get the catalog codes listed in the input file (stored as a Map)
	Map catalogCodes = message.getProperty('CatalogCodes')

	//Assign catalog_ID to catalogCode
	Map catalogCodeToId = [:]

	//Add master catalog, catalog codes are derived from STEP payload, so master won't be there
	if (importMasterCatalog) {
		catalogCodes.put("${ctryCode}_master".toString(), "${ctryCode} Master Catalog".toString())
	}

	Map newCatalogCodes = [:]
	catalogCodes.each { catalogCode, catalogName ->
		if (!root.Catalog.any { it.code.text() == catalogCode }) {
			newCatalogCodes.put(catalogCode, catalogName)
		}
		def id = root.Catalog.find { it.code.text() == catalogCode }?.ID.text()
		catalogCodeToId.put(catalogCode, id)
	}
	if (newCatalogCodes.size()) {
		message.setProperty('CreateNewCatalogs', 'true')
		Writable writable = new StreamingMarkupBuilder().bind { builder ->
			batchParts {
				batchChangeSet1 {
					newCatalogCodes.each { catalogCode, catalogName ->
						batchChangeSetPart1 {
							method('POST')
							uri('Catalog')
							body {
								Catalog {
									Catalog {
										code(catalogCode)
										countryCode(ctryCode)
										name((publicationSplit) ? catalogName : catalogBaseCode)
										publicationCode((publicationSplit) ? catalogCode.replace(catalogBaseCode, 'objEnt_pub') : '')
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
	} else {
		message.setProperty('CreateNewCatalogs', 'false')
	}
	message.setProperty('CatalogCodeToId', catalogCodeToId)

	return message
}