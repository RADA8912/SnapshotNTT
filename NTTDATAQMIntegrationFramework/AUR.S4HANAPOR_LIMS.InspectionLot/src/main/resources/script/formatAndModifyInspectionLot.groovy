import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    
    Reader reader = message.getBody(Reader) 
    def jsonData = new JsonSlurper().parse(reader)
    def typeValue = message.getProperty("Type")
    
    if (jsonData?.d) {
        jsonData.InspectionLot = jsonData.remove("d")
        jsonData.InspectionLot.remove("to_InspectionLotWithStatus")
        jsonData.InspectionLot.remove("__metadata")        
    }
    jsonData.InspectionLot.Type = typeValue
    def cleanedJson = JsonOutput.toJson(jsonData)
    def prettyJson = JsonOutput.prettyPrint(cleanedJson)
    
    message.setBody(prettyJson)

    return message
}
