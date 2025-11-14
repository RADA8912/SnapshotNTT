import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*


class result {
	String name
	String iD
	String type
	String Version
}

def Message processData(Message message) {

	//Get Response Payload
	def body = message.getBody(String.class);

	def jsonSlurper = new JsonSlurper()
	def json = jsonSlurper.parseText(body)
	
	def output = " [{\"iD\": \"STOP\",\"name\": \"No IFlow in Selected Package\"}]"

	def r = [];
	int i = 0;

	json.d.results.each{
		r[i]=new result(name:it.DisplayName, iD: it.Name, type: it.Type, Version: it.Version);
		i++;
	}
	
	if(i>0)
		output = JsonOutput.toJson(r);
	
	message.setBody("{\"IntegrationFlow\":" + output+ "}");
	
	return message;

}