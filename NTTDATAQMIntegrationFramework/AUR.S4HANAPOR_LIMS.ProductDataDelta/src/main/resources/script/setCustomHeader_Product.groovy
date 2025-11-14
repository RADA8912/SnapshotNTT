import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
        //Create Message Factory
       def messageLog = messageLogFactory.getMessageLog(message)
              
       def product = message.getProperty("Product")
       def type = message.getProperty("Type")
       
       //Add Custom Header with the desired Name and the contents of the Headerfield
       messageLog.addCustomHeaderProperty("Product", product)
       messageLog.addCustomHeaderProperty("Type", type)      
       
       return message
}