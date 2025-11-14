/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	Map<String,String> catalogCodeToId = message.getProperty('CatalogCodeToId') ?: [:]

	root.batchChangeSetResponse.batchChangeSetPartResponse.each { response ->
		String code = response.body.Catalog.Catalog.code.text()
		String id = response.body.Catalog.Catalog.ID.text()
		catalogCodeToId.put(code,id)
	}

	message.setProperty('CatalogCodeToId',catalogCodeToId)

	def errors = root.batchChangeSetResponse.batchChangeSetPartResponse.findAll { item -> item.statusCode.text() ==~ /^(4|5).*/ }

	int errorCount = errors.size()

	if (errorCount) {
		def messageLog = messageLogFactory?.getMessageLog(message)
		if (messageLog) {
			StringBuilder sb = new StringBuilder()
			errors.eachWithIndex { error, idx ->
				sb << "Error ${idx} - ${error.body.text()}\r\n"
			}
			messageLog.addAttachmentAsString('Errors', sb.toString(), 'text/plain')
		}
		// Set controller status and message
		message.setProperty('ProcessStatus', 'FAILED')
		message.setProperty('ProcessMessage', 'Create Catalog call failed - check MPL logs in CPI')
	}

	return message
}