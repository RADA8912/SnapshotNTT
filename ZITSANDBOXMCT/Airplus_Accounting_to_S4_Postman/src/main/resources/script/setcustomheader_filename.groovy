import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {      
	def headerMap = message.getHeaders()
	def fileName = headerMap.get("CamelFileName")
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){     
    messageLog.addCustomHeaderProperty('filename', fileName );
    }
    return message
}