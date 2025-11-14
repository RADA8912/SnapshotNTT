import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*;
import java.util.HashMap;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    
    // Get LogLevel of the artifact

    def map = message.getProperties();
	def logConfig = map.get("SAP_MessageProcessingLogConfiguration");
	def logLevel = (String) logConfig.logLevel;
	def payload = map.get("payload")
    def messageLog = messageLogFactory.getMessageLog(message);

    if(messageLog != null){
            messageLog.setStringProperty("Logging", "Printing Payload As Attachment");
            messageLog.addAttachmentAsString("Error Log", body , "text/plain");
            messageLog.addAttachmentAsString("Payload Source", payload , "text/plain");
    }
    return message;
}