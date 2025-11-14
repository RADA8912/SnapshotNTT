import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.camel.impl.DefaultAttachment
import javax.activation.DataHandler
import org.apache.commons.codec.binary.Base64

/**
* CreateSOAPAttachmentXML
* This Groovy script creates an XML-attachment in SOAP-message from payload.
*
* Groovy script parameters (exchange properties)
* - CreateSOAPAttachmentXML.fileName = File name
* - CreateSOAPAttachmentXML.addTimeStamp = Add time stamp ('true', 'false', 'yes', 'no')
* - CreateSOAPAttachmentXML.addMessageID = Add message ID ('true', 'false', 'yes', 'no')
* - CreateSOAPAttachmentXML.createFromExchangeProperty = Create Attachment from Excachge Property ('true', 'false', 'yes', 'no')
* - CreateSOAPAttachmentXML.base64EncodedProperty = Exchange Property for creating the Attachment
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	final String DEFAULT_ATTACHMENT_FILE_NAME = "attachment"
	final String ATTACHMENT_FILE_EXTENSION = "xml"
	final String MIME_TYPE = "text/xml"

	// Get exchange properties
	String fileNameIn = getExchangeProperty(message,'CreateSOAPAttachmentXML.fileName', false)
	if (fileNameIn == null || fileNameIn.length() == 0) {
		fileNameIn = DEFAULT_ATTACHMENT_FILE_NAME
	}
	String addTimeStamp = getExchangeProperty(message,'CreateSOAPAttachmentXML.addTimeStamp', false) as String
	def dateNow = new Date()
	String timeStamp = dateNow.format("yyyyMMdd_HHmmss_SSS")
	String messageID = message.getProperties().get('SAP_MessageProcessingLogID') as String
	String addMessageID = getExchangeProperty(message,'CreateSOAPAttachmentXML.addMessageID', false)
	if (addMessageID == null || fileNameIn.length() == 0) {
		addMessageID = "true"
	}
	
	
	
	//create attachment from base64 encoded exchange property if createFromExchangeProperty is true
    String createFromExchangeProperty = getExchangeProperty(message,'CreateSOAPAttachmentXML.createFromExchangeProperty', false)

    if("true".equalsIgnoreCase(createFromExchangeProperty) || "yes".equalsIgnoreCase(createFromExchangeProperty)){

        String base64EncodedProperty = getExchangeProperty(message,'CreateSOAPAttachmentXML.base64EncodedProperty', true)
        
        if (Base64.isBase64(base64EncodedProperty)) {
		byte[] decoded = base64EncodedProperty.decodeBase64()
		decodedAttachmentProperty = new String(decoded)
	    }

        String fileName = createFileName(fileNameIn,timeStamp,addTimeStamp,addMessageID,messageID,ATTACHMENT_FILE_EXTENSION)
        
        
        
         message.setProperty("base64AttachmentTest", decodedAttachmentProperty)

        // Set body and MIME type
        def dataHandler = new DataHandler(decodedAttachmentProperty, MIME_TYPE + "; name=" + fileName)

        // Add the attachment to the message
        def attachment = new DefaultAttachment(dataHandler)
        message.addAttachmentObject(fileName, attachment)

        // Set attachment file name to property
        message.setProperty("AttachmentFileName", fileName)

        // Set attachment file name to custom header property
        def messageLog = messageLogFactory.getMessageLog(message)
        messageLog.addCustomHeaderProperty("AttachmentFileName", fileName)
        println(decodedAttachmentProperty)

        return message
    
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