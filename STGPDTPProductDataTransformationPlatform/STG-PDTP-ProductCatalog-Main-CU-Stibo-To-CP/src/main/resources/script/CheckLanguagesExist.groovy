/*
 * Copyright (c) 2021 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {

    def reader = message.getBody(Reader)
    def root = new XmlSlurper().parse(reader)

    String catalogLanguages = message.getHeader('XCatalogLanguages', String)

    List<String> validLanguages = catalogLanguages.split(',').collect()

    Map<String,String> languageToId = [:]

    List<String> missingLanguages = []

    validLanguages.each { language ->
        String id = root.Language.find { it.code.text() == language }?.ID?.text()
        id ? languageToId.put(language, id) : missingLanguages.push(language)
    }

    missingLanguages ? setError(missingLanguages, message) : message.setProperty('LanguageToId', languageToId)

    return message
}

void setError(List<String> missingLanguages, Message message) {
    message.setProperty('MissingLanguages', 'true')
    def messageLog = messageLogFactory?.getMessageLog(message)
    if (messageLog) {
        messageLog.addAttachmentAsString('Errors', "Missing languages: ${missingLanguages.join(',')}", 'text/plain')
    }
}
