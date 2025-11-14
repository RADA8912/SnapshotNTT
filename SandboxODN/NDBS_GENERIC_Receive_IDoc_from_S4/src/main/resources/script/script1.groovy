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
    
       
       def SAP_Sender= map.get("SAP_Sender");
       def SAP_Receiver= map.get("SAP_Receiver");
       def SAP_IDocType= map.get("SAP_IDocType");
       def SAP_MessageType= map.get("SAP_MessageType");
       
    
       //Add Custom Header with the desired Name and the contents of the Headerfield
       messageLog.addCustomHeaderProperty("No Receiver found for", SAP_Sender+"|"+SAP_Receiver+"|"+SAP_MessageType+"|"+SAP_IDocType)
       return message
}