import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import org.apache.camel.Exchange

def Message processData(Message message) {
    String encodingExchange = message.getProperty(Exchange.CHARSET_NAME);

    def encoding =  encodingExchange == null ?  "UTF-8" : encodingExchange;
    def body = message.getBody()
    if (body instanceof String){
        def size = body.getBytes(encoding).length
        message.setHeader("anPayloadSize", size);
    } else {
        message.setHeader("anPayloadSize", message.getBodySize())
    }

    String uuid = UUID.randomUUID().toString();
    message.setHeader("MessageId", uuid)

    return message;
}