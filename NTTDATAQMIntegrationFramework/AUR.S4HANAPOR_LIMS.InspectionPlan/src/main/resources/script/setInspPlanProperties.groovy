import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    def body = message.getBody(String)
    def json
    try {
        json = new JsonSlurper().parseText(body)
    } catch (Exception e) {
        message.setProperty("ExtractionError", "JSON Parsing Error: ${e.message}")
        return message
    }    
    def data = json?.data
    if (data) {
        def inspectionPlan = data?.InspectionPlan
        def inspectionPlanGroup = data?.InspectionPlanGroup
        def inspectionPlanInternalVersion = data?.InspectionPlanInternalVersion
     
        message.setProperty("InspectionPlan", inspectionPlan)
        message.setProperty("InspectionPlanGroup", inspectionPlanGroup)
        message.setProperty("InspectionPlanInternalVersion", inspectionPlanInternalVersion)
    } else {
        message.setProperty("ExtractionError", "'data' element not found.")
    }

    return message
}