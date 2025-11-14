import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def Message processData(Message message) {    
    Reader reader = message.getBody(Reader)
    def input = new JsonSlurper().parse(reader)
    def queues = input.findAll { it.messageCount != 0 && it.name.contains("dead-letter") }.collect{ (it.name ) }
 
    def map = message.getProperties();
	def String currentstage = map.get("currentstage");

    def builder = new JsonBuilder()
    builder {      
        'eventType' "EventMeshError"
        'resource' {
        'resourceName' currentstage
        'resourceType' "app"
        "tags" {
            'sampleTag' "EventmeshStageDeadLetterQueue"           
             }
            }
        'severity' "FATAL"
        'category' "ALERT"
        'subject' "Event Mesh Dead-Letter Queues not empty"
        'body' "Please check dead-letter queues in Event Mesh in subaccount '"+ currentstage + "': " + queues
        
        if ( queues.size()>0 ) {
            message.setProperty("generateAlert", "true")
        } else {
            message.setProperty("generateAlert", "false")
            }
    }
    message.setBody(builder.toPrettyString())
    return message    
}