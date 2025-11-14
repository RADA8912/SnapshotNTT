import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesIDoc
* Set IDoc fields to header fields and log entries for search by IDoc number in message monitor.
* 'Document number' for following IDoc types are supported: 
*  - /SCWM/WMTOID0*
*  - ACC_DOCUMENT0x
*  - BATMAS0x
*  - BOMMAT0x
*  - BUPA_INBOUND_MAIN_SAVE_M0x
*  - COND_A0x
*  - CREMAS0x
*  - DEBMAS0x
*  - DELFOR0x
*  - DELVRY0x
*  - GSVERF0x
*  - INFREC0x
*  - INTERNAL_ORDER_CREATE0x
*  - INVOIC0x
*  - LOIPRO0x
*  - MATMAS0x
*  - MBGMCR0x
*  - ORDERS0x
*  - PEXR200x
*  - PORDCR1xx
*  - PROJECT2x
*  - SALESORDER_CREATEFROMDAT2xx
*  - SHPMNT0x
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
		String belNr = root.IDOC.E1EDK01.BELNR[0].text() // GSVERF0x, INVOIC0x, ORDERS0x, ...
		String bsArt = root.IDOC.E1EDK01.BSART[0].text() // GSVERF0x, INVOIC0x, ORDERS0x, ...
		String vtrNr = root.IDOC.E1EDK09.VTRNR[0].text() // DELFOR0x
		String labNk = root.IDOC.E1EDK09.LABNK[0].text() // DELFOR0x
		String vBelN = root.IDOC.E1EDL20.VBELN[0].text() // DELVRY0x
		String tkNum = root.IDOC.E1EDT20.TKNUM[0].text() // SHPMNT0x
		String bgmRef = root.IDOC.E1IDKU1.BGMREF[0].text() // PEXR200x
		String refDocNo = root.IDOC.E1BPACHE09.REF_DOC_NO[0].text() // ACC_DOCUMENT0x
		String order = root.IDOC.E1BP2075_7.ORDER[0].text() // INTERNAL_ORDER_CREATE0x
		String poNumber = root.IDOC.E1PORDCR1.E1BPMEPOHEADER.PO_NUMBER[0].text() // PORDCR1xx
		String docType = root.IDOC.E1PORDCR1.E1BPMEPOHEADER.DOC_TYPE[0].text() // PORDCR1xx
		String vendor = root.IDOC.E1PORDCR1.E1BPMEPOHEADER.VENDOR[0].text() // PORDCR1xx		
		String purchNoC = root.IDOC.E1SALESORDER_CREATEFROMDAT2.E1BPSDHD1.PURCH_NO_C[0].text() // SALESORDER_CREATEFROMDAT2xx
		String matNr = root.IDOC.E1MARAM.MATNR[0].text() // MATMAS0x
		String matNrLong = root.IDOC.E1MARAM.MATNR_LONG[0].text() // MATMAS
		String maKtx = root.IDOC.E1MARAM.E1MAKTM[0].MAKTX[0].text() // MATMAS
		String lifNr = root.IDOC.E1LFA1M.LIFNR[0].text() // CREMAS0x
		String name1 = root.IDOC.E1LFA1M.NAME1[0].text() // CREMAS0x
		String name2 = root.IDOC.E1LFA1M.NAME2[0].text() // CREMAS0x
		String kunNr = root.IDOC.E1KNA1M.KUNNR[0].text() // DEBMAS0x
		if (name1.length() == 0) {
			name1 = root.IDOC.E1KNA1M.NAME1[0].text() // DEBMAS0x
		}
		if (name2.length() == 0) {
			name2 = root.IDOC.E1KNA1M.NAME2[0].text() // DEBMAS0x
		}
		String bPartner = root.IDOC.E101BUS_EI_EXTERN.E101BUS_EI_HEADER.E101BUS_EI_INSTANCE.BPARTNER[0].text() // BUPA_INBOUND_MAIN_SAVE_M0x
		String bPartnerGuid = root.IDOC.E101BUS_EI_EXTERN.E101BUS_EI_HEADER.E101BUS_EI_INSTANCE.BPARTNERGUID[0].text() // BUPA_INBOUND_MAIN_SAVE_M0x
		if (name1.length() == 0) {
			name1 = root.IDOC.E101BUS_EI_EXTERN.E101BUS_EI_CENTRAL_DATA.E101BUS_EI_BUPA_CENTRAL.E101US_EI_BUPA_CENTRAL_DATA.E101_EI_STRUC_CENTRAL_ORGAN.NAME1[0].text() // BUPA_INBOUND_MAIN_SAVE_M0x
		}
		if (name2.length() == 0) {
			name2 = root.IDOC.E101BUS_EI_EXTERN.E101BUS_EI_CENTRAL_DATA.E101BUS_EI_BUPA_CENTRAL.E101US_EI_BUPA_CENTRAL_DATA.E101_EI_STRUC_CENTRAL_ORGAN.NAME2[0].text() // BUPA_INBOUND_MAIN_SAVE_M0x
		}
		if (matNr.length() == 0) {
			matNr = root.IDOC.E1KOMG.MATNR[0].text() // COND_A0x
		}
		if (lifNr.length() == 0) {
			lifNr = root.IDOC.E1KOMG.LIFNR[0].text() // COND_A0x
		}
		if (matNrLong.length() == 0) {
			matNrLong = root.IDOC.E1KOMG.MATNR_LONG[0].text() // COND_A0x
		}
		String aufNr = root.IDOC.E1AFKOL.AUFNR[0].text() // LOIPRO0x
		String infNr = root.IDOC.E1EINAM.INFNR[0].text() // INFREC0x
		if (matNr.length() == 0) {
			matNr = root.IDOC.E1EINAM.MATNR[0].text() // INFREC0x
		}
		if (lifNr.length() == 0) {
			lifNr = root.IDOC.E1EINAM.LIFNR[0].text() // INFREC0x
		}
		String projectDefinition = root.IDOC.E1BP2054_PROJDEFINITION.PROJECT_DEFINITION[0].text() // PROJECT2x
		if (matNr.length() == 0) {
			matNr = root.IDOC.E1BATMAS.MATERIAL[0].text() // BATMAS0x
		}
		String materialLong = root.IDOC.E1BATMAS.MATERIAL_LONG[0].text() // BATMAS0x
		String batch = root.IDOC.E1BATMAS.BATCH[0].text() // BATMAS0x
		if (matNr.length() == 0) {
			matNr = root.IDOC.E1STZUM.E1MASTM.MATNR[0].text() // BOMMAT0x
		}
		String equipmentInt = root.IDOC.E1BP_IEQM_EXTRACTOR.EQUIPMENT_INT[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		String equipmentExt = root.IDOC.E1BP_IEQM_EXTRACTOR.EQUIPMENT_EXT[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		String descript = root.IDOC.E1BP_IEQM_EXTRACTOR.DESCRIPT[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		String readFloc = root.IDOC.E1BP_IEQM_EXTRACTOR.READ_FLOC[0].text() // MDM_EQUIPMENT_SAVEREPLICA
		if (refDocNo.length() == 0) {
			refDocNo = root.IDOC.E1MBGMCR.E1BP2017_GM_HEAD_01.REF_DOC_NO[0].text() // MBGMCR0x
		}
		String gmCode = root.IDOC.E1MBGMCR.E1BP2017_GM_CODE.GM_CODE[0].text() // MBGMCR0x
		String lgNum = root.IDOC.'_-SCWM_-E1LTORH'.LGNUM[0].text() // /SCWM/WMTOID0x
		String whO = root.IDOC.'_-SCWM_-E1LTORH'.WHO[0].text() // /SCWM/WMTOID0x
		if (lgNum.length() == 0) {
			lgNum = root.IDOC.'_SCWM-E1LTCOH'.LGNUM[0].text() // /SCWM/WMTCID0x
		}
		if (whO.length() == 0) {
			whO = root.IDOC.'_SCWM-E1LTCOH'.WHO[0].text() // /SCWM/WMTCID0x
		}
		// Set values for search in header field
		message.setHeader('SAP_ApplicationID', docNum)
		message.setHeader('SAP_Sender', sender)
		message.setHeader('SAP_Receiver', receiver)
		message.setHeader('SAP_MessageType', messageType)

		// Set log entries
		messageLog.setStringProperty('SAP IDoc number (DOCNUM)', trimZeroLeft(docNum))
		if (belNr.length() > 0) {
			messageLog.setStringProperty('SAP Document number (BELNR)', trimZeroLeft(belNr))
//			message.setProperty('SAP Document number (BELNR)', trimZeroLeft(belNr)) // Only for debug
		}
		if (bsArt.length() > 0) {
			messageLog.setStringProperty('Document Type (BSART)', bsArt)
//			message.setProperty('Document Type (BSART)', bsArt) // Only for debug
		}
		if (vtrNr.length() > 0) {
			messageLog.setStringProperty('Agreement Number (Contract Number) (VTRNR)', trimZeroLeft(vtrNr))
//			message.setProperty('Agreement Number (Contract Number) (VTRNR)', trimZeroLeft(vtrNr)) // Only for debug
		}
		if (labNk.length() > 0) {
//			messageLog.setStringProperty('Customer number for forecast / JIT dlv. sched. (LABNK)', trimZeroLeft(labNk))
			message.setProperty('Customer number for forecast / JIT dlv. sched. (LABNK)', trimZeroLeft(labNk)) // Only for debug
		}
		if (vBelN.length() > 0) {
			messageLog.setStringProperty('Sales and Distribution Document Number (VBELN)', trimZeroLeft(vBelN))
//			message.setProperty('Sales and Distribution Document Number (VBELN)', trimZeroLeft(vBelN)) // Only for debug
		}
		if (tkNum.length() > 0) {
			messageLog.setStringProperty('Shipment Number (TKNUM)', trimZeroLeft(tkNum))
//			message.setProperty('Shipment Number (TKNUM)', trimZeroLeft(tkNum)) // Only for debug
		}
		if (bgmRef.length() > 0) {
			messageLog.setStringProperty('Message Reference Number (BGMREF)', trimZeroLeft(bgmRef))
//			message.setProperty('Message Reference Number (BGMREF)', trimZeroLeft(bgmRef)) // Only for debug
		}
		if (refDocNo.length() > 0) {
			messageLog.setStringProperty('Reference Document Number (REF_DOC_NO)', trimZeroLeft(refDocNo))
//			message.setProperty('Reference Document Number (REF_DOC_NO)', trimZeroLeft(refDocNo)) // Only for debug
		}
		if (order.length() > 0) {
			messageLog.setStringProperty('Order Number(ORDER)', trimZeroLeft(order))
//			message.setProperty('Order Number(ORDER)', trimZeroLeft(order)) // Only for debug
		}
		if (poNumber.length() > 0) {
			messageLog.setStringProperty('Purchasing Document Number (PO_NUMBER)', trimZeroLeft(poNumber))
//			message.setProperty('Purchasing Document Number (PO_NUMBER)', trimZeroLeft(poNumber)) // Only for debug
		}
		if (purchNoC.length() > 0) {
			messageLog.setStringProperty('Customer Reference (PURCH_NO_C)', trimZeroLeft(purchNoC))
//			message.setProperty('Customer Reference (PURCH_NO_C)', trimZeroLeft(purchNoC)) // Only for debug
		}
		if (docType.length() > 0) {
			messageLog.setStringProperty('Purchasing Document Type (DOC_TYPE)', trimZeroLeft(docType))
//			message.setProperty('Purchasing Document Type (DOC_TYPE)', trimZeroLeft(docType)) // Only for debug
		}		
		if (vendor.length() > 0) {
			messageLog.setStringProperty("Vendor's account number (VENDOR)", trimZeroLeft(vendor))
//			message.setProperty("Vendor's account number (VENDOR)", trimZeroLeft(vendor)) // Only for debug
		}
		if (matNr.length() > 0) {
			messageLog.setStringProperty('Material Number (18 Characters) (MATNR)', trimZeroLeft(matNr))
//			message.setProperty('Material Number (18 Characters) (MATNR)', trimZeroLeft(matNr)) // Only for debug
		}
		if (matNrLong.length() > 0) {
			messageLog.setStringProperty('Material Number (MATNR_LONG)', trimZeroLeft(matNrLong))
//			message.setProperty('Material Number (MATNR_LONG)', trimZeroLeft(matNrLong)) // Only for debug
		}
		if (materialLong.length() > 0) {
			messageLog.setStringProperty('Material Number (MATERIAL_LONG)', trimZeroLeft(materialLong))
//			message.setProperty('Material Number (MATERIAL_LONG)', trimZeroLeft(materialLong)) // Only for debug
		}
		if (maKtx.length() > 0) {
			messageLog.setStringProperty('Material Description (MAKTX)', maKtx)
//			message.setProperty('Material Description (MAKTX)', maKtx) // Only for debug
		}
		if (lifNr.length() > 0) {
			messageLog.setStringProperty('Account Number of Vendor or Creditor (LIFNR)', trimZeroLeft(lifNr))
//			message.setProperty('Account Number of Vendor or Creditor (LIFNR)', trimZeroLeft(lifNr)) // Only for debug
		}
		if (kunNr.length() > 0) {
			messageLog.setStringProperty('Customer Number (KUNNR)', trimZeroLeft(kunNr))
//			message.setProperty('Customer Number (KUNNR)', trimZeroLeft(kunNr)) // Only for debug
		}
		if (bPartner.length() > 0) {
			messageLog.setStringProperty('Business Partner Number (BPARTNER)', trimZeroLeft(bPartner))
//			message.setProperty('Business Partner Number (BPARTNER)', trimZeroLeft(bPartner)) // Only for debug
		}
		if (bPartnerGuid.length() > 0) {
			messageLog.setStringProperty('Business Partner GUID (BPARTNERGUID)', bPartnerGuid)
//			message.setProperty('Business Partner GUID (BPARTNERGUID)', bPartnerGuid) // Only for debug
		}
		if (name1.length() > 0) {
			messageLog.setStringProperty('Name 1 (NAME1)', name1)
//			message.setProperty('Name 1 (NAME1)', name1) // Only for debug
		}
		if (name2.length() > 0) {
			messageLog.setStringProperty('Name 2 (NAME2)', name2)
//			message.setProperty('Name 2 (NAME2)', name2) // Only for debug
		}
		if (aufNr.length() > 0) {
			messageLog.setStringProperty('Order Number (AUFNR)', trimZeroLeft(aufNr))
//			message.setProperty('Order Number (AUFNR)', trimZeroLeft(aufNr)) // Only for debug
		}
		if (infNr.length() > 0) {
			messageLog.setStringProperty('Number Of Purchasing Info Record (INFNR)', trimZeroLeft(infNr))
//			message.setProperty('Number Of Purchasing Info Record (INFNR)', trimZeroLeft(infNr)) // Only for debug
		}
		if (projectDefinition.length() > 0) {
			messageLog.setStringProperty('Project Definition (PROJECT_DEFINITION)', projectDefinition)
//			message.setProperty('Project Definition (PROJECT_DEFINITION)', projectDefinition) // Only for debug
		}
		if (batch.length() > 0) {
			messageLog.setStringProperty('Batch Number (BATCH)', trimZeroLeft(batch))
//			message.setProperty('Batch Number (BATCH)', trimZeroLeft(batch)) // Only for debug
		}
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
		if (gmCode.length() > 0) {
			messageLog.setStringProperty('Transaction For Goods Movement (GM_CODE)', gmCode)
//			message.setProperty('Transaction For Goods Movement (GM_CODE)', gmCode) // Only for debug
		}
		if (lgNum.length() > 0) {
			messageLog.setStringProperty('Warehouse Number (LGNUM)', trimZeroLeft(lgNum))
//			message.setProperty('Warehouse Number (LGNUM)', trimZeroLeft(lgNum)) // Only for debug
		}
		if (whO.length() > 0) {
			messageLog.setStringProperty('Warehouse Order Number (WHO)', trimZeroLeft(whO))
//			message.setProperty('Warehouse Order Number (WHO)', trimZeroLeft(whO)) // Only for debug
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