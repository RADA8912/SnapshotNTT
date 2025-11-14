/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	message.setHeader('SAP_ApplicationID', message.getHeader('XProcessCode', String))

	def messageLog = messageLogFactory.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty('ProcessChainCode', message.getHeader('XProcessChainCode', String))
		messageLog.addCustomHeaderProperty('ProcessChainDefinitionCode', message.getHeader('XProcessChainDefinitionCode', String))
		messageLog.addCustomHeaderProperty('ProcessCode', message.getHeader('XProcessCode', String))
		messageLog.addCustomHeaderProperty('TargetPublicationID', message.getHeader('XTargetPublicationID', String))
		messageLog.addCustomHeaderProperty('TargetCountry', message.getHeader('XTargetCountry', String))
		messageLog.addCustomHeaderProperty('TargetLanguage', message.getHeader('XTargetLanguage', String))
		messageLog.addCustomHeaderProperty('SourcePriceType', message.getHeader('XSourcePriceType', String))
		messageLog.addCustomHeaderProperty('SourcePriceCurrency', message.getHeader('XSourcePriceCurrency', String))
		messageLog.addCustomHeaderProperty('CatalogCode', message.getHeader('XSourceCatalogCode', String))
		messageLog.addCustomHeaderProperty('CatalogLanguage', message.getHeader('XSourceCurrentLanguage', String))
		messageLog.addCustomHeaderProperty('TargetGroup', message.getHeader('XTargetGroup', String))
		messageLog.addCustomHeaderProperty('CatalogVersion', message.getHeader('XActiveVersion', String))
		messageLog.addCustomHeaderProperty('CatalogVersionCode', message.getHeader('XCatalogVersionCode', String))
		messageLog.addCustomHeaderProperty('RemoteTargetFolder', message.getHeader('XSFTPRootDirectory', String))
		messageLog.addCustomHeaderProperty('DelayForProductFlow', message.getProperty('DelayForProductFlow'))
		messageLog.addCustomHeaderProperty('IncludeAvailability', message.getHeader('XIncludeAvailability', String))
		messageLog.addCustomHeaderProperty('SourceCommunicationStructure', message.getHeader('XSourceCommunicationStructure', String))
		messageLog.addCustomHeaderProperty('PublicationScope', message.getHeader('XPublicationScope', String))
	}
	return message
}