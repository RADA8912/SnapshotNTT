/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	Reader reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	// Check for errors and throw exception if found
	def errors = root.batchChangeSetResponse.batchChangeSetPartResponse.findAll { item -> item.statusCode.text().take(1) != '2' }
	int errorCount = errors.size()
	if (errorCount) {
		// Log request payload into MPL
		InputStream request = message.getProperty('BatchRequestPayload')
		if (request) {
			def messageLog = messageLogFactory?.getMessageLog(message)
			if (messageLog) {
				messageLog.addAttachmentAsString('BatchRequestPayload', request.getText('UTF-8'), 'text/xml')
			}
		}
		// Throw exception
		StringBuilder sb = new StringBuilder()
		errors.eachWithIndex { error, idx ->
			sb << "OData Error ${idx} - ${error.body.text()}\r\n"
		}
		throw new Exception(sb.toString())
	}

	// Otherwise it is successful
	message.setBody('<ImportStatus>Success</ImportStatus>')
	return message
}