import com.sap.gateway.ip.core.customdev.util.Message


def Message processData(Message message) {

	
	def value = message.getProperties().get("URL");
    String encodedUrl = URLEncoder.encode(value, "UTF-8");
    message.setProperty("URL", encodedUrl)
    
    
    def value1 = message.getProperties().get("URLDescription");
    String encodedUrlDescription = URLEncoder.encode(value1, "UTF-8");
    message.setProperty("URLDescription", encodedUrlDescription)
	
	
	return message
}

