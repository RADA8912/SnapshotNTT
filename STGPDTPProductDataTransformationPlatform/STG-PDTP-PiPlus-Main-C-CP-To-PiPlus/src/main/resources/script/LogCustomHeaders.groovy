/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	message.setHeader('SAP_ApplicationID', message.getHeader('XProcessCode', String))

	def messageLog = messageLogFactory.getMessageLog(message)
	if (messageLog) {
		messageLog.addCustomHeaderProperty('CatalogCode', message.getHeader('XSourceCatalogCode', String))
		messageLog.addCustomHeaderProperty('CatalogLanguage', message.getHeader('XSourceCurrentLanguage', String))
		messageLog.addCustomHeaderProperty('TargetGroup', message.getHeader('XTargetGroup', String))
		messageLog.addCustomHeaderProperty('TargetCountry', message.getHeader('XTargetCountry', String))
		messageLog.addCustomHeaderProperty('TargetPublicationID', message.getHeader('XTargetPublicationID', String))
		messageLog.addCustomHeaderProperty('TargetLanguage', message.getHeader('XTargetLanguage', String))
		messageLog.addCustomHeaderProperty('RemoteTargetFolder', message.getHeader('XSFTPRootDirectory', String))
		messageLog.addCustomHeaderProperty('SourcePiDataVersion', message.getHeader('XSourcePiDataVersion', String))
		messageLog.addCustomHeaderProperty('SourcePiDataOrganization', message.getHeader('XSourcePiDataOrganization', String))
		messageLog.addCustomHeaderProperty('ProcessChainCode', message.getHeader('XProcessChainCode', String))
		messageLog.addCustomHeaderProperty('ProcessChainDefinitionCode', message.getHeader('XProcessChainDefinitionCode', String))
		messageLog.addCustomHeaderProperty('ProcessCode', message.getHeader('XProcessCode', String))
		messageLog.addCustomHeaderProperty('SourcePriceType', message.getHeader('XSourcePriceType', String))
		messageLog.addCustomHeaderProperty('SourcePriceCurrency', message.getHeader('XSourcePriceCurrency', String))
		messageLog.addCustomHeaderProperty('TargetFilenamePrefix', message.getHeader('XTargetFilenamePrefix', String))
		messageLog.addCustomHeaderProperty('IncludeFilter', message.getHeader('XIncludeFilter', String))
		messageLog.addCustomHeaderProperty('DelayForFilterFlow', message.getProperty('DelayForFilterFlow'))
		messageLog.addCustomHeaderProperty('SourceCommunicationStructure', message.getHeader('XSourceCommunicationStructure', String))
	}
	return message
}