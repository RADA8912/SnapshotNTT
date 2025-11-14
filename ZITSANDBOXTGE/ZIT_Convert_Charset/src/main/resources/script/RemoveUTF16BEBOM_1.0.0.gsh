import com.sap.gateway.ip.core.customdev.util.Message

/**
* RemoveUTF8BOM
* This Groovy script removes leading UTF-16BE BOM.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {	
	// Get body
	def body = message.getBody(java.lang.String) as String

/**
	// Remove leading UTF-8 BOM
	// Using FEFF because this is the Unicode character represented by the UTF-16BE byte order mark (FE FF).
	if (body.startsWith("\uFEFF")) {
		body = body.substring(1)
		message.setBody(body)	
	}
*/

	body = body.replace("\u00FE", "")
	body = body.replace("\u00FF", "")

    // Geht nicht
//	body = body.replace("&#xFE;", "")
//	body = body.replace("&#xFF;", "")
	message.setBody(body)

	return message
}