import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.StringEscapeUtils

/**
* EscapeJava
* This Groovy script escape Unicode in payload.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	if (message.getBodySize() > 0) {
		// Get body
		def body = message.getBody(java.lang.String) as String

		// Unescape string
		body = StringEscapeUtils.escapeJava(body)
		message.setBody(body)
	}
	return message
}