import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler

/**
* SetFirstAttachmentToBody
* This Groovy script sets a message-attachment to body payload.
*
* @author itelligence.de
* @version 1.0.0
*/

def Message processData(Message message) {
    
	// Get attachments
	Map<String, DataHandler> attachments = message.getAttachments()

	// Check if attaments are available
	if (!attachments.isEmpty()) {
//	    attachmentFileName = attachments.values()[0].getName()
//        java.lang.String attachmentFileName = "<Test.txt>"
		message.removeAttachment("<" + attachments.values()[0].getName() + ">")
	}

//    for (String name : (Set<String>)message.getAttachmentNames()) {
//        message.removeAttachment(name)
//    }

	return message
}