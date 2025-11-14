import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
	def messageLog = messageLogFactory.getMessageLog(message);
	def Payload = message.getBody(String.class);
	if (message.getProperty("Logging".toString()) == 'YES'){
	    messageLog.addAttachmentAsString("3. Salesforce Response" , Payload, "text/xml");
	}
	return message;
}
