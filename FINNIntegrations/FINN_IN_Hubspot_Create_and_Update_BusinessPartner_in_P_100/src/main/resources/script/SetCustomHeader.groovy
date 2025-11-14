import com.sap.gateway.ip.core.customdev.util.Message;

/**
 * This method logs Names, IDs and the payload for monitoring.
 * @param message
 * @return message
 */

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
        //Read HubspotID
        def hubspotID = message.getProperties().get("BPIdentificationNumberHub");        
        //Set as Custom Header
        if(hubspotID!=null){
        messageLog.addCustomHeaderProperty("HubspotID", hubspotID);
        }
        
	   //Read FirstName & LastName
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
        messageLog.addAttachmentAsString("Payload Original Body", body, "text/plain");
        }
       
        
        
    }
    return message;
}