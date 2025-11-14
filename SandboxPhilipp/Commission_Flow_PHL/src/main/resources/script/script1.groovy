import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message){
    
    def body = message.getBody(java.lang.String) as String;
    def messageLog = messageFactory.getMessageLog(message);
        messageLog.addAttachmentAsString("1: Incomming Webhook", body ,"text/xml");
        return message;
}