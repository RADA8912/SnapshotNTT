import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.*
def Message processData(Message message) {

    def body = message.getBody(new java.lang.String().getClass());

    body = body.replaceAll("<PicklistOption>","<PicklistEmplstatus>");
    body = body.replaceAll("</PicklistOption>","</PicklistEmplstatus>");

    message.setBody(body);

    return message;
}