import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesIDocSHPMNT
* Set IDoc fields to header fields and log entries for search by IDoc number in message monitor.
* 'Shipment Number (TKNUM)' is supported.
* Only data of first IDoc will be used.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	// Get body
	def body = message.getBody(java.lang.String) as String

	if (body.length() > 0) {
		def xml = new XmlSlurper().parseText(body)
		def messageLog = messageLogFactory.getMessageLog(message)
		def propertyMap = message.getProperties()

		// Get values from xml-body (payload)
		String sender = xml.IDOC.EDI_DC40.SNDPRN[0].text()
		String receiver = xml.IDOC.EDI_DC40.RCVPRN[0].text()
		String messageType = xml.IDOC.EDI_DC40.MESTYP[0].text()
		String idocType = xml.IDOC.EDI_DC40.IDOCTYP[0].text()
		String cimType = xml.IDOC.EDI_DC40.CIMTYP[0].text()
		String docNum = xml.IDOC.EDI_DC40.DOCNUM[0].text()
		String tkNum = xml.IDOC.E1EDT20.TKNUM[0].text() // SHPMNT0x

		// Set values for search in header field
		message.setHeader('SAP_ApplicationID', docNum)
		message.setHeader('SAP_Sender', sender)
		message.setHeader('SAP_Receiver', receiver)
		message.setHeader('SAP_MessageType', messageType)

		// Set log entries
		messageLog.setStringProperty('SAP IDoc number (DOCNUM)', trimZeroLeft(docNum))
		if (tkNum.length() > 0) {
			messageLog.setStringProperty('Shipment Number (TKNUM)', trimZeroLeft(tkNum))
//			message.setProperty('Shipment Number (TKNUM)', trimZeroLeft(tkNum)) // Only for debug
		}
		messageLog.setStringProperty('SAP Sender (SNDPRN)', sender)
		messageLog.setStringProperty('SAP Receiver (RCVPRN)', receiver)
		messageLog.setStringProperty('SAP Message type (MESTYP)', messageType)
		messageLog.setStringProperty('SAP IDoc type (IDOCTYP)', idocType)
		messageLog.setStringProperty('SAP CIM type (CIMTYP)', cimType)
	}

	return message
}

/**
 * Removes leading zeros.
 * Execution mode: Single value 
 *
 * @param value Value
 * @return input number without leading zeros.
 */
private def String trimZeroLeft(String value) {
	String output = ""

	if (value != null) {
		if (value.length() == 0) {
			output = value
		} else {
			output = value.replaceAll("^0*", "")
						.replaceAll(" ", "")
			if (output.length() == 0) {
				output = "0"
			}
		}
	} else {
		output = value
	}

	return output
}