import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/**
 * Logs payload as attachment
 */
def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String;
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
	messageLog.addAttachmentAsString("Payload MS", body, "text/plain");
	}	
	return message;	
}