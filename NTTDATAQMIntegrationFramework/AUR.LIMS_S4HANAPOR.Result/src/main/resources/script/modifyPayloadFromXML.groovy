import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {    
    Reader reader = message.getBody(Reader)        
    def sourcePayload = message.getProperty("sourcePayload")
    def sourceJson = new JsonSlurper().parseText(sourcePayload)
            
    //replace ChangedDateTime with value from Inspection Lot
    sourceJson.ChangedDateTime = message.getProperty("timestamp")

   message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(sourceJson)))
   return message
}