import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.StringBuffer;

def Message processData(Message message) {
    //Body
    def messageLog = messageLogFactory.getMessageLog(message);
    
    String content = "";
    
    messageLog.setStringProperty("Logging", "Payload");
    
    def propertyMap = message.getProperties()  as String;
    content = content + "\n" + "Message Properties" + "\n" + "\n" + propertyMap + "\n" 

    def header = message.getHeaders() as String;
    content = content + "\n" + "Message Headers" + "\n" + "\n" +  header + "\n"

    def body = message.getBody(java.lang.String) as String;
    content = content + "\n" + "Message Body" + "\n" + "\n"  + body + "\n";

    messageLog.addAttachmentAsString("Source message log", content, "text/plain");    
    }

    return message;
}