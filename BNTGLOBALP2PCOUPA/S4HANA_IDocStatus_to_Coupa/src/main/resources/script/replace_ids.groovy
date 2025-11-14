import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def ponumberlong = message.getProperty("s4_po_no")
	def ponumber = message.getProperty("s4_po_no").replaceFirst("^0*", "").stripIndent(2).replaceFirst("^0*", "")
	message.setProperty("s4_po_no", ponumber)

	def grnumber = message.getProperty("s4_gr_no").replace("Coupa ID: ","")
	message.setProperty("s4_gr_no", grnumber)
    
    //set Custom Header
    def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		if(ponumberlong != null){
			messageLog.addCustomHeaderProperty("Coupa PO Number", ponumberlong)	
        }
        if(grnumber != null){
			messageLog.addCustomHeaderProperty("Coupa GR Number", grnumber)	
        }
	}
    
	return message
}