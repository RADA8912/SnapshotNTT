import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

	def pmap = message.getProperties();
	
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	def propertiesAsString ="\n";
	properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };
	
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null && properties.get("enableLog") == "true") {
		messageLog.addAttachmentAsString("Log - IDoc" , "\n Properties \n ----------   \n" + propertiesAsString +
		                                                   "\n Body \n ----------  \n\n" + body,
		                                                   "text/xml");
	}
	
	return message;
}