import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	map = message.getProperties();
	property_EnableLogging = map.get("EnableLogging");
	message.setHeader("SAP_IsIgnoreProperties",new Boolean(true));
	
	if (property_EnableLogging.toUpperCase().equals("TRUE")) {	
		def body = message.getBody(java.lang.String) as String;
		def messageLog = messageLogFactory.getMessageLog(message);
		messageLog.addAttachmentAsString("Payload", body, "text/plain");
	}	

	return message;
}
