import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String;
	def headers = message.getHeaders() as Map<String, Object>;
	def messageLog = messageLogFactory.getMessageLog(message);
	def propertyMap = message.getProperties();
	String IdocType = propertyMap.get("IdocType");
	String logger = propertyMap.get("local_log");
	if(logger.equals("true")){
		if(messageLog != null){
			messageLog.addAttachmentAsString(IdocType, body, "text/xml");
		}
	}
	return message;
}