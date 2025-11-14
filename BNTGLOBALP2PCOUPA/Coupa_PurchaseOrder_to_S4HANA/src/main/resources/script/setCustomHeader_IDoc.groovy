import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def iDocNumber = message.getHeader("SapIDocDbId", String)
    def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		if(iDocNumber != null){
			messageLog.addCustomHeaderProperty("IDoc", iDocNumber)	
        }
	}
	
	//Return
	return message
}