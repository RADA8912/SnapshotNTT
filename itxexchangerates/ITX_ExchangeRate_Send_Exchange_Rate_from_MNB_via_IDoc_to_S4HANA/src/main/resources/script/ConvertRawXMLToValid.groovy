import com.sap.gateway.ip.core.customdev.util.Message

/**
 * Remove Namespaces in XML
 */
def Message processData(Message message) {
	def body = message.getBody(java.lang.String)
	
	// Convert raw xml to valid
	body = body.replace("&gt;",">")
	body = body.replace("&lt;","<")

	message.setBody(body)
	return message
}