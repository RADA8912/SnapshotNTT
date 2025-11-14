import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
       message.setBody(message.getBody(java.lang.String));
       return message;
}