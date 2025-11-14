import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //initialize
    def payload = message.getBody(String)
    def idoc = new XmlSlurper(false, false).parseText(payload)
	
	//get IDoc values
	def idoc_CostCenter = ""
	def idoc_DescriptionENShort = ""
	def idoc_DescriptionENLong = ""
	def idoc_CompanyCode = ""
	def idoc_Active = "true"
	def idoc_smtp_addr = ""
	
	idoc_CostCenter = idoc.IDOC.E1CSKSM.KOSTL.toString().replaceFirst("^0*", "")
	idoc_DescriptionENShort = idoc.IDOC.E1CSKSM.E1CSKTM.find{it.SPRAS_ISO == "EN"}.KTEXT.toString()
	idoc_DescriptionENLong = idoc.IDOC.E1CSKSM.E1CSKTM.find{it.SPRAS_ISO == "EN"}.LTEXT.toString()
	idoc_CompanyCode = idoc.IDOC.E1CSKSM.BUKRS.toString()
	if(idoc.IDOC.E1CSKSM.BKZKP.toString().equals("X") || idoc.IDOC.E1CSKSM.ZE1CSKSM.ZINACTIVE.toString().equals("X")) {
		idoc_Active = "false"
	}
	idoc_smtp_addr = idoc.IDOC.E1CSKSM.ZE1CSKSM.SMTP_ADDR.toString()
			
	//define Coupa values
	def coupa_Name = idoc_DescriptionENShort + " (" + idoc_CostCenter + ")"
	def coupa_Active = idoc_Active
	def coupa_Description = idoc_DescriptionENLong
	def coupa_ExternalRefNum = idoc_CostCenter
	def coupa_ExternalRefCode = idoc_CompanyCode + "|" + idoc_CostCenter
	def coupa_Default = "false"
	def coupa_ParentExternalRefCode = idoc_CompanyCode
	def coupa_Owner = idoc_smtp_addr
	def coupa_NameForQueryCoupaID = idoc_CompanyCode + "_Comp_CC"
	def coupa_ExternalRefCodeForQueryCoupaID = idoc_CompanyCode
	
	//set properties
    message.setProperty("coupa_Name", coupa_Name)
	message.setProperty("coupa_Active", coupa_Active)
	message.setProperty("coupa_Description", coupa_Description)
	message.setProperty("coupa_ExternalRefNum", coupa_ExternalRefNum)
	message.setProperty("coupa_ExternalRefCode", coupa_ExternalRefCode)
	message.setProperty("coupa_Default", coupa_Default)
	message.setProperty("coupa_ParentExternalRefCode", coupa_ParentExternalRefCode)
	message.setProperty("coupa_Owner", coupa_Owner)
	message.setProperty("coupa_NameForQueryCoupaID", coupa_NameForQueryCoupaID)
	message.setProperty("coupa_ExternalRefCodeForQueryCoupaID", coupa_ExternalRefCodeForQueryCoupaID)
    message.setProperty("idoc_CompanyCode", idoc_CompanyCode)
    
    //set MPL-Infos
    message.setHeader("SAP_Sender", "S4")
    message.setHeader("SAP_Receiver", "Coupa")
    message.setHeader("SAP_MessageType", "COSMAS")
    message.setHeader("SAP_ApplicationID", idoc.IDOC.EDI_DC40.DOCNUM)
    def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		if(idoc_CostCenter != null){
			messageLog.addCustomHeaderProperty("CostCenter", idoc_CostCenter)	
        }
	}
	
	//return
    return message
}