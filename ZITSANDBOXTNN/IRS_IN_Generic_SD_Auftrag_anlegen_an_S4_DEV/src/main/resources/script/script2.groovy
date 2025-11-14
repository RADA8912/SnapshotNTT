import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
	    
		//Read CreatedByUser
		def CreatedByUser = message.getHeaders().get("CreatedByUser");
		//Set as Custom Header
		if(CreatedByUser!=null){
		messageLog.addCustomHeaderProperty("CreatedByUser", CreatedByUser);	
		}
		
		//Read CreationDate
		def CreationDate = message.getHeaders().get("CreationDate");		
		//Set as Custom Header
		if(CreationDate!=null){
		messageLog.addCustomHeaderProperty("CreationDate", CreationDate);	
		}
		
		//Read LastChangeDate
		def LastChangeDate = message.getHeaders().get("LastChangeDate");		
		//Set as Custom Header
		if(LastChangeDate!=null){
		messageLog.addCustomHeaderProperty("LastChangeDate", LastChangeDate);
		}
	    
	}
	return message;
}