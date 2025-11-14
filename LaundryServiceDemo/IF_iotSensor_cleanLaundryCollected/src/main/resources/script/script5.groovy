import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;


def Message processData(Message message) {
    
    //Get Body 
    def body = message.getBody(String.class);
    def jsonSlurper = new JsonSlurper();
    def list = jsonSlurper.parseText(body);
    
    def val=list.ID.toString();
    message.setHeader("bagId",val);

    def locVal=list.locationId.toString();
    message.setHeader("locationId",locVal);


    return message;
}