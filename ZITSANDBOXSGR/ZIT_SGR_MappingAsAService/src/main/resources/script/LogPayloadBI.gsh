import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String;
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		messageLog.setStringProperty("Logging", "Save payload 'Payload BI' as attachment")
		messageLog.setStringProperty("Payload_BI", message.getProperty("Payload_BI").toString())
		messageLog.addAttachmentAsString("Payload BI", body, "text/plain");
	}	
	return message;	
}