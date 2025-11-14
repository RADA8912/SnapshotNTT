import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

//    def body = message.getBody(java.io.InputStream)

    def in = message.getBody(java.io.InputStream)


//	message.setBody(new String(in.toByteArray()))

	return message
}
