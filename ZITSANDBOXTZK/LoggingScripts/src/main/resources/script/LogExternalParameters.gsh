import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	//Properties  
	def pmap = message.getProperties();
    def messageLog = messageLogFactory.getMessageLog(message);

    String enableLogging = pmap.get("ENABLE_LOGGING");
    
    // Prepare string for MPL attachment content
 	String externalParameters;
 	externalParameters = externalParameters + "\nENABLE_LOGGING = " + enableLogging;
 	
 	// Log parameters	
 	if (messageLog != null) {
 		messageLog.addAttachmentAsString("Externalized Parameters", externalParameters, "text/plain");
 	}
	
	return message;
}