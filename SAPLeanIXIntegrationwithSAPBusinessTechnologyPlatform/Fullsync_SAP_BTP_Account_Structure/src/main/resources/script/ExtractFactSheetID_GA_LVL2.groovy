import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    def body = message.getBody(String)
    def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		messageLog.addAttachmentAsString("Response Create FactSheet GlobalAccount Level 2", body, "text/plain")
	}
    
    def jsonSlurper = new JsonSlurper()
    def payload = jsonSlurper.parseText(body)
    def id = payload?.data?.createFactSheet?.factSheet?.id
    if (id) {
        message.setProperty("FactSheetID", id)
    } else {
        throw new Exception("ID not found in the payload! Most likely Platform FactSheet Level 2 (SAP BTP Global Account) couldn't be created.")
    }
    return message
}