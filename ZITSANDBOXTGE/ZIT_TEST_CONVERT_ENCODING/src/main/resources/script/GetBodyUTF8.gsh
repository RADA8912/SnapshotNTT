import com.sap.gateway.ip.core.customdev.util.Message
import java.io.*
import java.nio.charset.Charset 

def Message processData(Message message) {
	try{
		// Get body
		byte[] bodyByteArray = message.getBody(byte[].class)
		String body = new String(bodyByteArray, Charset.forName('UTF-8'))

		// Set body
		message.setBody(body)
		return message
	} catch(Exception e) {
		e.printStackTrace()
		throw e
	}
}