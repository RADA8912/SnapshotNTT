import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message checkContext(Message message) {
       //Headers 
       def header = message.getHeaders();
       def context = header.get("context");
       if (context==null){
        context = "Result"
        }
       message.setHeader("context", context);
       return message;
}