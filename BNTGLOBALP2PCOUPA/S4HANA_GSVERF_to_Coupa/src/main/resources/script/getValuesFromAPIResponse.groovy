import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

def Message processData(Message message) {
    def reader = message.getBody(Reader)
    def json = new JsonSlurper().parse(reader)
	def comment_id = ""
	comment_id = json.id
	message.setProperty("comment_id", comment_id)
	return message
}