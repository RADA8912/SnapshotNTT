import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler

/**
* GetFirstSOAPAttachment
* This Groovy script gets first attachment of SOAP-message and set it to message body.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	String attachmentFileName = ""

	// Connect message log
	def messageLog = messageLogFactory.getMessageLog(message)

	// Get attachments
	Map<String, DataHandler> attachments = message.getAttachments()

	// Check if attaments are available
	if (!attachments.isEmpty()) {
		// Set first attachment to body
		message.setBody(attachments.values()[0].getContent())
		attachmentFileName = attachments.values()[0].getName()

		// Set attachment file name to header to save payload as file
		message.setHeader("CamelFileName", attachmentFileName)

		// Set attachment file name to property
		message.setProperty("AttachmentFileName", attachmentFileName)

		// Set attachment file name to custom header property
		messageLog.addCustomHeaderProperty("AttachmentFileName", attachmentFileName)
	} else {
		throw Exception("No attachment found in message.")	
	}

	return message
}