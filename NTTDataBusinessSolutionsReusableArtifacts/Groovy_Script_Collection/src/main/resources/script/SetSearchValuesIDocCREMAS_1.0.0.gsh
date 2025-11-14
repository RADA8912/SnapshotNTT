import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesIDocCREMAS
* Set IDoc fields to header fields and log entries for search by IDoc number in message monitor.
* 'Account Number of Vendor or Creditor (LIFNR)', 'Name 1 (NAME1)', 'Name 2 (NAME2)' are supported.
* Only data of first IDoc will be used.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	if (message.getBodySize() > 0) {
		// Get body
		Reader reader = message.getBody(Reader)
		XmlSlurper slurper = new XmlSlurper()
		// Keep whitespaces
		slurper.keepIgnorableWhitespace = true
		def root = slurper.parse(reader)

		def messageLog = messageLogFactory.getMessageLog(message)
		def propertyMap = message.getProperties()

		// Get values from xml-body (payload)
		String sender = root.IDOC.EDI_DC40.SNDPRN[0].text()
		String receiver = root.IDOC.EDI_DC40.RCVPRN[0].text()
		String messageType = root.IDOC.EDI_DC40.MESTYP[0].text()
		String idocType = root.IDOC.EDI_DC40.IDOCTYP[0].text()
		String cimType = root.IDOC.EDI_DC40.CIMTYP[0].text()
		String docNum = root.IDOC.EDI_DC40.DOCNUM[0].text()
		String lifNr = root.IDOC.E1LFA1M.LIFNR[0].text() // CREMAS0x
		String name1 = root.IDOC.E1LFA1M.NAME1[0].text() // CREMAS0x
		String name2 = root.IDOC.E1LFA1M.NAME2[0].text() // CREMAS0x

		// Set values for search in header field
		message.setHeader('SAP_ApplicationID', docNum)
		message.setHeader('SAP_Sender', sender)
		message.setHeader('SAP_Receiver', receiver)
		message.setHeader('SAP_MessageType', messageType)

		// Set log entries
		messageLog.setStringProperty('SAP IDoc number (DOCNUM)', trimZeroLeft(docNum))
		if (lifNr.length() > 0) {
			messageLog.setStringProperty('Account Number of Vendor or Creditor (LIFNR)', trimZeroLeft(lifNr))
//			message.setProperty('Account Number of Vendor or Creditor (LIFNR)', trimZeroLeft(lifNr)) // Only for debug
		}
		if (name1.length() > 0) {
			messageLog.setStringProperty('Name 1 (NAME1)', name1)
//			message.setProperty('Name 1 (NAME1)', name1) // Only for debug
		}
		if (name2.length() > 0) {
			messageLog.setStringProperty('Name 2 (NAME2)', name2)
//			message.setProperty('Name 2 (NAME2)', name2) // Only for debug
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