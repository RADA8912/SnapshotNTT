import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		//Read IDoc number from Header
		def IDOCNUM = message.getHeaders().get("IDOCNUM");
		def clientid = message.getHeaders().get("SAP_Sender")[7..9] ;
		
		
		//Set IDoc number as Custom Header
		if(IDOCNUM!=null)
			messageLog.addCustomHeaderProperty("IDOCNUM", IDOCNUM);	
			
		message.setProperty("clientid", clientid);	
	}
	return message;
}