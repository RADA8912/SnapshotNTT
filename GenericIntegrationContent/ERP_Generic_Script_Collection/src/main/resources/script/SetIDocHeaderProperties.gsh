import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.io.*

/**
 * Set IDoc header properties
 */
def Message processData(Message message) {
    def body = message.getBody(java.io.Reader)
    def xml = new XmlSlurper().parse(body)
	def messageLog = messageLogFactory.getMessageLog(message)
	def propertyMap = message.getProperties()
	String sender = ""
	String receiver = ""
	String idocType = ""
	String messageType = ""

	//Get values from xml-body (payload)
	sender = xml.IDOC[0].EDI_DC40.SNDPRN.text()
	receiver = xml.IDOC[0].EDI_DC40.RCVPRN.text()
	idocType = xml.IDOC[0].EDI_DC40.IDOCTYP.text()
	messageType = xml.IDOC[0].EDI_DC40.MESTYP.text()
	
	//Set values for search in header field
	message.setHeader("SAP_Sender", sender)
	message.setHeader("SAP_Receiver", receiver)
	message.setHeader("SAP_IDocType", idocType)	
	message.setHeader("SAP_MessageType", messageType)
	
	//Set log entries
	messageLog.setStringProperty("SAP Sender (SNRPRN)", sender)
	messageLog.setStringProperty("SAP Receiver (RCVPRN)", receiver)
	messageLog.setStringProperty("SAP IDoc type (IDOCTYP)", idocType)
	messageLog.setStringProperty("SAP Message type (MESTYP)", messageType)

	//Set custom header properties for message log API

	
	messageLog.addCustomHeaderProperty("3 SAP Message type (MESTYP)", messageType)
    messageLog.addCustomHeaderProperty("4 SAP IDoc type (IDOCTYP)", idocType)
    messageLog.addCustomHeaderProperty("2 SAP Receiver (RCVPRN)", receiver)
    messageLog.addCustomHeaderProperty("1 SAP Sender (SNRPRN)", sender)
	
	return message
}