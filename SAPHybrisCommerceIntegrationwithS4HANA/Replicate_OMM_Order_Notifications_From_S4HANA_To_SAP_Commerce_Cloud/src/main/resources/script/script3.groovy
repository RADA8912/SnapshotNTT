import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

    def properties = message.getProperties() as Map < String, Object > ;

    if (properties.get("enableLog") == "true") {

        def headers = message.getHeaders() as Map < String, Object > ;
        def propertiesAsString = "\n";
        properties.each {
            it -> propertiesAsString = propertiesAsString + "${it}" + "\n"
        };

        def headersAsString = "\n";
        headers.each {
            it -> headersAsString = headersAsString + "${it}" + "\n"
        };
        
        def messageLog = messageLogFactory.getMessageLog(message);
        def body = message.getBody(java.lang.String) as String;
        if (messageLog != null) {
            messageLog.addAttachmentAsString("Log - Delivery Notification", "\n Properties \n ----------   \n" + propertiesAsString +
                "\n headers \n ----------   \n" + headersAsString +
                "\n Body \n ----------  \n\n" + body,
                "text/xml");
        }
    }

    return message;
}