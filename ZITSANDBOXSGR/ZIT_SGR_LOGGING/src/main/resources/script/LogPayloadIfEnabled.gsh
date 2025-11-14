import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

	def pmap = message.getProperties();
	String enableLogging = pmap.get("ENABLE_PAYLOAD_LOGGING");
	
	if(enableLogging != null && enableLogging.toUpperCase().equals("true")){
		def body = message.getBody(java.lang.String) as String;
		def messageLog = messageLogFactory.getMessageLog(message);
		if(messageLog != null){
		  messageLog.addAttachmentAsString("Payload", body, "text/plain");
		}
	}
	
	return message;
}