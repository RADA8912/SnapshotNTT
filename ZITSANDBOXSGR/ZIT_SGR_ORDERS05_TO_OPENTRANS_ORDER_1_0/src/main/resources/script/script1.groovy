import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.io.*;
def Message processData(Message message) {
//	def body = message.getBody(java.lang.String)


    def body = message.getBody(java.io.Reader)
    def xml = new XmlSlurper().parse(body)
	def messageLog = messageLogFactory.getMessageLog(message)
	def propertyMap = message.getProperties()
	String sender = ""
	String receiver = ""
	String messageType = ""
	String docNum = ""
	String belNr = ""

	//Get values from xml-body (payload)
	sender = xml.IDOC.EDI_DC40.SNRPRN.text()
	receiver = xml.IDOC.EDI_DC40.RCVPRN.text()
	messageType = xml.IDOC.EDI_DC40.MESTYP.text()
	docNum = xml.IDOC.EDI_DC40.DOCNUM.text()
	belNr = xml.IDOC.E1EDK01.BELNR.text()
	
	//Set values for search in header field
	message.setHeader("SAP_ApplicationID", docNum)
	message.setHeader("SAP_Sender", docNum)
	message.setHeader("SAP_Receiver", docNum)
	message.setHeader("SAP_MessageType", docNum)
	
	//Set log entries
	messageLog.setStringProperty("SAP IDoc number (DOCNUM)", docNum)
	messageLog.setStringProperty("SAP Document number (BELNR)", belNr)
	messageLog.setStringProperty("SAP Sender (SNRPRN)", sender)
	messageLog.setStringProperty("SAP Receiver (RCVPRN)", receiver)
	messageLog.setStringProperty("SAP Message type (MESTYP)", messageType)

	return message;
}