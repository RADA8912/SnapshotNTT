import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

// Remove header lines from CSV
def Message processData(Message message){
	def body = message.getBody(String.class)

	//Set Header
	message.setHeader("EffectiveDate", body.substring(0,11))

	//Remove 2 header lines
	body = body.substring(body.indexOf('\n')+1)
	body = body.substring(body.indexOf('\n')+1)

	message.setBody(body)
	return message
}