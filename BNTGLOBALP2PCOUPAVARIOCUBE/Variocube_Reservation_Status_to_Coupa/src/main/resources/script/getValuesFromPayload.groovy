import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {
    def payload = message.getBody(String)
    def json = new JsonSlurper().parseText(payload)
    
	def Coupa_Status = ""
	def Coupa_ID = ""
	
	Coupa_Status = json.delivery.state
	Coupa_ID = json.metadata.foreignId.toString()
	
	if(Coupa_Status == null){
	    Coupa_Status = "Unknown"
	}
	
	message.setProperty("Coupa_Status", Coupa_Status)
	message.setProperty("Coupa_ID", Coupa_ID)
	
	return message
}