import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    
    Reader reader = message.getBody(Reader)    
    def json = new JsonSlurper().parse(reader)
    def routingCondition = "InspectionResult"

    if (json.containsKey("InspLotUsageDecisionLevel")) {
        routingCondition = "UsageDecision"
    }
    
    message.setProperty("routingCondition", routingCondition)
    message.setProperty("InspLotNumber", json.InspectionLot)
       
    // JSON zurück in String konvertieren und setzen
   message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(json)))
   return message
}
