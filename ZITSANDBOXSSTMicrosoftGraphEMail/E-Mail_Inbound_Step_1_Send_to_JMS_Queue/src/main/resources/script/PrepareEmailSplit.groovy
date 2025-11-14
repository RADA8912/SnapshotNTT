import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def body = message.getBody(String)
	def slurper = new JsonSlurper()
	def parsed_body = slurper.parseText(body)
	def new_body = ""

	for (mail in parsed_body.value) {
		new_body = new_body + JsonOutput.toJson(mail) + "\n"
	}

	message.setBody(new_body)
	return message
}