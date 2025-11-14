import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*


class result {
	String name
	String iD
}

def Message processData(Message message) {

	//Get Response Payload
	def body = message.getBody(String.class);

	def jsonSlurper = new JsonSlurper()
	def json = jsonSlurper.parseText(body)

	def r = [];
	int i = 0;

	json.d.results.each{
		r[i]=new result(name:it.DisplayName, iD: it.TechnicalName);
		i++;
	}
	def output = JsonOutput.toJson(r);
	
	message.setBody("{\"IntegrationPackage\":" + output+ "}")


	return message;

}