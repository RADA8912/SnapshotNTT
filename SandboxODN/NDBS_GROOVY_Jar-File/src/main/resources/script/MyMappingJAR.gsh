import com.sap.gateway.ip.core.customdev.util.Message
import myMapping.*

def Message processData(Message message) {
	// Get body
	def body = message.getBody(java.lang.String)

    // Call methode from JAR-file
    MyDataMapping mdm = new MyDataMapping()
    body = mdm.getMyUpperCase(body)
    
    // Set body
	message.setBody(body)
    return message
}