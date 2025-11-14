import com.sap.gateway.ip.core.customdev.util.Message
import javax.activation.DataHandler
import com.sap.gateway.ip.core.customdev.util.AttachmentWrapper


/**
* RemoveAllAtachments
* This Groovy script removes all attachments of the incoming message
* 
*
* @author nttdata-solutions.com
* @version 1.0.0
*/


def Message processData(Message message) {
    
        
        //get the attachments from the message
        Map<String, DataHandler> attachments = message.getAttachments()
        Map<String, AttachmentWrapper> attachmentWrappers = message.getAttachmentWrapperObjects()
        
        //check if attachments empty
        if (!attachments.isEmpty()) {
            
        //remove the attachments from the message
        attachmentWrappers.clear()
        attachments.clear()
    
        }
        
    return message
    
}