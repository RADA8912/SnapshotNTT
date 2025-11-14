import com.sap.gateway.ip.core.customdev.util.Message

/**
 * Logs payload as attachment in "Exeception Subprocess"
 */
def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String
	def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
	messageLog.addAttachmentAsString("Payload Exception", body, "text/plain")
	}	
	return message
}