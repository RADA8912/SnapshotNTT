import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.AccessTokenAndUser
import com.sap.it.api.ITApiFactory

def Message processData(Message ciMessage) {
	//Build the JSON-Request: Read the Headers
	def emailSubject = ciMessage.getHeader("emailSubject", String)
	if (emailSubject == null) {
		throw new Exception ("Header emailSubject is missing")
	}
	
	def emailAddresses = ciMessage.getHeader("emailAddress", String)
	if (emailAddresses == null) {
		throw new Exception ("Header emailAddress is missing")
	}
	
	def attachmentName = ciMessage.getHeader("attachmentName", String)
	def attachmentContentType = ciMessage.getHeader("attachmentContentType", String)
	def attachmentContentBytes = ciMessage.getHeader("attachmentContentBytes", String)
	
	def emailBody = ciMessage.getBody(String)
	if (emailBody != null) {
    	emailBody = emailBody.replaceAll("\\r?\\n","<br>")
	} else {
		emailBody = ''
	}
	
	//Build the JSON-Request
	def builder = new JsonBuilder()
	builder {
		message {
			subject emailSubject
			body{
				contentType 'HTML'
				content emailBody
			}
			toRecipients(
				emailAddresses.split(";").collect {
					[
						emailAddress : [
							address: it
						]
					]
				}
			)
			if (attachmentName != null && attachmentContentType != null && attachmentContentBytes != null) {
				attachments ([
					{
						'@odata.type' '#microsoft.graph.fileAttachment'
						'name' attachmentName
						'contentType' attachmentContentType
						'contentBytes' attachmentContentBytes
					}
				])
			}
		}
		saveToSentItems true
	}
	
	//Build the JSON-Request: Set the Body
	ciMessage.setBody(builder.toString())
	
	//Create the "Content-Type" Header
	ciMessage.setHeader("Content-Type", "application/json")
	
	//Get the OAuth2 Authorization Code and create the "Authorization" Header
	//Therefore, an "OAuth2 Authorization Code (Microsoft 365)" must be created within the Security Material.
	//The name of this Security Material must be set within the Property "Oauth2AuthorizationCodeCredential".
	def Oauth2AuthorizationCodeCredential = ciMessage.getProperty("Oauth2AuthorizationCodeCredential")
	SecureStoreService secureStoreService = ITApiFactory.getService(SecureStoreService.class, null)
	AccessTokenAndUser accessTokenAndUser = secureStoreService.getAccesTokenForOauth2AuthorizationCodeCredential(Oauth2AuthorizationCodeCredential)
	def token = accessTokenAndUser.getAccessToken()
	ciMessage.setHeader("Authorization", "Bearer "+token)
	
	//Return
	return ciMessage
}