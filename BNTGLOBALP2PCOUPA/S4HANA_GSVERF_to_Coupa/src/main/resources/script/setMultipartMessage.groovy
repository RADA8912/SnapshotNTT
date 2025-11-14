import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    byte[] fileContent = message.getProperty("base64_invoice").decodeBase64()
	def filename = message.getProperty("invoice_number_prefix_written").replace(' ', '_') + "_" + message.getProperty("invoice_number_prefix" )+ message.getProperty("invoice_number") + "_" + message.getProperty("invoice_date") + ".pdf"
    
    String formDataPart1="------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"attachment[file]\"; filename=\"" +filename+ "\"\r\nContent-Type: application/pdf\r\n\r\n"
    String formDataPart2="\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"attachment[type]\"\r\n\r\nfile\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--"	
    byte[]  formDataPart1Bytes = formDataPart1.getBytes()
    byte[]  formDataPart2Bytes = formDataPart2.getBytes()
	
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    outputStream.write(formDataPart1Bytes)
    outputStream.write(fileContent)
    outputStream.write(formDataPart2Bytes)
	
    message.setBody(outputStream)
	
	message.setHeader("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
	message.setHeader("Accept", "application/json")
	
    return message
}
