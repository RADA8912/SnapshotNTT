/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {

	def messageLog = messageLogFactory?.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty('MediaFile', message.getHeader('CamelFileNameOnly', String))
	}
	return message
}