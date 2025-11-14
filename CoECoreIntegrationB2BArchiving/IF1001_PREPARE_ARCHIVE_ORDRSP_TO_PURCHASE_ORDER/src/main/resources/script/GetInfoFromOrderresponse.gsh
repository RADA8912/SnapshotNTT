import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def log = messageLogFactory.getMessageLog(message)
	final String DEFAULT_DOCUMENT_NUMBER = "123"
	
    // XML-Input einlesen
    def body = message.getBody(String)
    // Suche nach 'LIN+<Wert1>+<Wert2>+<Dokumentnummer>:' im Text
	def pattern = ~/LIN\+([^+]+)\+([^+]+)\+([^+]+):/
	def matcher = (body =~ pattern)
	def documentNumber = DEFAULT_DOCUMENT_NUMBER
	 
	// Falls das Muster gefunden wird, extrahiere die Dokumentnummer (3. Gruppe)
	if (matcher.find()) {
		documentNumber = matcher.group(3)
	} 
	log.addCustomHeaderProperty("ExtractedDocumentNumber", documentNumber);
    // Setze Header für die Weiterverarbeitung
	message.setHeader("LinkedSAPObjectKey", documentNumber)
    message.setHeader("Slug", documentNumber+".edi")
    message.setHeader("BusinessObjectTypeName","BUS2012")
    return message
}