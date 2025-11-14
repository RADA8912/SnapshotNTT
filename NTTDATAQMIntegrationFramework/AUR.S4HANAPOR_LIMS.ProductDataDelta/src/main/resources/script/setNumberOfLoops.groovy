import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {    
    def messageLog = messageLogFactory.getMessageLog(message)
    def loops = message.getProperty("CamelLoopIndex") as String  
    if ( messageLog ) {     
    messageLog.addCustomHeaderProperty("Loops", loops)
    }
    return message
}
