import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	def body = message.getBody(java.lang.String);
	body = body.replace("106", "106B");
	body = body.replace("Target", "Target1");
	message.setBody(body);
	
	return message;
}