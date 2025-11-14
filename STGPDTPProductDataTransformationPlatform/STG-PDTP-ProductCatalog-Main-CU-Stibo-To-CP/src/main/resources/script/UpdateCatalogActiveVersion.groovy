/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

Message processData(Message message) {
    def catalogBaseCode = message.getHeader('XCatalogBaseCode', String)
    boolean publicationSplit = message.getHeader('XPublicationSplit', Boolean)
    def catalogVersionCode = message.getHeader('XCatalogVersionCode', String)
    Map catalogCodesAndNames = message.getProperty('CatalogCodes')

    List catalogCodes = (publicationSplit) ? catalogCodesAndNames.collect { it.key } : [catalogBaseCode]
    Map outputBody = [catalogCodes: catalogCodes, activeCatalogVersionCode: catalogVersionCode]

    message.setHeader('Content-Type', 'application/json')
    message.setBody(JsonOutput.toJson(outputBody))

    return message
}