import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/**
 * Creates a attachment with debug values
 */
def Message processData(Message message) {

	def DEBUG = new StringBuffer();
	def messageLog = messageLogFactory.getMessageLog(message);

	DEBUG.append("TEST" + "\n");
	DEBUG.append("Hello world." + "\n");
	
	if(messageLog != null){
		messageLog.addAttachmentAsString("DEBUG", DEBUG.toString(), "text/plain");
	}

	return message;	
}