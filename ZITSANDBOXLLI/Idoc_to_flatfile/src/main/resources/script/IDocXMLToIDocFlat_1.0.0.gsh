import com.sap.gateway.ip.core.customdev.util.Message

/**
* IDocXMLToIDocFlat
* This Groovy script converts IDoc-XML to IDoc-Flat.
* Converter do not create or check fixed element length. This must do in a step before.
*
* Groovy script parameters (exchange properties)
* - IDocXMLToIDocFlat.segmentTerminator = Segment terminator
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	final String DEFAULT_SEGMENT_TERMINATOR = '\r\n'

	String segmentTerminator = ""
	String body = ""

	// Get exchange properties
	segmentTerminator = getExchangeProperty(message, "IDocXMLToIDocFlat.segmentTerminator", false)
	if (segmentTerminator == null || segmentTerminator.length() == 0) {
		segmentTerminator = DEFAULT_SEGMENT_TERMINATOR
	}
	segmentTerminator = createNewLine(segmentTerminator)
	
	// Get body
	def bodyXML = message.getBody(java.lang.String) as String

	if (bodyXML.length() > 0) {	
		XmlSlurper slurper = new XmlSlurper()
		// Keep whitespaces
		slurper.keepIgnorableWhitespace = true
		def root = slurper.parseText(bodyXML)

		// Convert IDoc-XML to IDoc-Flat
		root.'**'.findAll { it ->
			// Do nothing on root or start segments
			if (it.name() == root.name() || it.name() == "IDOC" || it.name() == "EDI_DC40") {
				// Do nothing

			// Set name of node
			} else if (it.attributes().find {'segment'.equalsIgnoreCase(it.key)}) {
				body += segmentTerminator + it.name()

			// Set elements in node
			} else {
				// Create a segment terminator in front of EDI_DC40 if not is first occurrence
				if (body.length() > 0 && it.text().startsWith('EDI_DC40')) {
					body += segmentTerminator
				}
				body += it.text()
			}
		}
	}
	
	message.setBody(body)

	return message
}

/**
 * getExchangeProperty
 * @param message This is message.
 * @param propertyName This is name of property.
 * @param mandatory This is parameter if property is mandatory.
 * @return propertyValue Return property value.
 */
private getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (propertyValue == null || propertyValue.length() == 0) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * createNewLine
 * @param value This is value.
 * @return value Return value.
 */
private createNewLine(String value) {
	value.replace("\\n", "\n")
		.replace("\\r", "\r")
}