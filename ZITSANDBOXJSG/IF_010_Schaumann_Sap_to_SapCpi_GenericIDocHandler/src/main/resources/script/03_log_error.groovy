import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
    
    def incomingJson = message.getProperties().get('cacheIncomingMsg')
    def messageLog = messageLogFactory.getMessageLog(message)
    if(messageLog != null){
        messageLog.addAttachmentAsString("IncomingIdocCollection.xml", incomingJson, "application/xml")
        def ex = message.getProperties().get('CamelExceptionCaught')
        messageLog.addAttachmentAsString("Error information.txt", ex.toString(), "text/plain")
        messageLog.addAttachmentAsString("CurrentMsgBody.xml", message.getBody(java.lang.String), "application/xml")
    }
    return message
}