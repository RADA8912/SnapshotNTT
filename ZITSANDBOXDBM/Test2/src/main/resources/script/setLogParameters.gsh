import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	def pmap = message.getProperties();
	def messageLog = messageLogFactory.getMessageLog(message);
	
	String enableLogging = pmap.get("ENABLE_PAYLOAD_LOGGING");

	// Prepare string for MPL attachment content
	String externalizedParameters;
	externalizedParameters = externalizedParameters + "\nENABLE_PAYLOAD_LOGGING = " + enableLogging;
	
	// Log parameters	
	if (messageLog != null) {
		messageLog.addAttachmentAsString("Externalized Parameters", externalizedParameters, "text/plain");
	}
	
	return message;
}