// Below Script add Tenant URL to the JSON body that will be accepted by BTP Alert Notification Service 

import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def Message processData(Message message) {
    // Fetch properties
    def map = message.getProperties()
    def tenantURL = map.get("CloudIntegrationTenantURL") ?: "???"
    def tenantENV = map.get("CloudIntegrationEnvironment") ?: "???"

    // Get the input source body
    def sourceBody = message.getBody(java.lang.String) as String;

    // Define json slurper for parsing inbound JSON
    def jsonSlurper = new JsonSlurper()
    def json = jsonSlurper.parseText(sourceBody)
    
    // Check if the "subject" field exists and update it
    if (json.containsKey("subject")) {
        def currentSubject = json.subject
        def updatedSubject = currentSubject.replace("Alert - ", "Alert - ${tenantENV} - ")
        json.subject = updatedSubject
    }

    // Check if the "tags" object exists, and create it if it doesn't
    if (!json.containsKey("tags")) {
        json.tags = [:]
    }

    if (json.tags == null) {
        json.tags = [:]
    }

    // Add an extra field for Tenant URL under the "tags" section
    json.tags."Cloud Integration Tenant URL" = tenantURL

    // Convert the modified Groovy object back to JSON
    def jsonBuilder = new JsonBuilder(json)
    def modifiedJson = jsonBuilder.toPrettyString()
    message.setBody(modifiedJson)
    return message
}
