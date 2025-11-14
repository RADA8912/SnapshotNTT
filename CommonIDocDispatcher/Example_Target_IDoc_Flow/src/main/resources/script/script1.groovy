import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

	def reader = message.getBody(Reader)
	def messageLog = messageLogFactory.getMessageLog(message)
	if (messageLog)
		messageLog.addAttachmentAsString("Payload", reader.getText(), "text/xml");
	
    return message
}