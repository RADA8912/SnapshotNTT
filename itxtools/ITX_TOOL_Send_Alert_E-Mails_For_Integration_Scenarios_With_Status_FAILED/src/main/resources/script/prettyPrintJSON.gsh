import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.*

/**
* prettyPrintJSON: Pretty-prints and format the JSON payload.
*
* @author itelligence.de
* @version 1.0.0
*/
def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String
	String output = JsonOutput.prettyPrint(body)
	message.setBody(output)
	return message
}