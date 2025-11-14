import com.sap.gateway.ip.core.customdev.util.Message
import java.nio.charset.StandardCharsets
import java.nio.charset.Charset

def Message processData(Message message) {
	// Get body
	String rawString = message.getBody(java.lang.String) as String
	byte[] bytes = rawString.getBytes(StandardCharsets.UTF_16BE)

	String utf8EncodedString = new String(bytes, StandardCharsets.UTF_8)

   	message.setBody(utf8EncodedString)

    return message
}