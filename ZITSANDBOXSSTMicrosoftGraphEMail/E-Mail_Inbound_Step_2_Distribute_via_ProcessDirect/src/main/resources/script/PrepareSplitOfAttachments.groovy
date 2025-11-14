import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def body = message.getBody(String)
	def slurper = new JsonSlurper()
	def parsed_body = slurper.parseText(body)
	def new_body = ""
	
	for (attachment in parsed_body.value) {		
		def temp_body = [
		        name: attachment.name,
				contentType: attachment.contentType,
				contentBytes: attachment.contentBytes
			]
		new_body = new_body + JsonOutput.toJson(temp_body) + "\n"
	}

	message.setBody(new_body)
	return message
}