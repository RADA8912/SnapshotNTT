import com.sap.gateway.ip.core.customdev.util.Message

/**
 * Logs payload as attachment only is enabled
 */
def Message processData(Message message) {

	def pmap = message.getProperties()
	String enableLogging = pmap.get("ENABLE_PAYLOAD_LOGGING")
	
	if(enableLogging != null && enableLogging.toUpperCase().equals("TRUE")){
		def body = message.getBody(java.lang.String) as String
		def messageLog = messageLogFactory.getMessageLog(message)
		if(messageLog != null){
		  messageLog.addAttachmentAsString("Payload", body, "text/plain")
		}
	}
	
	return message
}