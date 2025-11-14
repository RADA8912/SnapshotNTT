import com.sap.gateway.ip.core.customdev.util.Message

/**
 * Remove Namespaces in XML
 */
def Message processData(Message message) {
	def body = message.getBody(java.lang.String)
	
	// Remove Namespaces
	body = body.replace("message:","")
        	.replace("common:","")
        	.replace("generic:","")

	message.setBody(body)
	return message
}