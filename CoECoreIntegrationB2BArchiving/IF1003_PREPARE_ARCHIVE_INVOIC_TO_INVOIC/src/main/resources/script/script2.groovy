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

    //def patternDTM = ~/DTM\+137:([^+]+):/
    def patternDTM = /DTM\+137:(\d{4})\d{4}:/ 
    
	def matcherDTM = (body =~ patternDTM)
	def fscYear = ""
	
	if (matcherDTM.find()) {
		fscYear = matcherDTM.group(1)
	} else {
		fscYear = "123"
	}
   
	
    // TXT-Output setzen
	message.setHeader("LinkedSAPObjectKey", "1341"+documentNumber+fscYear)
    message.setHeader("Slug", documentNumber+".edi")
    message.setHeader("BusinessObjectTypeName","BKPF")
    return message
}