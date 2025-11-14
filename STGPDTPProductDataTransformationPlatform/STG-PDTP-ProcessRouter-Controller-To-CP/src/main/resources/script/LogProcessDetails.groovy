/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def messageLog = messageLogFactory.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty('ProcessChainCode', message.getHeader('XProcessChainCode', String))
		messageLog.addCustomHeaderProperty('ProcessChainDefinitionCode', message.getHeader('XProcessChainDefinitionCode', String))
		messageLog.addCustomHeaderProperty('ProcessCode', message.getHeader('SAP_ApplicationID', String))
	}
	return message
}