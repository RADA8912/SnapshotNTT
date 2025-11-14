import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set BP_REL as body and split it after
def Message processData(Message message) {
    	
    String BP_REL_BODY = message.getProperty("BP_REL_PAYLOAD");
    message.setBody(BP_REL_BODY);
    
   return message;
}