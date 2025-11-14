import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
	def url = message.getProperty("variocube_host") + "/" + message.getProperty("variocube_tenant")+ "/deliveries/" + message.getProperty("VC_deliveryId")
    
    String formDataPart1="------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"attachment[url]\"\r\n\r\n" + url + "\r\n"
	String formDataPart2="------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"attachment[type]\"\r\n\r\nurl\r\n"
	String formDataPart3="------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"attachment[intent]\"\r\n\r\ninternal\r\n"
	String formDataPart4="------WebKitFormBoundary7MA4YWxkTrZu0gW--"
	
    byte[]  formDataPart1Bytes = formDataPart1.getBytes()
    byte[]  formDataPart2Bytes = formDataPart2.getBytes()
	byte[]  formDataPart3Bytes = formDataPart3.getBytes()
	byte[]  formDataPart4Bytes = formDataPart4.getBytes()
	
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    outputStream.write(formDataPart1Bytes)
    outputStream.write(formDataPart2Bytes)
	outputStream.write(formDataPart3Bytes)
	outputStream.write(formDataPart4Bytes)
	
    message.setBody(outputStream)
	
	message.setHeader("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
	message.setHeader("Accept", "application/json")
	
    return message
}