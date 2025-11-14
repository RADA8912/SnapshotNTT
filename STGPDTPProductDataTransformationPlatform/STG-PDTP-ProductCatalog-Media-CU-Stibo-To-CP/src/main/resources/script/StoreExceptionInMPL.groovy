/*
 * Copyright (c) 2020 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	def messageLog = messageLogFactory?.getMessageLog(message)
	if (messageLog) {
		StringBuilder sb = new StringBuilder()
		sb << "Error Message: ${message.getProperty('ErrorMessage')}\r\n"
		sb << "Error Trace: ${message.getProperty('ErrorTrace')}\r\n"
		messageLog.addAttachmentAsString('Errors', sb.toString(), 'text/plain')
		// Log request payload into MPL
		boolean storePayload = message.getProperty('StorePayloadInMPL')?.toBoolean()
		if (storePayload) {
			InputStream request = message.getProperty('BatchRequestPayload')
			if (request) {
				messageLog.addAttachmentAsString('BatchRequestPayload', request.getText('UTF-8'), 'text/xml')
			}
		}
	}
	return message
}