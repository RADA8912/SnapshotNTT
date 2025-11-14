import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def body = message.getBody();
    def replace = body.toString()
    replace = replace.replaceAll("<","(")
    replace = replace.replaceAll(">",")")
    message.setBody(replace);
    return message;
}