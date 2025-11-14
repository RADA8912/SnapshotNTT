import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {      
	def propertyMap = message.getProperties()
	def idocnumber = propertyMap.get("IDoc_number")
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){     
    messageLog.addCustomHeaderProperty('IDoc number', idocnumber );
    }
    return message
}