import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.commons.codec.binary.StringUtils

def Message processData(Message message) {
	// Get body
	def rawString = message.getBody(java.lang.String) as String
	byte[] bytes = StringUtils.getBytesUtf16Le(rawString)
 
	String utf8EncodedString = StringUtils.newStringUtf8(bytes)

	// Set body
	message.setBody(utf8EncodedString)
	return message
}