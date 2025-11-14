import com.sap.gateway.ip.core.customdev.util.Message

/**
* CorrectCSVHeaderNames
* This Groovy script correct header names in CSV payload.
* You can use this if you like to use header names for valid XML-field names.
* For Tabulator you can use '\t'.
*
* Groovy script parameters (exchange properties)
* - CorrectCSVHeaderNames.FieldSeparator = Field separator
*
* @author nttdata-solutions.com
* @version 0.9.0
*/
def Message processData(Message message) {
	final String DEFAULT_LINE_SEPARATOR = '\r\n'
	final String DEFAULT_FIELD_SEPARATOR = ';'

	// Get exchange properties
	fieldSeparator = getExchangeProperty(message, "CorrectCSVHeaderNames.FieldSeparator", false)
	if (fieldSeparator == null || fieldSeparator.length() == 0) {
		fieldSeparator = DEFAULT_FIELD_SEPARATOR
	}
	fieldSeparator = createTabulator(fieldSeparator)

	if (message.getBodySize() > 0) {
		// Get body
		def body = message.getBody(java.lang.String) as String

		// Get line separator
		String lineSeparator = getLineSeparator(body, DEFAULT_LINE_SEPARATOR)

		// Get header line
		def headerLine = body.substring(0, body.indexOf(lineSeparator))

		// Change header line
		// Remove string signs because these signs are not needed
		headerLine = headerLine.replaceAll('"', '').replaceAll("'", '')
		
		def headerList = headerLine.split(fieldSeparator)
		headerList.eachWithIndex { column, idx ->
			String columnNew = ''

			// Trim column, replace all special characters and whitespace to underlined
			columnNew = column.trim().replaceAll('[^a-zA-Z0-9_\\\\s-]', '_')

			// Set underline if a number is at fist position, because number is not allowed at fist position in xsd
			if (Character.isDigit(columnNew.charAt(0))) {
				columnNew = "_" + columnNew
			}
			headerList[idx] = columnNew
		}
		headerLine = headerList.join(fieldSeparator)

		// Set new headerline
		body = headerLine + lineSeparator + body.substring(body.indexOf(lineSeparator) + 1)

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
		if (propertyValue == null || propertyValue.length() == 0) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * createTabulator
 * @param value This is value.
 * @return value Return value.
 */
private createTabulator(String value) {
	value.replace("\\t", "\t")
}

/**
 * Gets line separator in payload.
 *
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