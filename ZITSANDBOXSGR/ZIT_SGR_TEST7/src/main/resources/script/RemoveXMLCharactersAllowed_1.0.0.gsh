import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
import groovy.xml.XmlUtil

/**
* RemoveXMLCharactersAllowed
* This Groovy script removes characters by using an allowed list from XML payload.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Umlauts
	final String UMLAUTS = 'äÄöÖüÜß'
	// Allowed special characters
	final String ALLOWED_SPECIAL_CHARACTERS = '/|.,;: _+-=<>@(){}!?%&"\t\'\\[\\]'
	// Carriage return and line feed (CRLF)
	final String ALLOWED_LINE_BREAK = '\\r\\n'

	// alphanumeric values and some allowed special characters
	String allowedValues = 'a-zA-Z0-9' + UMLAUTS + ALLOWED_SPECIAL_CHARACTERS + ALLOWED_LINE_BREAK

	if (message.getBodySize() > 0) {
		// Get body
		Reader reader = message.getBody(Reader)
		XmlSlurper slurper = new XmlSlurper()
		// Keep whitespaces
		slurper.keepIgnorableWhitespace = true
		def root = slurper.parse(reader)

		root.'**'.findAll { it ->
			// Reset on nodes without fields
			if (it.children().size() == 0) {
				if (it.text()) {
					it.replaceBody(it.text().replaceAll('[^' + allowedValues + ']+',''))
				}
			}
		}

		String bodyNew = XmlUtil.serialize(root)

		// Set the message body 
		message.setBody(bodyNew)
	}

	return message
}