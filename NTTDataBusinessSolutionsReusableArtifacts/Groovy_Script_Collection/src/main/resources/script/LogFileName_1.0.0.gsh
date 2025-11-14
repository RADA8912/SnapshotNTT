import com.sap.gateway.ip.core.customdev.util.Message

/**
* LogFileName
* This Groovy script gets header value from 'CamelFileName' and set it to message log custom header property 'FileName'.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Get header value
	String fileName = getHeader(message, 'CamelFileName', false)

	// Connect message log
	def messageLog = messageLogFactory.getMessageLog(message)

	// Set file name to log
	if (fileName) {
		messageLog.addCustomHeaderProperty('FileName', fileName)
	} else {
		messageLog.addCustomHeaderProperty('FileName', '')
	}

	return message
}

/**
 * getHeader
 * @param message This is message.
 * @param headerName This is name of header.
 * @param mandatory This is parameter if header is mandatory.
 * @return headerValue Return header value.
 */
private getHeader(Message message, String headerName, boolean mandatory) {
	String headerValue = message.getHeaders().get(headerName) as String
	if (mandatory) {
		if (!(headerValue)) {
			throw Exception("Mandatory header '$headerName' is missing.")
		}
	}
	return headerValue
}