import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	def body = message.getBody(java.lang.String);

	body = body.replace("<ORDER version=\"1.0\"", "<ORDER xmlns=\"http://www.opentrans.org/XMLSchema/1.0\"  version=\"1.0\"");

	message.setBody(body);
	
	return message;
}