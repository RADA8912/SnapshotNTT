package src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message

final class Headers {
	final String activeVersion
	final String sourceCatalogCode
	final String sourceCurrentLang
	final String targetLang
	final String targetCountry
	final String targetPublicationID
	final String sftpRootDir
	final String mediaShapeTypes
	final String mediaShapeMapping

	Headers(Message message) {
		activeVersion = message.getHeader('XActiveVersion', String)
		sourceCatalogCode = message.getHeader('XSourceCatalogCode', String)
		sourceCurrentLang = message.getHeader('XSourceCurrentLanguage', String)
		targetLang = message.getHeader('XTargetLanguage', String)
		targetCountry = message.getHeader('XTargetCountry', String)
		targetPublicationID = message.getHeader('XTargetPublicationID', String)
		sftpRootDir = message.getHeader('XSFTPRootDirectory', String)
		mediaShapeTypes = message.getHeader('XMediaShapeTypes', String)
		mediaShapeMapping = message.getHeader('XMediaShapeMapping', String)

	}

}