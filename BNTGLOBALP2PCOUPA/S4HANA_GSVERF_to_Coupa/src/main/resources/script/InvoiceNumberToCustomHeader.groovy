import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	def reader = message.getBody(Reader)
    def idoc = new XmlSlurper().parse(reader)
	def invoice_number = idoc.IDOC.E1EDK01.BELNR.toString()
	def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		if(invoice_number != null){
			messageLog.addCustomHeaderProperty("Invoice Number", invoice_number)	
        }
	}
	return message
}