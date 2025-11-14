import com.sap.gateway.ip.core.customdev.util.Message

/**
* Remove XML header
* This Groovy script removes XML header in payload.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	// Get body
	def body = message.getBody(java.lang.String) as String

	if (body.contains("?xml")) {
		// Remove xml header line
		int i = body.indexOf(">")
		body = body.substring(i + 1)

		// Remove leading CRLF
		int j = body.indexOf("<")
		body = body.substring(j)

		message.setBody(body)
	}

	return message
}