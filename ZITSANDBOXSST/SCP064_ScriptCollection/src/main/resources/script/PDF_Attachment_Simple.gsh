import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import org.apache.camel.impl.DefaultAttachment
import javax.mail.util.ByteArrayDataSource

def Message processData(Message message) {
	//get message body
	def body = message.getBody()

	//covnert message body into bytes, mention correct MIME type
    byte[] bodyByte = body.decodeBase64()
//  byte[] bodyByte = body.getBytes() ohne base 64
    def dataSource = new ByteArrayDataSource(bodyByte, 'application/pdf')

    //create attachment object
    def attachment = new DefaultAttachment(dataSource)
    message.addAttachmentObject("Attachment.pdf", attachment)

    return message
}