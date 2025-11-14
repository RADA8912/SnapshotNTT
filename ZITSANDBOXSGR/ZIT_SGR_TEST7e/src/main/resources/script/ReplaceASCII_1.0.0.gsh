import com.sap.gateway.ip.core.customdev.util.Message
import java.text.Normalizer

/**
* ReplaceASCIISigns
* This Groovy script replaces ASCII signs in payload.
* Attention characters such as 'ß' are not replaced.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	if (message.getBodySize() > 0) {
		// Get body
		def body = message.getBody(java.lang.String) as String

		// Remove ASCII signs
		body = Normalizer.normalize(body, Normalizer.Form.NFD)
		body = body.replaceAll("[^\\p{ANSI}]", "")

		// Set body
		message.setBody(body)
	}

	return message
}