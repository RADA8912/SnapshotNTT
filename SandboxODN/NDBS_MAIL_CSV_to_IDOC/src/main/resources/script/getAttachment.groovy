
import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler

def Message processData(Message message) {
    Map<String, DataHandler> attachments = message.getAttachments();
    if (attachments.isEmpty()) {
        // Handling of missing attachment goes here
    } else {
        //Handling of available attachments
        attachments.values().each{ attachment ->
            if(attachment.getContentType().toLowerCase().contains("csv")){
                message.setBody(attachment.getContent());
                message.setProperty("Filename", attachment.getName());
            }
        }
    }
    return message;
}