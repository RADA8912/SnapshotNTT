import com.sap.gateway.ip.core.customdev.util.Message

/**
* LogAllHeaders
* This Groovy script logs all header values.
*
* - LogAllHeaders.Activate = Activate log ('true', 'false', 'yes', 'no')
* - LogAllHeaders.DebugMode = Debug mode ('true', 'false', 'yes', 'no')
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final String DEFAULT_LINE_SEPARATOR = '\r\n'
	final Boolean DEFAULT_ACTIVATE_LOG = false
	final Boolean DEFAULT_DEBUG_MODE = false
	final int DEFAULT_KEY_LENGTH = 29

	Boolean activate
	Boolean debugMode

	// Get exchange properties
	// Activation
	def activateIn = getExchangeProperty(message, 'LogAllHeaders.Activate', false)
	if (activateIn != null ) {
		// Check exchange property
		if ('true'.equalsIgnoreCase(activateIn) || 'yes'.equalsIgnoreCase(activateIn)) {
			activate = true
		} else {
			activate = false
		}
	} else {
		activate = DEFAULT_ACTIVATE_LOG
	}

	// Debug Mode
	def debugModeIn = getExchangeProperty(message, 'LogAllHeaders.DebugMode', false)
	if (debugModeIn != null ) {
		// Check exchange property
		if ('true'.equalsIgnoreCase(debugModeIn) || 'yes'.equalsIgnoreCase(debugModeIn)) {
			debugMode = true
		} else {
			debugMode = false
		}
	} else {
		debugMode = DEFAULT_DEBUG_MODE
	}

	if(activate) {
		def mapHeader = message.getHeaders()
		
		// Create header value string
		String logHeader = 'Message Header Values' + DEFAULT_LINE_SEPARATOR + DEFAULT_LINE_SEPARATOR

		// Create line
		mapHeader.each {key, val ->
			String space = ''
			if(key.length() < DEFAULT_KEY_LENGTH) {
				space = ' '.multiply(DEFAULT_KEY_LENGTH - key.length())
			}
			// Concat line
			logHeader += "$key" + space + " = $val" + DEFAULT_LINE_SEPARATOR
		}

		// Log as attachment
		def messageLog = messageLogFactory.getMessageLog(message)
		if(messageLog != null){
			messageLog.addAttachmentAsString("Header values", logHeader, 'text/plain')
		}

		if (debugMode) {
			// Set header values to body
			message.setBody(logHeader)
		}
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