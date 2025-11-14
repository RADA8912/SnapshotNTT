
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body
    def body = message.getBody(java.lang.String)as String;
    
    String text = body.substring( 1, body.length() - 1 );
    
    message.setBody(text);
    
    return message;
}