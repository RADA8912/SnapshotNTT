import com.sap.gateway.ip.core.customdev.util.Message;

/**
 * This method stores important information for the monitoring of the iFlow. Business Partner, as well as names and the
 * payload is stored so that is visible within the monitor of the Cloud Integration
 * @param message
 * @return message
 */


def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
        //Read customerID
        def cusID = message.getProperties().get("CustomerID");        
        //Set as Custom Header
        if(cusID!=null){
        messageLog.addCustomHeaderProperty("Customer ID", cusID);
        }
        
	   
        
        
        //Read Body
        def body = message.getBody(java.lang.String) as String;

        //Set as Custom Header
        if(body!=null){
        messageLog.addAttachmentAsString("Payload for Selection Rule", body, "text/plain");
        }
       
        
        
    }
    return message;
}