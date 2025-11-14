import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    // Retrieve the JSON payloads from message properties
    String payload1 = message.getProperty("inspection")
    String payload2 = message.getProperty("char")

    if (!payload1 || !payload2) {
        throw new IllegalStateException("Both payloads must be provided as message properties")
    }

    // Parse the JSON payloads
    def jsonSlurper = new JsonSlurper()
    def json1 = jsonSlurper.parseText(payload1)
    def json2 = jsonSlurper.parseText(payload2)

    // Combine the two JSON objects
    def combinedJson = json1 + json2

    // Serialize the combined JSON back to a string
    String combinedJsonString = JsonOutput.toJson(combinedJson)

    // Set the combined JSON as the message body
    message.setBody(combinedJsonString)

    return message
}
