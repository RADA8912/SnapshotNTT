import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;

def Message processData(Message message) {
   
    
    def slurper = new groovy.json.JsonSlurper()
	def properties = message.getProperties() as Map<String, Object>;

	def body = message.getBody(java.lang.String) as String;
	
	def result = slurper.parseText(body);
	def material = result.INVOIC02.IDOC.MaterialData;
	properties.put("material",material);
	
    def builder = new JsonBuilder()
    def root = builder.event{                
    "batchChangeSet" material
    };
	
	message.setBody(JsonOutput.toJson(root.event));

       return message;
}