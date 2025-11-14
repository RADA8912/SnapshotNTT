/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

def Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	def errors = root.batchChangeSetResponse.batchChangeSetPartResponse.findAll { item -> item.statusCode.text() ==~ /^(4|5).*/ }
	int errorCount = errors.size()

	def messageLog = messageLogFactory?.getMessageLog(message)
	if (errorCount) {
		if (messageLog) {
			StringBuilder sb = new StringBuilder()
			errors.eachWithIndex { error, idx ->
				sb << "Error ${idx} - ${error.body.text()}\r\n"
			}
			messageLog.addAttachmentAsString('Errors', sb.toString(), 'text/plain')
		}
		// Set controller status and message
		message.setProperty('ProcessStatus', 'FAILED')
		message.setProperty('ProcessMessage', 'Create CatalogVersion call failed - check MPL logs in CPI')
	} else {
		def catalogBaseCode = message.getHeader('XCatalogBaseCode', String)
		boolean publicationSplit = message.getHeader('XPublicationSplit', Boolean)
		Map catalogCodeToId = message.getProperty('CatalogCodeToId')
		// Build map of publication code (objEnt_pub_website_dom) vs catalog version (UUID)
		Map catalogVersions = root.batchChangeSetResponse.batchChangeSetPartResponse.body.CatalogVersion.CatalogVersion.collectEntries { version ->
			// Also log it into the custom headers
			String catalogCode = catalogCodeToId.find { it.value == version.catalog_ID.text() }?.key
			if (messageLog) {
				messageLog.addCustomHeaderProperty(catalogCode, version.ID.text())
			}
			String publicatonCode = (publicationSplit) ? catalogCode.replace(catalogBaseCode, 'objEnt_pub') : catalogBaseCode
			[(publicatonCode): version.ID.text()]
		}
		message.setHeader('XCatalogVersions', JsonOutput.toJson(catalogVersions))
	}
	return message
}