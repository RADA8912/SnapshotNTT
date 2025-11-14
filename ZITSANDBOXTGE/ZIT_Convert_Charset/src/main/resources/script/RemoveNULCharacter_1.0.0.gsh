import com.sap.gateway.ip.core.customdev.util.Message

/**
* RemoveNULCharacter
* This Groovy script remove NUL character in body.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Get body
//	def body = message.getBody(java.lang.String) as String

//	if (body.length() > 0) {
		// Remove NUL Character
//		body = body.replace("\u0000", "")

//		message.setBody(body)
//	}

	return message
}