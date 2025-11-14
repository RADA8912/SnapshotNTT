import com.sap.gateway.ip.core.customdev.util.Message

/**
* RemovePlainHeaderAndFooter
* This Groovy script remove header and footer lines from payload.
*
* Groovy script parameters (exchange properties)
* - RemovePlainHeaderAndFooter.HeaderLines = Number of header lines
* - RemovePlainHeaderAndFooter.FooterLines = Number of footer lines
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final int DEFAULT_HEADER_LINES = 2
	final int DEFAULT_FOOTER_LINES = 0
	final String DEFAULT_LINE_SEPARATOR = '\r\n'

	int headerLines = 0
	int footerLines = 0

	// Get exchange properties
	headerLinesStr = getExchangeProperty(message, "RemovePlainHeaderAndFooter.HeaderLines", false)
	if (headerLinesStr == null || headerLinesStr.length() == 0) {
		headerLines = DEFAULT_HEADER_LINES
	} else if (headerLinesStr.isInteger()) {
		headerLines = headerLinesStr as Integer
	}
	// Set absolute value to correct if negative value was configured
	headerLines = Math.abs(headerLines)

	footerLinesStr = getExchangeProperty(message, "RemovePlainHeaderAndFooter.FooterLines", false)
	if (footerLinesStr == null || footerLinesStr.length() == 0) {
		footerLines = DEFAULT_FOOTER_LINES
	} else if (footerLinesStr.isInteger()) {
		footerLines = footerLinesStr as Integer
	}
	// Set absolute value to correct if negative value was configured
	footerLines = Math.abs(footerLines)

	if (message.getBodySize() > 0) {
		// Get body
		def body = message.getBody(java.lang.String) as String

		// Get line separator
		String lineSeparator = getLineSeparator(body, DEFAULT_LINE_SEPARATOR)

		// Remove header lines
		if (headerLines > 0) {
			body = body.split(lineSeparator).drop(headerLines).join(lineSeparator)
		}

		// Remove footer lines
		if (footerLines > 0) {
			bodyList = body.split(lineSeparator)
			int lines = bodyList.size() - footerLines
			body = bodyList.take(lines).join(lineSeparator)
		}

		message.setBody(body)
	}

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
		if (!propertyValue) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * Gets line separator in payload.
 * @param body Body
 * @param defaultLineSeparator Default line separator
 * @return line separator.
 */
private def String getLineSeparator(String body, String defaultLineSeparator) {
	// Compute line separator in payload
	String lineSeparator = defaultLineSeparator
	if (body.contains('\r\n')) {
		lineSeparator = '\r\n'
	} else if (body.contains('\r')) {
		lineSeparator = '\r'
	} else if (body.contains('\n')) {
		lineSeparator = '\n'
	}	
	return lineSeparator
}