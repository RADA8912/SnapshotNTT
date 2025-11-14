import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler

/**
* GetFirstSOAPAttachment
* This Groovy script gets first attachment of SOAP-message and set it to message body.
* 
* Groovy script parameters (exchange properties)
* - GetFirstSOAPAttachment.propertyName = name of the property where the attachment should be added
* - GetFirstSOAPAttachment.addAttachmentToProperty = Add attachment to property ('true', 'false', 'yes', 'no')
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
	
	// Get properties
	String addAttachmentToProperty = getExchangeProperty(message,'GetFirstSOAPAttachment.addAttachmentToProperty',true)

	// Check if attachments are available
	if (!attachments.isEmpty()) {
	    
	    
	        // case 1
    	    if(!("true".equalsIgnoreCase(addAttachmentToProperty) || "yes".equalsIgnoreCase(addAttachmentToProperty))){
    		// Set first attachment to body
    		message.setBody(attachments.values()[0].getContent())
    	    }
    	    
    	    // case 2
    	    if("true".equalsIgnoreCase(addAttachmentToProperty) || "yes".equalsIgnoreCase(addAttachmentToProperty)){
            
            // Get first attachment and encode to base64 String
            def attachment = attachments.values()[0].getContent()
            String encodedAttachment = attachment.bytes.encodeBase64().toString()
            
            // Get the name for the attachment property and store the base64 encoded attachment as property
            String attachmentPropertyName = getExchangeProperty(message,'GetFirstSOAPAttachment.propertyName',true)
    		message.setProperty(attachmentPropertyName,encodedAttachment)
    	   
    	    }
    	    
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



private getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (propertyValue == null || propertyValue.length() == 0) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}