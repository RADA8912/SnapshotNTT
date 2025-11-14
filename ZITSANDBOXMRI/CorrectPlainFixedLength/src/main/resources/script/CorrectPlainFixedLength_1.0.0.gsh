import com.sap.gateway.ip.core.customdev.util.Message

/**
* CorrectPlainFixedLength
* This Groovy script correct length in plain fixed length file. It will repeat sign at end or cut line.
*
* Groovy script parameters (exchange properties)
* - CorrectPlainFixedLength.MaxLengthOfLine = max length of line
* - CorrectPlainFixedLength.RepeatSign = repeate sign
* - CorrectPlainFixedLength.LineSeparator = line separator
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	final Integer DEFAULT_MAX_LENGTH_OF_LINE = 0
	final String DEFAULT_REPEAT_SIGN = ' '
	final String DEFAULT_LINE_SEPARATOR = '\r\n'

	String body = ''
	int maxLengthOfLine = 0

	// Get exchange properties
	// MaxLengthOfLine
	inMaxLengthOfLine = getExchangeProperty(message, "CorrectPlainFixedLength.MaxLengthOfLine", false)
	if (inMaxLengthOfLine == null || inMaxLengthOfLine.length() == 0) {
		maxLengthOfLine = DEFAULT_MAX_LENGTH_OF_LINE
	} else {
		if (!inMaxLengthOfLine.isNumber()) {
			throw Exception("Parameter max length of line '$inMaxLengthOfLine' has not mandatory numeric value.")
		} else {
			maxLengthOfLine = Integer.valueOf(inMaxLengthOfLine) as Integer
		}
		if (maxLengthOfLine <= 0) {
			throw Exception("Parameter max length of line '$inMaxLengthOfLine' has not mandatory numeric value greater '0'.")
		}
	}

	// RepeatSign
	repeatSign = getExchangeProperty(message, "CorrectPlainFixedLength.RepeatSign", false)
	if (repeatSign == null || repeatSign.length() == 0) {
		repeatSign = DEFAULT_REPEAT_SIGN
	}
	if (repeatSign.length() > 1) {
		throw Exception("Parameter repeat sign '$repeatSign' has not mandatory length 1.")
	}

	// LineSeparator
	lineSeparator = getExchangeProperty(message, "CorrectPlainFixedLength.LineSeparator", false)
	if (lineSeparator == null || lineSeparator.length() == 0) {
		lineSeparator = DEFAULT_LINE_SEPARATOR
	}
	lineSeparator = createNewLine(lineSeparator)

	// Get body
	def lines = message.getBody(java.lang.String) as String

	// Find longest line if no max length Of line is configured
	if (maxLengthOfLine <= 0) {
		lines.eachLine { String line ->
			lengthOfLine = line.length()
			if (lengthOfLine > maxLengthOfLine) {
				maxLengthOfLine = lengthOfLine
			}
		}
	}

	// Create new body
	lines.eachLine {line, lineNumber ->
		lengthOfLine = line.length()
		Integer lengthToRepeat = maxLengthOfLine - lengthOfLine

		// Set line separator
		if (lineNumber > 0) {
			body += lineSeparator
		}

		// Fill line up to maximum length
		if (lengthToRepeat > 0) { // Add repeat sign
			String repeatedSigns = repeatSign.multiply(lengthToRepeat).substring(0, lengthToRepeat)
			body += line + repeatedSigns
		} else if (lengthToRepeat < 0) { // Cut line
			body += line.substring(0, maxLengthOfLine)
		} else { // Line has right length
			body += line
		}
	}

	// Set body
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