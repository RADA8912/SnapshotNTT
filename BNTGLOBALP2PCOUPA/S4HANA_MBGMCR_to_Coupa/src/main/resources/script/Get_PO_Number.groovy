import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //initialize
    def reader = message.getBody(Reader)
    def MBGMCR04 = new XmlSlurper().parse(reader)
	
	//define variables
	def po_number = ""
	
	//set variables
	po_number = MBGMCR04.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.PO_NUMBER.toString().stripIndent(2).replaceFirst("^0*", "")

	//set properties
	message.setProperty("po_number", po_number)

	//return
    return message
}