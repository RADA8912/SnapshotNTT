import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/**
 * Logs payload as attachment
 */
def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String;
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		messageLog.addAttachmentAsString("Payload Single Airtable Contract", body, "text/plain");
	}	
	

    
   //Read Internal Number
    def intrID = message.getHeaders().get("InternalRealEstateNumber");        
    //Set as Custom Header
    if(intrID!=null){
    messageLog.addCustomHeaderProperty("InternalRealEstateNumber", intrID);
    }
	

   //Read Contract ID
    def sapID = message.getHeaders().get("ContractID");        
    //Set as Custom Header
    if(sapID!=null){
    messageLog.addCustomHeaderProperty("ContractID", sapID);
    }
	
	return message;	

}