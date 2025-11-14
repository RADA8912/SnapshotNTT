import com.sap.gateway.ip.core.customdev.util.Message;

//Store the offset variable as custom header to monitor it.

def Message processData(Message message) {
    
   def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
        //Store the original payload
          
        def bodyAsString = message.getBody(String.class);
        messageLog.addAttachmentAsString("Debitor Invoices Payload", bodyAsString, "text/xml");
        
        
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
        
        
         //Set timestamp as Custom Header
        if(to_ts!=null){
        messageLog.addCustomHeaderProperty("To", to_ts as String);
        }
    }
    
    
    return message;
}