/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def messageLog = messageLogFactory.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty('ProcessCode', message.getHeader('XProcessCode', String))
		messageLog.addCustomHeaderProperty('STEPImportFile', message.getHeader('XImportFile', String))
		messageLog.addCustomHeaderProperty('STEPImportBaseDirectory', message.getHeader('XImportBaseDirectory', String))
		messageLog.addCustomHeaderProperty('STEPImportCountry', message.getHeader('XImportCountry', String))
		messageLog.addCustomHeaderProperty('STEPImportSourceDirectory', message.getHeader('XImportSourceDirectory', String))
		messageLog.addCustomHeaderProperty('STEPImportMasterCatalog', message.getHeader('XImportMasterCatalog', String))
		messageLog.addCustomHeaderProperty('STEPTriggerFile', message.getHeader('XSFTPFilePattern', String))
	}
	return message
}