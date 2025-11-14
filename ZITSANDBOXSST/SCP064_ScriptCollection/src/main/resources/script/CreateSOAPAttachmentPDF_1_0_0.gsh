import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource

/**
* CreateSOAPAttachmentPDF
* This Groovy script creates an PDF-attachment in SOAP-message from Base64-encoded payload.
*
* Groovy script parameters (exchange properties)
* - CreateSOAPAttachmentPDF.fileName = File name
* - CreateSOAPAttachmentPDF.addTimeStamp = Add time stamp ('true', 'false', 'yes', 'no')
* - CreateSOAPAttachmentPDF.addMessageID = Add message ID ('true', 'false', 'yes', 'no')
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	final String DEFAULT_ATTACHMENT_FILE_NAME = "attachment"
	final String ATTACHMENT_FILE_EXTENSION = "pdf"
	final String MIME_TYPE = "application/pdf"

	// Get exchange properties
	String fileNameIn = getExchangeProperty(message,'CreateSOAPAttachmentPDF.fileName', false)
	if (fileNameIn == null || fileNameIn.length() == 0) {
		fileNameIn = DEFAULT_ATTACHMENT_FILE_NAME
	}
	String addTimeStamp = getExchangeProperty(message,'CreateSOAPAttachmentPDF.addTimeStamp', false)
	def dateNow = new Date()
	String timeStamp = dateNow.format("yyyyMMdd_HHmmss_SSS")
	String messageID = message.getProperties().get('SAP_MessageProcessingLogID') as String
	String addMessageID = getExchangeProperty(message,'CreateSOAPAttachmentPDF.addMessageID', false)
	if (addMessageID == null || fileNameIn.length() == 0) {
		addMessageID = "true"
	}

	// Get body
	def body = message.getBody(java.lang.String) as String

	if (body.length() > 0) {
		// Compute attachment fileName
		String fileName = fileNameIn
		if ("true".equalsIgnoreCase(addTimeStamp) || "yes".equalsIgnoreCase(addTimeStamp)) {
			fileName += "_" + timeStamp
		}
		if ("true".equalsIgnoreCase(addMessageID) || "yes".equalsIgnoreCase(addMessageID)) {
			fileName += "_" + messageID
		}
		fileName += "." + ATTACHMENT_FILE_EXTENSION

		// Decode Base64-encoded PDF and get byte
		byte[] bodyByte = body.decodeBase64()

		// Set body and MIME type
		def dataSource = new ByteArrayDataSource(bodyByte, MIME_TYPE + "; name=" + fileName)

		// Add the attachment to the message
		def attachment = new DefaultAttachment(dataSource)
		message.addAttachmentObject(fileName, attachment)

		// Set attachment file name to property
		message.setProperty("AttachmentFileName", fileName)

		// Set attachment file name to custom header property
		def messageLog = messageLogFactory.getMessageLog(message)
		messageLog.addCustomHeaderProperty("AttachmentFileName", fileName)
	}

	return message
}

/**
 * getExchangeProperty
 * @param message This is message.
 * @param propertyName This is name of property.
 * @param mandatory This is parameter if property is mandatory.
 * @return propertyValue Return property value.
 */
private getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (propertyValue == null || propertyValue.length() == 0) {
			throw new Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}