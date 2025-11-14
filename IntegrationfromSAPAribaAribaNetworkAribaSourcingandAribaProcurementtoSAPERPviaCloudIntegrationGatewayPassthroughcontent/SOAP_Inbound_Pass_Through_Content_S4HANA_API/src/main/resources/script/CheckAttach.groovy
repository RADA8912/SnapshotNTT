import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.gateway.ip.core.customdev.util.AttachmentWrapper
import java.util.Map
import javax.activation.DataHandler
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource
import groovy.xml.*

def Message processData(Message message) {

    Map<String, DataHandler> attachments = message.getAttachments()
    if (!attachments.isEmpty()) {
        message.setHeader("hasAttachments", "YES")
    }

    return message
}
