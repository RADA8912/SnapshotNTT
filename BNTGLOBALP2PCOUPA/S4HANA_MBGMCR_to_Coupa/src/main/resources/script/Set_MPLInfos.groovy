import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	// Variables
	def idocPayload = message.getBody(Reader)
    def input = new XmlSlurper().parse(idocPayload)
	
	//set MPL-Infos
    message.setHeader("SAP_Sender", "S4")
    message.setHeader("SAP_Receiver", "Coupa")
    message.setHeader("SAP_MessageType", "MBGMCR")
    message.setHeader("SAP_ApplicationID", input.IDOC.EDI_DC40.DOCNUM)
	input.IDOC.E1MBGMCR.E1BP2017_GM_ITEM_CREATE.each{ item ->
		def messageLog = messageLogFactory.getMessageLog(message)
		if(messageLog != null){
			if(item.GR_NUMBER != null && !item.GR_NUMBER.toString().equals("")){
				messageLog.addCustomHeaderProperty("GR Number", item.GR_NUMBER.toString())	
			}
			if(item.PO_NUMBER != null && !item.PO_NUMBER.toString().equals("")){
				messageLog.addCustomHeaderProperty("PO Number", item.PO_NUMBER.toString())	
			}
		}
	}
	
	//return
    return message
}
