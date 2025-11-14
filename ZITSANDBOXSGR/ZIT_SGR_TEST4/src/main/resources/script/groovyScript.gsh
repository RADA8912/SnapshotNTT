import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.*

/**
* JSON Formatter: Format all values in JSON message to JSON data types with REGEX.
*
* @author itelligence.de
* @version 1.0.0
*/
def Message processData(Message message) {
	// Following numerics need to format as string:
	// - Thousand separators are not allowed in numeric JSON data type.
	// - Decimal separator comma are not allowed in numeric JSON data type.
	
	def body = message.getBody(java.lang.String) as String

	// Format JSON data type integer (for example #)
	String output = body.replaceAll("\"(\\d+)\"", "\$1")
	
	// Format JSON data type negative integer (for example -#)
	output = output.replaceAll("\"(-\\d+)\"", "\$1")

	// Format JSON data type numeric with decimal separator point (for example #.##)
	output = output.replaceAll("\"(\\d*\\.\\d+)\"", "\$1")

	// Format JSON data type negative numeric with decimal separator point (for example -#.##)
	output = output.replaceAll("\"(-\\d*\\.\\d+)\"", "\$1")

	// Format JSON data type boolean (for example 'true' or 'false')
	output = output.replaceAll("\"(true)\"", "\$1")
	output = output.replaceAll("\"(false)\"", "\$1")

	// Format to null
	output = output.replaceAll("\"(_JsOnNulL_)\"", "null")
	
	// Format to string
	// This is inserted to send numeric values as string.
	output = output.replaceAll("(_JsOnSTriNg_)", "")
	
	// Pretty Print of JSON message
	output = JsonOutput.prettyPrint(output)

	// Format to empty array
    // Need to do after pretty pint
	output = output.replaceAll("\"(_JsOnEmPtyArRay_)\"", "[]")

	// Set body
	message.setBody(output)
	return message
}