/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

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
		String errorBody = errors[0].body.text()
		Map errorContent = new JsonSlurper().parseText(errorBody)
		message.setProperty('ErrorMessage', "HTTP Response Code: ${errorContent.error.code}, Response Message: ${errorContent.error.message}")
		message.setProperty('ErrorTrace', "HTTP Response Payload - ${errorBody}")
		message.setBody('<ImportStatus>Error</ImportStatus>')
	}
	// Otherwise it is successful
	message.setBody('<ImportStatus>Success</ImportStatus>')
	return message
}