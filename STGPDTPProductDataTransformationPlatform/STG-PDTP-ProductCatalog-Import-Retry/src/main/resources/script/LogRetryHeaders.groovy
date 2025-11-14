/*
 * Copyright (c) 2022 Miele & Cie. KG - All rights reserved.
 */

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    Reader reader = message.getBody(Reader)
    def root = new XmlParser().parse(reader)
    // get the list of codes depending on the entity name
    List codes = getCodes(root)
    String firstCode = codes[0].text()

    def messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog) {
        messageLog.addCustomHeaderProperty('BatchEntity', message.getHeader('XBatchEntity', String))
        messageLog.addCustomHeaderProperty('TargetService', message.getHeader('XTargetService', String))
        messageLog.addCustomHeaderProperty('FirstCode', firstCode)
    }
    return message
}

List getCodes(Node root) {
    String entityName = root.batchChangeSet1.batchChangeSetPart1[0].uri.text()
    switch (entityName) {
        case 'ClassificationClassProductReference':
            return root.'**'.findAll { it.name() == 'targetCode' }
        default:
            return root.'**'.findAll { it.name() == 'code' }
    }
}