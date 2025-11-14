
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
	// Get body
	def body = message.getBody(java.lang.String) as String

	if (body.length() > 0) {
		
		body = body.replaceAll("&", "&amp;")
		
		message.setBody(body)
	}

	return message
}