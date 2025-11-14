import com.sap.gateway.ip.core.customdev.util.Message

/**
* PrettyPrintEDIFACT
* This Groovy script creates a carriage return and line feed (CRLF).
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	final String SEGMENT_TERMINATOR = "'"

	def body = message.getBody(java.lang.String) as String

	// Create carriage return and line feed (CRLF)
	body = body.replaceAll(SEGMENT_TERMINATOR, SEGMENT_TERMINATOR + "\r\n")
	message.setBody(body)

	return message
}