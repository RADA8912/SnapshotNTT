/*
 * Copyright (c) 2023 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {

	def root = new JsonSlurper().parse(message.getBody(Reader))
	int recordCount = root.'@count'
	def entity = message.getProperty('BatchEntity')
	if (entity == 'ClassificationClass') {
		message.setProperty('ClassificationClassActualCount', recordCount)
	} else if (entity == 'ClassificationClassProductReference') {
		message.setProperty('ClassificationClassProductRefActualCount', recordCount)
	}
	def messageLog = messageLogFactory?.getMessageLog(message)
	if (messageLog) {
		if (entity == 'ClassificationClass') {
			messageLog.addCustomHeaderProperty('ClassificationClassActualCount', recordCount as String)
		} else if (entity == 'ClassificationClassProductReference') {
			messageLog.addCustomHeaderProperty('ClassificationClassProductRefActualCount', recordCount as String)
		}
	}
	return message
}