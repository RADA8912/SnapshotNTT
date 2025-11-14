/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	int loopIndex = message.getProperty('CamelLoopIndex') as int
	String currentFileProcessedCount = (loopIndex + 1) as String
	message.setProperty('CurrentProcessedCount', currentFileProcessedCount)
	def messageLog = messageLogFactory?.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty("ProductFile${currentFileProcessedCount.padLeft(3, '0')}", message.getHeader('CamelFileNameOnly', String))
	}
	return message
}