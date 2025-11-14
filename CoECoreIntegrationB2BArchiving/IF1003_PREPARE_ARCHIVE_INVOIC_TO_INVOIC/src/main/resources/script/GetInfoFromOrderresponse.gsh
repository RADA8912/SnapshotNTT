import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    
    // XML-Input einlesen
    def body = message.getBody(String)
   
	def pattern = ~/BGM\+([^+]+)\+([^+]+)\+/
	def matcher = (body =~ pattern)
	def documentNumber = ""
	
	if (matcher.find()) {
		documentNumber = matcher.group(2)
	} else {
		documentNumber = "123"
	}
   
	
    // TXT-Output setzen
	message.setHeader("Archive_LinkedSAPObjectKey", documentNumber)
    message.setHeader("Archive_Slug", documentNumber+".edi")
    message.setHeader("Archive_BusinessObjectTypeName","BUS2032")
    return message
}