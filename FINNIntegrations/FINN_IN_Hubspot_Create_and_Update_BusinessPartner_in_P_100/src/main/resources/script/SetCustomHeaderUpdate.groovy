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
        
        //Read HubspotID
        def sapID = message.getProperties().get("BusinessPartner");        
        //Set as Custom Header
        if(sapID!=null){
        messageLog.addCustomHeaderProperty("BusinessPartner", sapID);
        }
        
       
        //Read FirstName
        def firstName = message.getProperties().get("FirstName");
        def lastName = message.getProperties().get("LastName")
        //Set as Custom Header
        if(firstName!=null){
        messageLog.addCustomHeaderProperty("Name", firstName + ' ' + lastName);    
        }
        
        //Read Body
        def body = message.getBody(java.lang.String) as String;

        //Set as Custom Header
        if(body!=null){
        messageLog.addAttachmentAsString("Payload:", body, "text/plain");
        }
       
        
        
    }
    return message;
}