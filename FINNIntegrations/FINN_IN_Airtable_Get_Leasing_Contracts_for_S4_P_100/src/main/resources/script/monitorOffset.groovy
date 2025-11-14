import com.sap.gateway.ip.core.customdev.util.Message;

//Store the offset and timestmap variable as custom header to monitor it.

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
    
    
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
        
          //Set timestamp as Custom Header
        if(to_ts!=null){
        messageLog.addCustomHeaderProperty("To", to_ts as String);
        }
        
    	messageLog.addAttachmentAsString("Payload Airtable Contract", body, "text/plain");
	}
	
    return message;
}