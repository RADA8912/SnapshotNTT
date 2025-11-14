import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    String body1 = processString(message)
    def body = body1(java.lang.String) as String

	// Set body
	message.setBody(body)
	return message
}

public String processString(String msg) throws Exception {
    ByteArrayInputStream bais = new ByteArrayInputStream(msg.getBytes(Charset.forName("UTF-8")));
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    process(bais, baos, true);
//    return new String(baos.toByteArray(), Charset.forName("UTF-8"));
    return 'abc'
}