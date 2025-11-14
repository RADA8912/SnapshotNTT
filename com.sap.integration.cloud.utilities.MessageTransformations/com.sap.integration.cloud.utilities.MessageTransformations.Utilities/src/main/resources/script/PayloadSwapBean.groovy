import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.Map;
import javax.activation.DataHandler;

//PayloadSwapBean
//Version 1.0.0

def Message processData(Message message) {
    Map<String, Object> props = message.getProperties();
    String keyName = props.get("swap.keyName").toUpperCase();
    String keyValue = props.get("swap.keyValue").toUpperCase();
    Map<String, DataHandler> attachments = message.getAttachments();

    if (attachments.isEmpty()) {
        message.setProperty("Attachment_Count", "0");
    } else {
        message.setProperty('Attachment_Count', attachments.size());

        for (int i = 0; i < attachments.size(); i++){
            DataHandler attachment = attachments.values()[i];
            if (keyName == 'PAYLOAD-NAME'){
                if (attachment.getName() != null){
                    if (attachment.getName().toUpperCase().contains(keyValue)) {
                        message = handleAttachmentMatch(message, attachment);
                        break;
                    }
                }
                else {
                    //attachment name is not available, usually for XI
                    //throw new IllegalArgumentException("The attachment name is not available in this message.")
                }
            }
            else if (keyName == 'PAYLOAD-CONTENT-TYPE'){
                if(attachment.getContentType() != null && attachment.getContentType().toUpperCase().contains(keyValue)){
                    message = handleAttachmentMatch(message, attachment);
                    break;
                }
            }
        }
    }

    return message;
}

private static Message handleAttachmentMatch(Message message, DataHandler attachment){
    message.setBody(attachment.getContent());
    message.setProperty('Attachment_Name', attachment.getName());
    message.setProperty('Attachment_ContentType', attachment.getContentType());
    message.setHeader('Content-Type', attachment.getContentType());
    return message;
}
