import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		//Read IDoc number from Header
		def IDOCNUM = message.getHeaders().get("IDOCNUM");		
		//Set IDoc number as Custom Header
		if(IDOCNUM!=null)
			messageLog.addCustomHeaderProperty("IDOCNUM", IDOCNUM);		
	}
	return message;
}