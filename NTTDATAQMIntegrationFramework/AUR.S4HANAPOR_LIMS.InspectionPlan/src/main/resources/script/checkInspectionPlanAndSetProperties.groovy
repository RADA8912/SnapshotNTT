import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {

Reader reader = message.getBody(Reader)    
def payload = new JsonSlurper().parse(reader)
def messageLog = messageLogFactory.getMessageLog(message)

def inspectionPlan = payload?.InspectionPlan
    if (!inspectionPlan || inspectionPlan.size() < 4) {
        throw new RuntimeException("InspectionPlan missing or incomplete.")
    }
    
    def headers = inspectionPlan.find { it.containsKey("InspectionHeaders") }?.InspectionHeaders
    def operations = inspectionPlan.find { it.containsKey("PlanOperations") }?.PlanOperations
    def materials = inspectionPlan.find { it.containsKey("MaterialAssignments") }?.MaterialAssignments
    def characteristics = inspectionPlan.find { it.containsKey("Characteristics") }?.Characteristics

    if (!headers || headers.isEmpty()) {
        message.setProperty("planstatus", "incomplete: headers missing")
        messageLog.addCustomHeaderProperty("headers", "empty")
    }
    if (!operations || operations.isEmpty()) {
        message.setProperty("planstatus", "incomplete: operations missing")
        messageLog.addCustomHeaderProperty("operations", "empty")
    }
    if (!materials || materials.isEmpty()) {
        message.setProperty("planstatus", "incomplete: materials missing")
        messageLog.addCustomHeaderProperty("materials", "empty")
    }
    if (!characteristics || characteristics.isEmpty()) {
        message.setProperty("planstatus", "incomplete: characteristics missing")
        messageLog.addCustomHeaderProperty("characteristics", "empty")
    }
    
    return message
}
