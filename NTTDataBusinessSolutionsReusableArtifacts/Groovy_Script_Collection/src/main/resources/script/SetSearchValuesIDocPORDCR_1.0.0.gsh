import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesIDocPORDCR
* Set IDoc fields to header fields and log entries for search by IDoc number in message monitor.
* 'Purchasing Document Number (PO_NUMBER)', 'Purchasing Document Type (DOC_TYPE)', 'Vendor's account number (VENDOR)' are supported.
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
		String poNumber = root.IDOC.E1PORDCR1.E1BPMEPOHEADER.PO_NUMBER[0].text() // PORDCR1xx
		String docType = root.IDOC.E1PORDCR1.E1BPMEPOHEADER.DOC_TYPE[0].text() // PORDCR1xx
		String vendor = root.IDOC.E1PORDCR1.E1BPMEPOHEADER.VENDOR[0].text() // PORDCR1xx

		// Set values for search in header field
		message.setHeader('SAP_ApplicationID', docNum)
		message.setHeader('SAP_Sender', sender)
		message.setHeader('SAP_Receiver', receiver)
		message.setHeader('SAP_MessageType', messageType)

		// Set log entries
		messageLog.setStringProperty('SAP IDoc number (DOCNUM)', trimZeroLeft(docNum))
		if (poNumber.length() > 0) {
			messageLog.setStringProperty('Purchasing Document Number (PO_NUMBER)', trimZeroLeft(poNumber))
//			message.setProperty('Purchasing Document Number (PO_NUMBER)', trimZeroLeft(poNumber)) // Only for debug
		}
		if (docType.length() > 0) {
			messageLog.setStringProperty('Purchasing Document Type (DOC_TYPE)', trimZeroLeft(docType))
//			message.setProperty('Purchasing Document Type (DOC_TYPE)', trimZeroLeft(docType)) // Only for debug
		}		
		if (vendor.length() > 0) {
			messageLog.setStringProperty("Vendor's account number (VENDOR)", trimZeroLeft(vendor))
//			message.setProperty("Vendor's account number (VENDOR)", trimZeroLeft(vendor)) // Only for debug
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