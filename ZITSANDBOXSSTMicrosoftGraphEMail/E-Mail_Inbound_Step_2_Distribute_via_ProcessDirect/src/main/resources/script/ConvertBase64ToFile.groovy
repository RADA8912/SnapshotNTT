import groovy.json.JsonSlurper
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def body = message.getBody(String)
	def slurper = new JsonSlurper()
	def parsed_body = slurper.parseText(body)
	
	message.setHeader("email_attachmentName", parsed_body.name)
	message.setHeader("email_attachmentContentType", parsed_body.contentType)

	byte[] fileContent = parsed_body.contentBytes.decodeBase64()
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    outputStream.write(fileContent)
	message.setBody(outputStream)
	
	return message
}