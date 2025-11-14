import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import org.apache.camel.Exchange

def Message processData(Message message) {
    String encodingExchange = message.getProperty(Exchange.CHARSET_NAME);
 
    def encoding =  encodingExchange == null ?  "UTF-8" : encodingExchnage;
     def body = message.getBody()
    if (body instanceof String){
        def size = body.getBytes(encoding).length
        message.setHeader("Content-Length", size);
    } else {
        message.setHeader("Content-Length", message.getBodySize())
    }
    
    return message;
}

