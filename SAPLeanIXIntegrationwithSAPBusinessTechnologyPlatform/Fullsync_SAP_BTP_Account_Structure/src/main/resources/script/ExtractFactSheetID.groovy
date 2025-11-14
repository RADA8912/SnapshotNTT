import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    def body = message.getBody(String)
    def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		messageLog.addAttachmentAsString("Response Create FactSheet GlobalAccount Level 1", body, "text/plain")
	}
    def jsonSlurper = new JsonSlurper()
    def payload = jsonSlurper.parseText(body)
    def id = payload?.data?.createFactSheet?.factSheet?.id
    if (id) {
        message.setProperty("FactSheetID", id)
    } else {
        throw new Exception("ID not found in the payload!")
    }
    return message
}