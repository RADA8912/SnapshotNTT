import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //initialize
    def reader = message.getBody(Reader)
    def idoc = new XmlSlurper().parse(reader)
	
	//define variables
	def po_number = ""
	
	//set variables
	po_number = idoc.order.BELNR.toString().stripIndent(2).replaceFirst("^0*", "")

	//set properties
	message.setProperty("po_number", po_number)

	//return
    return message
}