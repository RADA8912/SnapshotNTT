import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	
	
	
	def messageLog = messageLogFactory.getMessageLog(message);
	def bodyAsString = message.getBody(String.class);
	messageLog.addAttachmentAsString("Payload", bodyAsString, "text/xml");
	
	 def map = message.getHeaders()
	 def value = map.get("JPY_Currency")
	 message.setHeader("JPY_Currency", value)
	return message;
}