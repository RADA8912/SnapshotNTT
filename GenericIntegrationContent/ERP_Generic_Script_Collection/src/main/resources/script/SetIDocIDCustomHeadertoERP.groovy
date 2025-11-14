import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.io.*

/**
 * Set IDoc header properties
 */
def Message processData(Message message) {
        //Create Message Factory
       def messageLog = messageLogFactory.getMessageLog(message)
       
       //Get desired Headerfield - in this case, the contents of SapIDocDbId
       def map = message.getHeaders();
       def value = map.get("SapIDocDbId");
       
       //Add Custom Header with the desired Name and the contents of the Headerfield
       messageLog.addCustomHeaderProperty("IDoc", value)
       return message
}