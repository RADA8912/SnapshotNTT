import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetEDIFACTHeaderProperties
* This Groovy script set EDIFACT header values to message log.
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
		String messageNo = ""
		String messageType = ""
		String sender = ""
		String receiver = ""

		// Get body
		Reader reader = message.getBody(Reader)
		XmlSlurper slurper = new XmlSlurper()
		// Keep whitespaces
		slurper.keepIgnorableWhitespace = true
		def root = slurper.parse(reader)

		def messageLog = messageLogFactory.getMessageLog(message)
		def propertyMap = message.getProperties()

		//Get values from xml-body (payload)
		messageNo = root.M_ORDERS.S_BGM.D_1004.text()
		messageType = root.M_ORDERS.S_UNH.C_S009.D_0065.text() + " " + root.M_ORDERS.S_UNH.C_S009.D_0052.text() + root.M_ORDERS.S_UNH.C_S009.D_0054.text() + " " + root.M_ORDERS.S_UNH.C_S009.D_0057.text()
		sender = root.S_UNB.C_S002.D_0004.text()
		receiver = root.S_UNB.C_S003.D_0010.text()
		
		//Set values for search in header field
		message.setHeader("SAP_ApplicationID", messageNo)
		message.setHeader("SAP_MessageType", messageType)
		message.setHeader("SAP_Sender", sender)
		message.setHeader("SAP_Receiver", receiver)

		//Set log entries
		messageLog.setStringProperty("EDIFACT Message number", messageNo)
		messageLog.setStringProperty("EDIFACT Message type", messageType)
		messageLog.setStringProperty("EDIFACT Sender", sender)
		messageLog.setStringProperty("EDIFACT Receiver", receiver)

		//Set custom header properties for message log API
		messageLog.addCustomHeaderProperty("EDIFACT Message number", messageNo)
		messageLog.addCustomHeaderProperty("EDIFACT Message type", messageType)
		messageLog.addCustomHeaderProperty("EDIFACT Sender", sender)
		messageLog.addCustomHeaderProperty("EDIFACT Receiver", receiver)
	}

	return message
}