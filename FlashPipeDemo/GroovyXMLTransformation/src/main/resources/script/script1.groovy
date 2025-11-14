import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
	
	def messageLog = messageLogFactory.getMessageLog(message);
	
	def bodyAsString = message.getBody(String.class);
	
	messageLog.addAttachmentAsString("Payload", bodyAsString, "text/text");
	
	return message;

}