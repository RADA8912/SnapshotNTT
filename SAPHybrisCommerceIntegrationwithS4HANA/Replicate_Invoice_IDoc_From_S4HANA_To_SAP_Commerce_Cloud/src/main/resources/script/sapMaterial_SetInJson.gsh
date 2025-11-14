import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;   
import groovy.json.*;

def Message processData(Message message) {

	 def slurper = new JsonSlurper()
	def properties = message.getProperties() as Map<String, Object>;

	def body = message.getBody(java.lang.String) as String;
	def result = slurper.parseText(body);
	def materialDetails=properties.get("material");
	
	if(result.SapB2BDocuments.SapB2BDocument){
	    result.SapB2BDocuments.SapB2BDocument.material=materialDetails;
	}
	message.setBody(JsonOutput.toJson(result));
	


       return message;
}