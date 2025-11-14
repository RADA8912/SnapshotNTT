import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    // Get the body from the message
    def body = message.getBody(String)
    
    // Parse the JSON input
    def jsonSlurper = new JsonSlurper()
    def inputJson = jsonSlurper.parseText(body)
    
    // Extract the "results" array
    def results = inputJson?.d?.results
    
    // Remove specified segments from each result
    def cleanedResults = results.collect { result ->
        result.findAll { key, value ->
            !["__metadata", "to_InspResultValue", "to_InspSmplResult"].contains(key)
        }
    }
    
    // Create the output structure as valid json 
    def cleanedJson = JsonOutput.toJson([Characteristics: cleanedResults])
    def prettyJson = JsonOutput.prettyPrint(cleanedJson)
    
    // Set the transformed JSON as the body of the message
    message.setBody(prettyJson)
    
    return message
}
