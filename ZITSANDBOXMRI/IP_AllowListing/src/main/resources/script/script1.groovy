import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    def messageLog = messageLogFactory.getMessageLog(message);

    //headers
    def map = message.getHeaders();

    String value = map.get("x-forwarded-for");
    String[] splitterValues = value != null ? value.split(",") : null;

    if(splitterValues != null && splitterValues.length > 0) {
        int i = 0;
        int clientIPPosition = splitterValues.length - 2;
        def clientIP = splitterValues[clientIPPosition];
        if(clientIP != null && "165.1.238.182".equals(clientIP.trim())){
            messageLog.setStringProperty("Allowlisted ClientIP", clientIP);
        } else {
            messageLog.setStringProperty("Allowlisted", "false");
            throw new RuntimeException("Request not allowed from IP address: " + clientIP + " " + splitterValues + " " + splitterValues[clientIPPosition]);
        }
    } else {
        messageLog.setStringProperty("Allowlisting failed", "'x-forwarded-for' header not found");
    }
    return message;
}