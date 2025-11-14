import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	def body = ""
	message.setBody(body) 
	return message
}