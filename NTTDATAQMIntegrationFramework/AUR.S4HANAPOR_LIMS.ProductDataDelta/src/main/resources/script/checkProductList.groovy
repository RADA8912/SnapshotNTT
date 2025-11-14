import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {    
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)    
    def messageLog = messageLogFactory.getMessageLog(message)
    def productListStatus = input.ProductList.isEmpty()

    message.setProperty("Productlist", "notEmpty")	
    if ( messageLog ) {    
        if ( productListStatus ) {    
        message.setProperty("Productlist", "empty")	
        }        
    messageLog.addCustomHeaderProperty("ProductlistIsEmpty", productListStatus.toString())
    }
    return message
} 