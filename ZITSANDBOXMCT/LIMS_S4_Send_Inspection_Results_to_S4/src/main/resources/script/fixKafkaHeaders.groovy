import com.sap.gateway.ip.core.customdev.util.Message;
import java.nio.charset.StandardCharsets;

def Message processData(Message message) {
    def kafkaHeaders = message.getHeader("kafka.HEADERS", List);
    
    if (kafkaHeaders) {
        kafkaHeaders.each { header ->
            String key = header.key;
            byte[] valueBytes = header.value;
            String value = new String(valueBytes, StandardCharsets.US_ASCII);
            message.setHeader(key, value);
        }
    } 
    return message;
}
