import com.sap.gateway.ip.core.customdev.util.Message

Message logPayloadInput(Message message) {
    map = message.properties
    property_EnableLogging = map.get('EnableLogging')
    message.setHeader('SAP_IsIgnoreProperties', true)
    //logs if EnableLogging property is set as true
    if (property_EnableLogging.toUpperCase() == 'TRUE') {
        String body = message.getBody(String) as String
        def messageLog = messageLogFactory.getMessageLog(message)
        //name of the file
        messageLog.addAttachmentAsString('Payload Input', body, 'text/plain')
    }

    return message
}

Message logPayloadOutput(Message message) {
    map = message.properties
    property_EnableLogging = map.get('EnableLogging')
    message.setHeader('SAP_IsIgnoreProperties', true)
    //logs if EnableLogging property is set as true
    if (property_EnableLogging.toUpperCase() == 'TRUE') {
        String body = message.getBody(String) as String
        def messageLog = messageLogFactory.getMessageLog(message)
        //name of the file
        messageLog.addAttachmentAsString('Payload Output', body, 'text/plain')
    }

    return message
}
