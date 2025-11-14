import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    def headers = message.getHeaders() as String;
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        messageLog.setStringProperty("Logging 1", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString("Input:", body, "text/plain");
        messageLog.addAttachmentAsString("Headers:", headers, "text/plain");
     }
    return message;
}