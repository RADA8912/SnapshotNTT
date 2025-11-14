import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesIDocMBGMCR
* Set IDoc fields to header fields and log entries for search by IDoc number in message monitor.
* 'Reference Document Number (REF_DOC_NO)', 'Transaction For Goods Movement (GM_CODE)' are supported.
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
		String refDocNo = root.IDOC.E1MBGMCR.E1BP2017_GM_HEAD_01.REF_DOC_NO[0].text() // MBGMCR0x
		String gmCode = root.IDOC.E1MBGMCR.E1BP2017_GM_CODE.GM_CODE[0].text() // MBGMCR0x

		// Set values for search in header field
		message.setHeader('SAP_ApplicationID', docNum)
		message.setHeader('SAP_Sender', sender)
		message.setHeader('SAP_Receiver', receiver)
		message.setHeader('SAP_MessageType', messageType)

		// Set log entries
		messageLog.setStringProperty('SAP IDoc number (DOCNUM)', trimZeroLeft(docNum))
		if (refDocNo.length() > 0) {
			messageLog.setStringProperty('Reference Document Number (REF_DOC_NO)', trimZeroLeft(refDocNo))
//			message.setProperty('Reference Document Number (REF_DOC_NO)', trimZeroLeft(refDocNo)) // Only for debug
		}
		if (gmCode.length() > 0) {
			messageLog.setStringProperty('Transaction For Goods Movement (GM_CODE)', gmCode)
//			message.setProperty('Transaction For Goods Movement (GM_CODE)', gmCode) // Only for debug
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