import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*

def Message processData(Message message) {
    //Body 
    def jsonOP = message.getBody(String.class);
    jsonOP=jsonOP.toString()
    
//    def json_to_str=jsonOP.substring(0, jsonOP.length()- 1);
    json_to_str="{\"root\": "+jsonOP+"}"
    
    message.setBody(json_to_str);
    return message;
}