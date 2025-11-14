import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    
    def body = message.getBody(String)
    def messageLog = messageLogFactory.getMessageLog(message)
    
    def jsonParser = new JsonSlurper()    
    jsonData = jsonParser.parseText(body)                
    
    def inspectionLot = jsonData.data?.InspectionLot
	def plant = jsonData.data?.Plant
    def type = "Deleted"
    if (jsonData.type.contains("Created")) { 
        type = "created"} 
    if (jsonData.type.contains("Changed")) { 
        type = "changed"}    
        
    message.setProperty("InspectionLot", inspectionLot)
	message.setProperty("Type", type)
    
    messageLog.addCustomHeaderProperty("InspectionLot", inspectionLot)
    messageLog.addCustomHeaderProperty("Plant", plant)
    messageLog.addCustomHeaderProperty("Type", type)
    
    return message
}
