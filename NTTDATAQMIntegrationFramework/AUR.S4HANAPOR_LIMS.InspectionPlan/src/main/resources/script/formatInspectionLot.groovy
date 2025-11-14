import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    // JSON Body aus der Nachricht abrufen
    def body = message.getBody(String)
    def jsonSlurper = new JsonSlurper()
    def jsonData = jsonSlurper.parseText(body)
    
    if (jsonData?.d) {
        jsonData.InspectionLot = jsonData.remove("d")
        jsonData.InspectionLot.remove("to_InspectionLotWithStatus")
        jsonData.InspectionLot.remove("__metadata")        
    }
    
    def cleanedJson = JsonOutput.toJson(jsonData)
    def prettyJson = JsonOutput.prettyPrint(cleanedJson)

    message.setBody(prettyJson)

    return message
}
