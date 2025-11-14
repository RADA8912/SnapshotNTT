/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder

Message processData(Message message) {
    def catalogBaseCode = message.getHeader('XCatalogBaseCode', String)
    boolean publicationSplit = message.getHeader('XPublicationSplit', Boolean)
    def catalogLanguages = message.getHeader('XCatalogLanguages', String)
    List validLanguages = catalogLanguages.split(',').collect()
    Map catalogCodesAndNames = message.getProperty('CatalogCodes')

    List catalogCodes = (publicationSplit) ? catalogCodesAndNames.collect { it.key } : [catalogBaseCode]

    JsonBuilder builder = new JsonBuilder()
    builder {
        'catalogsLanguages' catalogCodes.collect { catalogCode ->
            [
                    'catalogCode': catalogCode,
                    'languages'  : validLanguages.collect { languageCode ->
                        [
                                'code'   : languageCode,
                                'isoCode': languageCode.replace('_', '-')
                        ]
                    }
            ]
        }
    }

    message.setHeader('Content-Type', 'application/json')
    message.setBody(builder.toString())

    return message
}