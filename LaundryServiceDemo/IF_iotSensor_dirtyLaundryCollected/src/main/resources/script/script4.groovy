import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;


def Message processData(Message message) {
    
    //Get Body 
    def body = message.getBody(String.class);
    def jsonSlurper = new JsonSlurper();
    def list = jsonSlurper.parseText(body);
    def val=list[0].bagId.toString();
    message.setHeader("bagId",val);
    return message;
}