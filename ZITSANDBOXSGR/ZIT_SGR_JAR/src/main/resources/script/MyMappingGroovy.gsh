import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	// Get body
	def body = message.getBody(java.lang.String)

    // Local mapping
    body = getMyLowerCase(body)

    // Set body
	message.setBody(body)
    return message
}

def String getMyLowerCase(String body) {
    // My mapping logic
    body = "My data in lower case: " + body.toLowerCase()

    return body
}