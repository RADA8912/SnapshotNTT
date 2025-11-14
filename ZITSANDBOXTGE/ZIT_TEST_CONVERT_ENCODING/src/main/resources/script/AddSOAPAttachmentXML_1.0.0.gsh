import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.activation.DataHandler
import org.apache.commons.codec.binary.Base64

/**
* AddSOAPAttachmentXML
* This Groovy script adds an XML-attachment in SOAP-message from payload.
*
* Groovy script parameters (exchange properties)
* - AddSOAPAttachmentXML.FileName = File name
* - AddSOAPAttachmentXML.AddTimeStamp = Add time stamp ('true', 'false', 'yes', 'no')
* - AddSOAPAttachmentXML.AddMessageID = Add message ID ('true', 'false', 'yes', 'no')
* - AddSOAPAttachmentXML.CreateFromExchangeProperty = Create Attachment from Excachge Property ('true', 'false', 'yes', 'no')
* - AddSOAPAttachmentXML.Base64EncodedProperty = Exchange Property for creating the attachment
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	final String DEFAULT_ATTACHMENT_FILE_NAME = "attachment"
	final String ATTACHMENT_FILE_EXTENSION = "xml"
	final String MIME_TYPE = "text/xml"

	// Get exchange properties
	String fileNameIn = getExchangeProperty(message,'AddSOAPAttachmentXML.FileName', false)
	if (fileNameIn == null || fileNameIn.length() == 0) {
		fileNameIn = DEFAULT_ATTACHMENT_FILE_NAME
	}
	String addTimeStamp = getExchangeProperty(message,'AddSOAPAttachmentXML.AddTimeStamp', false) as String
	def dateNow = new Date()
	String timeStamp = dateNow.format("yyyyMMdd_HHmmss_SSS")
	String messageID = message.getProperties().get('SAP_MessageProcessingLogID') as String
	String addMessageID = getExchangeProperty(message,'AddSOAPAttachmentXML.AddMessageID', false)
	if (addMessageID == null || fileNameIn.length() == 0) {
		addMessageID = "true"
	}

	// Get body
	def body = message.getBody(java.lang.String) as String

	if (body.length() > 0) {
		// Compute attachment fileName
		String fileName = createFileName(fileNameIn,timeStamp,addTimeStamp,addMessageID,messageID,ATTACHMENT_FILE_EXTENSION)

		// Set body and MIME type
		def dataHandler = new DataHandler(body, MIME_TYPE + "; name=" + fileName)

		// Add the attachment to the message
		def attachment = new DefaultAttachment(dataHandler)
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
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * createFileName
 * @param fileNameIn
 * @param timeStamp 
 * @param addTimeStamp
 * @param addMessageID
 * @param messageID This is the Id of the message.
 * @param ATTACHMENT_FILE_EXTENSION This is parameter if property is mandatory.
 * @return fileName Return fileName value.
 */
private createFileName(String fileNameIn, String timeStamp, String addTimeStamp, String addMessageID, String messageID, String ATTACHMENT_FILE_EXTENSION) {	 
	String fileName = fileNameIn
	if ("true".equalsIgnoreCase(addTimeStamp) || "yes".equalsIgnoreCase(addTimeStamp)) {
		fileName += "_" + timeStamp
	}
	if ("true".equalsIgnoreCase(addMessageID) || "yes".equalsIgnoreCase(addMessageID)) {
		fileName += "_" + messageID
	}
	fileName += "." + ATTACHMENT_FILE_EXTENSION

	return fileName
}