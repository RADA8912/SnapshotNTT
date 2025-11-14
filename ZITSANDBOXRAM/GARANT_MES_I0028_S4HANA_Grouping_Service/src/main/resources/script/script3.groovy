
import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    //Body
    def body = message.getBody(String);

    //Properties
    def properties = message.getProperties();
    def value = properties.get("newBody");
    
    message.setProperty("updateBody", body)
    
    message.setBody(value);
    return message;
}