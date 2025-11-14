import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
    message.setHeader('SAP_IsIgnoreProperties', true)
    //logs if EnableLogging property is set as true
    String body = message.getBody(String) as String
    def messageLog = messageLogFactory.getMessageLog(message)
    //name of the file
    messageLog.addAttachmentAsString('Payload', body, 'text/plain')

    return message
}
