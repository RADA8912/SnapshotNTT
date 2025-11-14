import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.StringEscapeUtils

/**
* UnescapeJava
* This Groovy script unescape Unicode-Escapes in payload.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	if (message.getBodySize() > 0) {
		// Get body
		def body = message.getBody(java.lang.String) as String

		// Unescape string
		body = StringEscapeUtils.unescapeJava(body)
		message.setBody(body)
	}
	return message
}