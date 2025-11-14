import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;
def Message processData(Message message) {
        //Parse Json Body
        def body = message.getBody(java.lang.String) as String;
        def slurper = new JsonSlurper();
        def parsedJson = slurper.parseText(body);

        def id=parsedJson.data.createFactSheet.factSheet.id;

     
        message.setProperty("LeanIX_ObjectID", id);
        
        
      
        return message;
}