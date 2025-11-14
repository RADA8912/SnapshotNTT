import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	def messageLog = messageLogFactory.getMessageLog(message);
	
	def bodyAsString = message.getBody(String.class);
	def map = message.getProperties();
	value = map.get("P_OriginalPayload");
	
	messageLog.addAttachmentAsString("Error Body", bodyAsString, "text/xml");
	messageLog.addAttachmentAsString("Origin Payload", value, "text/xml");
	
	return message;

}