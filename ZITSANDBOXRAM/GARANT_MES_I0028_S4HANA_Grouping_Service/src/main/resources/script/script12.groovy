import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	def messageLog = messageLogFactory.getMessageLog(message);
	
	def bodyAsString = message.getBody(String.class);
	
	messageLog.addAttachmentAsString("grouping", bodyAsString, "text/xml");
	message.setProperty("messageBody", bodyAsString)
	return message;

}