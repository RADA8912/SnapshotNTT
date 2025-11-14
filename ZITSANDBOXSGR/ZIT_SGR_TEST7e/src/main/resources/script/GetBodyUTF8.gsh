import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	final String SOURCE_ENCODING = "windows-1252" 
	final String TARGET_ENCODING = "UTF-8"

	// Get body as byte array
	def bodyBytes = message.getBody(byte[].class)
	def bodyIn = new String(bodyBytes, SOURCE_ENCODING).getBytes(TARGET_ENCODING)

	// Get body as string
	String body = new String(bodyIn)

	// Set body
	message.setBody(body)		
	return message
}