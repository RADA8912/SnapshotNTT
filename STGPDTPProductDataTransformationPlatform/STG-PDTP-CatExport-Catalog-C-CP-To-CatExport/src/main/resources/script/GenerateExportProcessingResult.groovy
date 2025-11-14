/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.StreamingMarkupBuilder

def Message processData(Message message) {

	def status = message.getProperty('ProcessingStatus')

	// Define target payload
	Writable writable = new StreamingMarkupBuilder().bind { builder ->
		Export {
			ProcessingStatus(status)
		}
	}

	// Log warnings into the MPL attachment
	def messageLog = messageLogFactory?.getMessageLog(message)
	if (messageLog) {
		List logEntries = message.getProperty('LogEntries')
		if (logEntries?.size()) {
			StringBuilder sb = new StringBuilder()
			logEntries.each {
				sb << "${it}\r\n"
			}
			messageLog.addAttachmentAsString('Warnings', sb.toString(), 'text/plain')
		}
	}

	message.setBody(writable.toString())
	return message
}