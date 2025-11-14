import com.sap.gateway.ip.core.customdev.util.Message

/**
* RemovePlainLines
* This Groovy script removes lines from a plain or csv paylaod
* There are 'get' or 'remove' parameter to get or remove lines in payload.
* It removes leading UTF-8 BOM and all empty lines.
*
* Groovy script parameters:
* - RemovePlainLines.DebugMode = 'true' creates property entries
* - RemovePlainLines.RemoveHeaderLines = Number greater '0' to remove leading header lines
* - RemovePlainLines.KeyLine = Keys to start lines separated by comma
* - RemovePlainLines.KeyLineGetOrRemove = Set 'get' or 'remove' to get or remove lines in payload
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	// Debug mode
	boolean debugMode = false

	// Constants
	final int DEFAULT_REMOVE_HEADER_LINES = 0
	final String DEFAULT_LINE_SEPARATOR = "\r\n"
	final String DEFAULT_KEY_LINE_GET_OR_REMOVE = "remove"

	// Variables
	long linesTotal = 0
	long countTotal = 0
	int i = 0
	boolean appendLineSeparator = false
	int removeHeaderLines = 0
	boolean mandatoyLines = false
	String keyLineGetOrRemove = ""
	String inRemoveHeaderLines = ""
	String inKeyLine = "" // Keys to start lines separated by comma
	String inKeyLineGetOrRemove = "" // Set 'get' or 'remove' to get or remove lines in payload
	boolean keyLineRemove = false
	String[] keysLine = ""
	String keyLine = ""
	StringBuffer str = new StringBuffer()

	/*
	 * Get message properties and set default mapping parameters
	 */

	// Set parameter DEBUG_MODE from the Message Properties
	String inDebugMode = getExchangeProperty(message, "RemovePlainLines.DebugMode", false) as String
	if (inDebugMode != null) {
		if ("true".equalsIgnoreCase(inDebugMode)) {
			debugMode = true
		} else if ("yes".equalsIgnoreCase(inDebugMode)) {
			debugMode = true
		} else if ("t".equalsIgnoreCase(inDebugMode)) {
			debugMode = true
		} else if ("y".equalsIgnoreCase(inDebugMode)) {
			debugMode = true
		}
	}

	// Set parameter REMOVE_HEADER_LINES from the Message Properties
	inRemoveHeaderLines = getExchangeProperty(message, "RemovePlainLines.RemoveHeaderLines", false) as String
	if (inRemoveHeaderLines != null && inRemoveHeaderLines.length() > 0) {
		if (!inRemoveHeaderLines.isNumber()) {
			throw Exception("Parameter of remove header lines '" + inRemoveHeaderLines + "' has not mandatory numeric sign.")
		} else {
			removeHeaderLines = Integer.valueOf(inRemoveHeaderLines) as Integer
		}
	} else {
		removeHeaderLines = DEFAULT_REMOVE_HEADER_LINES
		mandatoyLines = true
	}

	// Set parameter KEY_LINE from the Message Properties
	inKeyLine = getExchangeProperty(message, "RemovePlainLines.KeyLine", mandatoyLines) as String
	if (inKeyLine != null && inKeyLine.length() > 0) {
		mandatoyLines = true
	}

	// Set parameter KEY_LINE_GET_OR_REMOVE from the Message Properties
	inKeyLineGetOrRemove = getExchangeProperty(message, "RemovePlainLines.KeyLineGetOrRemove", mandatoyLines) as String

	if (inKeyLineGetOrRemove == null || inKeyLineGetOrRemove.length() == 0) {
		keyLineGetOrRemove = DEFAULT_KEY_LINE_GET_OR_REMOVE
	} else if (!(inKeyLineGetOrRemove.equalsIgnoreCase("get") || inKeyLineGetOrRemove.equalsIgnoreCase("remove"))) {
		throw Exception("Parameter of key line get or remove '" + inKeyLineGetOrRemove + "' has not mandatory 'get' or 'remove'.")
	} else {
		keyLineGetOrRemove = inKeyLineGetOrRemove.toLowerCase()
	}

	// Set messag log object
	def messageLog = messageLogFactory.getMessageLog(message)
	
	// Set messag log in debug mode
	if (debugMode == true) {
		if(messageLog != null){
			messageLog.setStringProperty("RemovePlainLines.DebugMode", debugMode.toString())
			messageLog.setStringProperty("RemovePlainLines.RemoveHeaderLines", inRemoveHeaderLines.toString())
			messageLog.setStringProperty("RemovePlainLines.KeyLine", inKeyLine.toString())
			messageLog.setStringProperty("RemovePlainLines.KeyLineGetOrRemove", inKeyLineGetOrRemove.toString())
		}
	}

	/*
	 * Process data
	 */

	if (message.getBodySize() > 0) {
		// Get body
		def body = message.getBody(java.lang.String)

		// Remove leading UTF-8 BOM
		if (body.startsWith("\uFEFF")) {
			body = body.substring(1)
		}

		// Loop all lines
		body.eachLine { line, count ->
			// Remove header lines
			if (count > removeHeaderLines - 1) {
				// Remove blank lines
				if (! line.trim().isEmpty()) {
					if (inKeyLine != null && inKeyLine.length() > 0) {
						keysLine = inKeyLine.split(',')
					}

					// Check key to GET line
					if (keyLineGetOrRemove.equalsIgnoreCase("get") && inKeyLine != null && inKeyLine.length() > 0) {
						for (i = 0; i < keysLine.length; i++) {
							keyLine = keysLine[i]
							if (keyLine.length() > 0) {
								if (line.startsWith(keyLine)) {
									// Append line separator
									if (appendLineSeparator == true) {
										str.append(DEFAULT_LINE_SEPARATOR)
									}

									// Append line
									str.append(line)
									appendLineSeparator = true
								}
							}
						}

					// Check key to REMOVE line
					} else if (keyLineGetOrRemove.equalsIgnoreCase("remove")  && inKeyLine != null && inKeyLine.length() > 0) {
						keyLineRemove = false

						for (i = 0; i < keysLine.length; i++) {
							keyLine = keysLine[i]
							if (keyLine.length() > 0) {
								if (line.startsWith(keyLine)) {
									keyLineRemove = true
									break
								}
							}
						}

						if (keyLineRemove == false) {
							// Append line separator
							if (appendLineSeparator == true) {
								str.append(DEFAULT_LINE_SEPARATOR)
							}

							// Append line
							str.append(line)
							appendLineSeparator = true
						}
					} else {
						// Append line separator
						if (appendLineSeparator == true) {
							str.append(DEFAULT_LINE_SEPARATOR)
						}

						// Append line
						str.append(line)
						appendLineSeparator = true	
					}
				}
			}
			countTotal = count + 1
		}
		body = str.toString()
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