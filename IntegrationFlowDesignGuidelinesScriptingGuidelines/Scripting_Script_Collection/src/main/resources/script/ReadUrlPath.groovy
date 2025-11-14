import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message extractUrlPath(Message message) {

       //get url 
       def map = message.getHeaders();
       def url = map.get("CamelHttpUrl");

       //split url
       String[] vUrl;
       vUrl = url.split('/');
       int size = vUrl.length;

       message.setProperty("service", vUrl[size-3]);
       message.setProperty("resource", vUrl[size-2]);
       message.setProperty("id", vUrl[size-1]);
       return message;
}