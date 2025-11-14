import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesIDocEQUIPMENT
* Set IDoc fields to header fields and log entries for search by IDoc number in message monitor.
* 'Equipment Number (EQUIPMENT_INT)', 'Equipment Number (EQUIPMENT_EXT)', 'Description Of Technical Object (DESCRIPT)', 'Functional Location Label (READ_FLOC)' are supported.
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
		String equipmentInt = root.IDOC.E1BP_IEQM_EXTRACTOR.EQUIPMENT_INT[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		String equipmentExt = root.IDOC.E1BP_IEQM_EXTRACTOR.EQUIPMENT_EXT[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		String descript = root.IDOC.E1BP_IEQM_EXTRACTOR.DESCRIPT[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		String readFloc = root.IDOC.E1BP_IEQM_EXTRACTOR.READ_FLOC[0].text() // MDM_EQUIPMENT_SAVEREPLICA

		// Set values for search in header field
		message.setHeader('SAP_ApplicationID', docNum)
		message.setHeader('SAP_Sender', sender)
		message.setHeader('SAP_Receiver', receiver)
		message.setHeader('SAP_MessageType', messageType)

		// Set log entries
		messageLog.setStringProperty('SAP IDoc number (DOCNUM)', trimZeroLeft(docNum))
		if (equipmentInt.length() > 0) {
			messageLog.setStringProperty('Equipment Number (EQUIPMENT_INT)', trimZeroLeft(equipmentInt))
//			message.setProperty('Equipment Number (EQUIPMENT_INT)', trimZeroLeft(equipmentInt)) // Only for debug
		}
		if (equipmentExt.length() > 0) {
			messageLog.setStringProperty('Equipment Number (EQUIPMENT_EXT)', trimZeroLeft(equipmentExt))
//			message.setProperty('Equipment Number (EQUIPMENT_EXT)', trimZeroLeft(equipmentExt)) // Only for debug
		}
		if (descript.length() > 0) {
			messageLog.setStringProperty('Description Of Technical Object (DESCRIPT)', descript)
//			message.setProperty('Description Of Technical Object (DESCRIPT)', descript) // Only for debug
		}
		if (readFloc.length() > 0) {
			messageLog.setStringProperty('Functional Location Label (READ_FLOC)', readFloc)
//			message.setProperty('Functional Location Label (READ_FLOC)', readFloc) // Only for debug
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