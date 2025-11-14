import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.io.*

def Message processData(Message message) {
    def body = message.getBody(java.io.Reader)
    def xml = new XmlSlurper().parse(body)
	def messageLog = messageLogFactory.getMessageLog(message)
	def propertyMap = message.getProperties()
	
	String docNum = ""
	String sender = ""
	String receiver = ""
	String messageType = ""

	//Get values from xml-body (payload)
	docNum = xml.IDOC.EDI_DC40.DOCNUM.text()
	sender = xml.IDOC.EDI_DC40.SNRPRN.text()
	receiver = xml.IDOC.EDI_DC40.RCVPRN.text()
	messageType = xml.IDOC.EDI_DC40.MESTYP.text()
	
	//Set values for search in header field
	message.setHeader("SAP_ApplicationID", docNum)
	message.setHeader("SAP_MessageType", messageType)
	
	//Set log entries
	messageLog.setStringProperty("SAP IDoc number (DOCNUM)", docNum)
	messageLog.setStringProperty("SAP Sender (SNRPRN)", sender)
	messageLog.setStringProperty("SAP Receiver (RCVPRN)", receiver)
	messageLog.setStringProperty("SAP Message type (MESTYP)", messageType)

	return message
}