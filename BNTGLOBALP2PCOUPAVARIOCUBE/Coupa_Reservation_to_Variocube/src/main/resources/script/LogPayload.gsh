import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String
	def vcRequest = message.getProperty("VC_Request")
	def httpStatus = message.getHeader("CamelHttpResponseCode", String)
	def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
	    messageLog.addAttachmentAsString("Variocube Request Payload", vcRequest, "text/plain")
		messageLog.addAttachmentAsString("Variocube Response Payload HTTP Status Code "+httpStatus, body, "text/plain")
	}
	return message
}