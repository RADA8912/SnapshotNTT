import groovy.json.JsonSlurper
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def body = message.getBody(String)
	def slurper = new JsonSlurper()
	def parsed_body = slurper.parseText(body)
	
	message.setHeader("email_id",               parsed_body.id)
	message.setHeader("email_createdAt",        parsed_body.createdDateTime)
	message.setHeader("email_hasAttachment",    parsed_body.hasAttachments)
	message.setHeader("email_subject",          parsed_body.subject)
	message.setHeader("email_sender",           parsed_body.sender.emailAddress.address.toLowerCase())
	message.setHeader("email_from",             parsed_body.from.emailAddress.address.toLowerCase())
	message.setHeader("email_toRecipients",     parsed_body.toRecipients.emailAddress.address.join(',').toLowerCase())
	message.setHeader("email_ccRecipients",     parsed_body.ccRecipients.emailAddress.address.join(',').toLowerCase())
	message.setHeader("email_bccRecipients",    parsed_body.bccRecipients.emailAddress.address.join(',').toLowerCase())
	message.setHeader("email_bodyContentType",  parsed_body.body.contentType)
	message.setHeader("email_body",             parsed_body.body.content)

	return message
}