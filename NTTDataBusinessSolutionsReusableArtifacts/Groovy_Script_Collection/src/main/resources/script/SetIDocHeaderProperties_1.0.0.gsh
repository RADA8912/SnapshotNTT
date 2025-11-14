import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetIDocHeaderProperties
* This Groovy script set IDoc header values to message log.
* "StringProperty" can easy read in message log.
* "CustomHeaderProperty" can request via CPI API.
*
* Groovy script parameters:
* - There are no parameters.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	if (message.getBodySize() > 0) {
		String sender = ""
		String receiver = ""
		String idocType = ""
		String messageType = ""
		String docNum = ""
		String belNr = ""
		
		// Get body
		Reader reader = message.getBody(Reader)
		XmlSlurper slurper = new XmlSlurper()
		// Keep whitespaces
		slurper.keepIgnorableWhitespace = true
		def root = slurper.parse(reader)

		def messageLog = messageLogFactory.getMessageLog(message)
		def propertyMap = message.getProperties()

		//Get values from xml-body (payload)
		sender = root.IDOC.EDI_DC40.SNDPRN.text()
		receiver = root.IDOC.EDI_DC40.RCVPRN.text()
		idocType = root.IDOC.EDI_DC40.IDOCTYP.text()
		messageType = root.IDOC.EDI_DC40.MESTYP.text()
		docNum = root.IDOC.EDI_DC40.DOCNUM.text()
		belNr = root.IDOC.E1EDK01.BELNR.text()
		
		//Set values for search in header field
		message.setHeader("SAP_ApplicationID", docNum)
		message.setHeader("SAP_Sender", sender)
		message.setHeader("SAP_Receiver", receiver)
		message.setHeader("SAP_MessageType", messageType)
		
		//Set log entries
		messageLog.setStringProperty("SAP IDoc number (DOCNUM)", docNum)
		messageLog.setStringProperty("SAP Document number (BELNR)", belNr)
		messageLog.setStringProperty("SAP Sender (SNRPRN)", sender)
		messageLog.setStringProperty("SAP Receiver (RCVPRN)", receiver)
		messageLog.setStringProperty("SAP IDoc type (IDOCTYP)", idocType)
		messageLog.setStringProperty("SAP Message type (MESTYP)", messageType)

		//Set custom header properties for message log API
		messageLog.addCustomHeaderProperty("SAP IDoc number (DOCNUM)", docNum)
		messageLog.addCustomHeaderProperty("SAP Document number (BELNR)", belNr)
		messageLog.addCustomHeaderProperty("SAP Sender (SNRPRN)", sender)
		messageLog.addCustomHeaderProperty("SAP Receiver (RCVPRN)", receiver)
		messageLog.addCustomHeaderProperty("SAP IDoc type (IDOCTYP)", idocType)
		messageLog.addCustomHeaderProperty("SAP Message type (MESTYP)", messageType)
	}

	return message
}