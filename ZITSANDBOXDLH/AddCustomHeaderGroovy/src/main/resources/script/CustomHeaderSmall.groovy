import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		//Read IDoc number from Header
		def Name = message.getHeaders().get("CustomHeaders_Name");		
		def Value = message.getHeaders().get("CustomHeaders_Value");		
		//Set IDoc number as Custom Header
		if(Name!=null)
			messageLog.addCustomHeaderProperty(Name, Value);		
	}
	return message;
}