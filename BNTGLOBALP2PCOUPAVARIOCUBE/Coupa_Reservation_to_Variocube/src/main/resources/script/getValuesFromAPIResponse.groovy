import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {
    def payload = message.getBody(String)
    def json = new JsonSlurper().parseText(payload)
    
	def Coupa_Status = ""
	def VC_deliveryId = ""
	
	Coupa_Status = json.delivery.state.toString()
	VC_deliveryId = json.delivery.deliveryId.toString()
	
	message.setProperty("Coupa_Status", Coupa_Status)
	message.setProperty("VC_deliveryId", VC_deliveryId)
	
	return message
}