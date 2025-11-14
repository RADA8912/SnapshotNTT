import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.*

def Message processData(Message message) {
    def json = new JsonSlurper().parseText(message.getBody(String));
//    message.setHeader('SALESORDER',json.data.KEY[0].SALESORDER);
    message.setHeader('eventType',json.eventType);

    return message;
}