import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {    
    def body = message.getBody(String)    
    def messageLog = messageLogFactory.getMessageLog(message);
    def json
    try {
        json = new JsonSlurper().parseText(body)
    } catch (Exception e) {
        message.setBody("Error in parsing JSON-Payload: ${e.message}")
        return message
    }    
    def inspectionPlan = null
    def inspectionPlanGroup = null 
    def inspectionPlanInternalVersion = null 
    
    if (json?.InspectionPlan && json.InspectionPlan.size() > 0) {
        def firstInspectionHeader = json.InspectionPlan[0]?.InspectionHeaders?.getAt(0)
        if (firstInspectionHeader && firstInspectionHeader.InspectionPlan) {
            inspectionPlan = firstInspectionHeader.InspectionPlan
            inspectionPlanGroup = firstInspectionHeader.InspectionPlanGroup
            inspectionPlanInternalVersion = firstInspectionHeader.InspectionPlanInternalVersion
        }
    }    
    if (inspectionPlan != null) {        
        if(messageLog != null){     
        messageLog.addCustomHeaderProperty("InspectionPlan", inspectionPlan)
        messageLog.addCustomHeaderProperty("InspectionPlanGroup", inspectionPlanGroup)
        messageLog.addCustomHeaderProperty("InspectionPlanInternalVersion", inspectionPlanInternalVersion)        
        }
    } else {
        message.setBody("No valid value found for 'InspectionPlan'.")
    }

    return message
}
