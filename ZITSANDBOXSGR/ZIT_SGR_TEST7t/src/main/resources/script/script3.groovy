import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;

def Message processData(Message message) {
    
    //get Cloud Foundry environment variables
    def envApplication = new JsonSlurper().parseText(System.getenv("VCAP_APPLICATION"))

    //Create a new map with the attributes from envApplication
    def envApplicationAttributes = [:]

    //interate all attributes
    envApplication.each { key, value ->
        envApplicationAttributes[key] = value
    }

    //Make it readable
    def readableJson = JsonOutput.prettyPrint(JsonOutput.toJson(envApplicationAttributes))
    
    //Create an attachment with Cloud Foundry environment variables
    messageLogFactory.getMessageLog(message)?.addAttachmentAsString('VCAP_APPLICATION.json', readableJson, 'application/json')

    //Set Cloud Foundry environment variables also as body
    message.setBody(readableJson)

    return message
}