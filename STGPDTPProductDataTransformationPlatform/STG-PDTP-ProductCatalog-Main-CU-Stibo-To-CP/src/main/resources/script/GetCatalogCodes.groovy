/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	def catalogBaseCode = message.getHeader('XCatalogBaseCode', String)
	if (catalogBaseCode.contains('_')) {
		message.setHeader('XCountryCode', catalogBaseCode.split('_')[0])
	} else {
		message.setHeader('XCountryCode', catalogBaseCode)
	}

	// Get map of catalog code and catalog name (based on publications)
	def reader = message.getBody(Reader)
	def root = new XmlSlurper().parse(reader)

	if (root.Entities.size()) {
		def publicationEntities = root.Entities.'**'.findAll { it.name() == 'Entity' && it.@UserTypeID.text() == 'objEnt_publication' }
		Map catalogCodes = publicationEntities.collectEntries { entity ->
			String catalogCode = entity.@ID.text().replace('objEnt_pub', catalogBaseCode)
			String catalogName = "${catalogBaseCode} ${entity.Name.text()}"
			[(catalogCode): catalogName]
		}
		message.setProperty('CatalogCodes', catalogCodes)
		message.setHeader('XPublicationSplit', 'true')
	} else {
		// Only single catalog based on base code
		message.setProperty('CatalogCodes', [(catalogBaseCode): ''])
		message.setHeader('XPublicationSplit', 'false')
	}

	return message
}