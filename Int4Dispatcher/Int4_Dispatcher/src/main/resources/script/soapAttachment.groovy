import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler

def Message processData(Message message) {
   Map<String, DataHandler> attachments = message.getAttachments()
   if (!attachments.isEmpty()) {
      Iterator<DataHandler> it = attachments.values().iterator()
      DataHandler attachment = it.next()
      message.setBody(attachment.getContent())
   }
   return message
}