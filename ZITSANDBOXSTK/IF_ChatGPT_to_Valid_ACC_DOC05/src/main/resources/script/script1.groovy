import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonBuilder;
    
def Message processData(Message message) {
    //Body 
    def body = message.getBody(String);
    message.setBody(new JsonBuilder(body).toPrettyString());
    
    return message;
    }
