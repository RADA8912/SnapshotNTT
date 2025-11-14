
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {

	// Get body
	def body = message.getBody(java.lang.String) as String;

	// Remove empty lines with RegEx
	body = body.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
			
	message.setBody(body);

    return message;

}