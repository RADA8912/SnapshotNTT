import com.sap.gateway.ip.core.customdev.util.Message;

//Store the offset variable as custom header to monitor it.

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    def bodyAsString = message.getBody(String.class);

    
    if(messageLog != null){
        
        //Read customerID
        def offset = message.getProperties().get("Offset"); 
        def from_ts = message.getProperties().get("from_ts");
        def to_ts = message.getProperties().get("to_ts");


        //Set as Custom Header
        if(offset!=null){
        messageLog.addCustomHeaderProperty("Offset", offset as String);
        }
        
	    //Set timestamp as Custom Header
        if(from_ts!=null){
        messageLog.addCustomHeaderProperty("From", from_ts as String);
        }
        if(to_ts!=null){
        messageLog.addCustomHeaderProperty("To", to_ts as String);
        }
        
        messageLog.addAttachmentAsString("Asset Master Payload", bodyAsString, "text/xml");

        

        
    }
    return message;
}