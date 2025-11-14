import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetSearchValuesOpenTrans
* This Groovy script set openTRANS fields to header fields and log entries for search by ID in message monitor.
* openTRANS versions 1.0, 2.0 and 2.1 are supported.
*
* There are two cases. One case with '<INTERCHANGE_INFO>' node and one case without. Both are supported.
* 'SAP_ApplicationID' in message header is interchange ID if available otherwise document ID.
*
* Supported openTRANS document types:
* - RFQ / Request for quotation (Angebotsanforderung)
' - Quotation (Angebot)
* - Order (Auftrag)
* - Order Change (Auftragsänderung)
* - Order Response (Auftragsbestätigung)
* - Dispatch Notification (Lieferavis)
* - Receipt Acknowledgement (Wareneingangsbestätigung)
* - Invoice (Rechnung)
* - Invoice List (Rechnungsliste)
* - Remittance Advice (Zahlungsavis)
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
		String altCustomerOrderID = ''
		String supplierOrderID = ''
		String deliveryIdRef = ''

		// Get values from xml-body
		// Get values from '<INTERCHANGE_INFO>' node 
		String interchangeID = root.INTERCHANGE_INFO.INTERCHANGE_ID[0].text()
		String sendingParty = root.INTERCHANGE_INFO.SENDING_PARTY[0].text()
		String receivingParty = root.INTERCHANGE_INFO.RECEIVING_PARTY[0].text()

		// Get RFQ ID
		String rfqID = root.RFQ.RFQ_HEADER.RFQ_INFO.RFQ_ID[0].text()
		if (rfqID.length() == 0) {
			rfqID = root.RFQ_HEADER.RFQ_INFO.RFQ_ID[0].text()
		}
		// Get Quotation ID
		String quotationID = root.QUOTATION.QUOTATION_HEADER.QUOTATION_INFO.QUOTATION_ID[0].text()
		if (quotationID.length() == 0) {
			quotationID = root.QUOTATION_HEADER.QUOTATION_INFO.QUOTATION_ID[0].text()
		}
		// Get Order ID
		String oderID = root.ORDER.ORDER_HEADER.ORDER_INFO.ORDER_ID[0].text()
		if (oderID.length() == 0) {
			oderID = root.ORDER_HEADER.ORDER_INFO.ORDER_ID[0].text()
		}
		// Get Order Change ID (This i the Order ID but we use different variable for later checks.)
		String oderChangeID = root.ORDERCHANGE.ORDERCHANGE_HEADER.ORDERCHANGE_INFO.ORDER_ID[0].text()
		if (oderChangeID.length() == 0) {
			oderChangeID = root.ORDERCHANGE_HEADER.ORDERCHANGE_INFO.ORDER_ID[0].text()
		}
		// Get Order Response ID
		String oderResponseID = root.ORDERRESPONSE.ORDERRESPONSE_HEADER.ORDERRESPONSE_INFO.ORDER_ID[0].text()
		if (oderResponseID.length() == 0) {
			oderResponseID = root.ORDERRESPONSE_HEADER.ORDERRESPONSE_INFO.ORDER_ID[0].text()
		}
		// Get Dispatch Notification ID
		String dispatchNotificationID = root.DISPATCHNOTIFICATION.DISPATCHNOTIFICATION_HEADER.DISPATCHNOTIFICATION_INFO.DISPATCHNOTIFICATION_ID[0].text()
		if (dispatchNotificationID.length() == 0) {
			dispatchNotificationID = root.DISPATCHNOTIFICATION_HEADER.DISPATCHNOTIFICATION_INFO.DISPATCHNOTIFICATION_ID[0].text()
		}
		// Get Receipt Acknowledgement ID
		String receiptAcknowledgementID = root.RECEIPTACKNOWLEDGEMENT.RECEIPTACKNOWLEDGEMENT_HEADER.RECEIPTACKNOWLEDGEMENT_INFO.RECEIPTACKNOWLEDGEMENT_ID[0].text()
		if (receiptAcknowledgementID.length() == 0) {
			receiptAcknowledgementID = root.RECEIPTACKNOWLEDGEMENT_HEADER.RECEIPTACKNOWLEDGEMENT_INFO.RECEIPTACKNOWLEDGEMENT_ID[0].text()
		}
		// Get Invoice ID
		String invoiceID = root.INVOICE.INVOICE_HEADER.INVOICE_INFO.INVOICE_ID[0].text()
		if (invoiceID.length() == 0) {
			invoiceID = root.INVOICE_HEADER.INVOICE_INFO.INVOICE_ID[0].text()
		}
		// Get Invoice List ID
		String invoiceListID = root.INVOICELIST.INVOICELIST_HEADER.INVOICELIST_INFO.INVOICELIST_ID[0].text()
		if (invoiceListID.length() == 0) {
			invoiceListID = root.INVOICELIST_HEADER.INVOICELIST_INFO.INVOICELIST_ID[0].text()
		}
		// Get Remittance Advice ID
		String remittanceadviceID = root.REMITTANCEADVICE.REMITTANCEADVICE_HEADER.REMITTANCEADVICE_INFO.REMITTANCEADVICE_ID[0].text()
		if (remittanceadviceID.length() == 0) {
			remittanceadviceID = root.REMITTANCEADVICE_HEADER.REMITTANCEADVICE_INFO.REMITTANCEADVICE_ID[0].text()
		}

		// openTRANS supported document types
		// Set values for search and log entries
		if (interchangeID.length() > 0) { // Interchange info
			// Set values for search in header field
			message.setHeader('SAP_ApplicationID', interchangeID)
			message.setHeader('SAP_Sender', sendingParty)
			message.setHeader('SAP_Receiver', receivingParty)
			
			// Set log entries and properties
			messageLog.setStringProperty('openTRANS interchangeID (INTERCHANGE_ID)', interchangeID)
			messageLog.setStringProperty('openTRANS Sending Party (SENDING_PARTY)', sendingParty)
			messageLog.setStringProperty('openTRANS Receiving Party (RECEIVING_PARTY)', receivingParty)
			message.setProperty('openTRANS_INTERCHANGE_ID', interchangeID)
			message.setProperty('openTRANS_SENDING_PARTY', sendingParty)
			message.setProperty('openTRANS_RECEIVING_PARTY', receivingParty)
		}

		if (oderID.length() > 0) { // Order
			// Get and compute values
			oderID = trimZeroLeft(oderID)
			if (interchangeID.length() != 0) {
				altCustomerOrderID = root.ORDER.ORDER_HEADER.ORDER_INFO.ALT_CUSTOMER_ORDER_ID[0].text()
			} else {
				altCustomerOrderID = root.ORDER_HEADER.ORDER_INFO.ALT_CUSTOMER_ORDER_ID[0].text()
			}
			altCustomerOrderID = trimZeroLeft(altCustomerOrderID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', oderID)
			}
			message.setHeader('SAP_MessageType', 'ORDER')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Order ID (ORDER_ID)', oderID)
			messageLog.setStringProperty('openTRANS Alt Customer Order ID (ALT_CUSTOMER_ORDER_ID)', altCustomerOrderID)
			message.setProperty('openTRANS_ORDER_ID', oderID)
			message.setProperty('openTRANS_ALT_CUSTOMER_ORDER_ID', altCustomerOrderID)

		} else if (oderChangeID.length() > 0) { // Order Change
			// Get and compute values
			oderChangeID = trimZeroLeft(oderChangeID)
			if (interchangeID.length() != 0) {
				altCustomerOrderID = root.ORDERCHANGE.ORDERCHANGE_HEADER.ORDERCHANGE_INFO.ALT_CUSTOMER_ORDER_ID[0].text()
			} else {
				altCustomerOrderID = root.ORDERCHANGE_HEADER.ORDERCHANGE_INFO.ALT_CUSTOMER_ORDER_ID[0].text()
			}
			altCustomerOrderID = trimZeroLeft(altCustomerOrderID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', oderChangeID)
			}
			message.setHeader('SAP_MessageType', 'ORDER CHANGE')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Order ID (ORDER_ID)', oderChangeID)
			messageLog.setStringProperty('openTRANS Alt Customer Order ID (ALT_CUSTOMER_ORDER_ID)', altCustomerOrderID)
			message.setProperty('openTRANS_ORDER_ID', oderChangeID)
			message.setProperty('openTRANS_ALT_CUSTOMER_ORDER_ID', altCustomerOrderID)

		} else if (oderResponseID.length() > 0) { // Order Response
			// Get and compute values
			oderResponseID = trimZeroLeft(oderResponseID)
			if (interchangeID.length() != 0) {
				altCustomerOrderID = root.ORDERRESPONSE.ORDERRESPONSE_HEADER.ORDERRESPONSE_INFO.ALT_CUSTOMER_ORDER_ID[0].text()
				supplierOrderID = root.ORDERRESPONSE.ORDERRESPONSE_HEADER.ORDERRESPONSE_INFO.SUPPLIER_ORDER_ID[0].text()
			} else {
				altCustomerOrderID = root.ORDERRESPONSE_HEADER.ORDERRESPONSE_INFO.ALT_CUSTOMER_ORDER_ID[0].text()
				supplierOrderID = root.ORDERRESPONSE_HEADER.ORDERRESPONSE_INFO.SUPPLIER_ORDER_ID[0].text()   
			}
			altCustomerOrderID = trimZeroLeft(altCustomerOrderID)
			supplierOrderID = trimZeroLeft(supplierOrderID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', oderResponseID)
			}
			message.setHeader('SAP_MessageType', 'ORDER RESPONSE')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Order Response ID (ORDERRESPONSE_ID)', oderResponseID)
			messageLog.setStringProperty('openTRANS Alt Customer Order ID (ALT_CUSTOMER_ORDER_ID)', altCustomerOrderID)
			messageLog.setStringProperty('openTRANS Supplier Order ID (SUPPLIER_ORDER_ID)', supplierOrderID)
			message.setProperty('openTRANS_ORDER_RESPONSE_ID', oderResponseID)
			message.setProperty('openTRANS_ALT_CUSTOMER_ORDER_ID', altCustomerOrderID)
			message.setProperty('openTRANS_SUPPLIER_ORDER_ID', supplierOrderID)

		} else if (dispatchNotificationID.length() > 0) { // Dispacth Notification
			// Get and compute values
			dispatchNotificationID = trimZeroLeft(dispatchNotificationID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', dispatchNotificationID)
			}
			message.setHeader('SAP_MessageType', 'DISPATCH_NOTIFICATION')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Dispatch Notification ID (DISPATCHNOTIFICATION_ID)', dispatchNotificationID)
			message.setProperty('openTRANS_DISPATCH_NOTIFICATION_ID', dispatchNotificationID)

		} else if (receiptAcknowledgementID.length() > 0) { // Receipt Acknowledgement
			// Get and compute values
			receiptAcknowledgementID = trimZeroLeft(receiptAcknowledgementID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', receiptAcknowledgementID)
			}
			message.setHeader('SAP_MessageType', 'RECEIPT_ACKNOWLEDGEMENT')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Receipt Acknowledgement ID (RECEIPTACKNOWLEDGEMENT_ID)', receiptAcknowledgementID)
			message.setProperty('openTRANS_RECEIPT_ACKNOWLEDGEMENT_ID', receiptAcknowledgementID)

		} else if (invoiceID.length() > 0) { // Invoice
			// Get and compute values
			invoiceID = trimZeroLeft(invoiceID)
			if (interchangeID.length() != 0) {
				deliveryIdRef = root.INVOICE.INVOICE_HEADER.INVOICE_INFO.DELIVERY_IDREF[0].text()
			} else {
			 	deliveryIdRef = root.INVOICE_HEADER.INVOICE_INFO.DELIVERY_IDREF[0].text()
			}
			deliveryIdRef = trimZeroLeft(deliveryIdRef)

			// Set values for search in header field
			message.setHeader('SAP_ApplicationID', invoiceID)
			message.setHeader('SAP_MessageType', 'INVOICE')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Invoice ID (INVOICE_ID)', invoiceID)
			messageLog.setStringProperty('openTRANS Delivery ID Ref (DELIVERY_IDREF)', deliveryIdRef)
			message.setProperty('openTRANS_INVOICE_ID', invoiceID)
			message.setProperty('openTRANS_DELIVERY_ID_REF', deliveryIdRef)

		} else if (invoiceListID.length() > 0) { // Invoice List
			// Get and compute values
			invoiceListID = trimZeroLeft(invoiceListID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', invoiceListID)
			}
			message.setHeader('SAP_MessageType', 'INVOICE_LIST')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Invoice List ID (INVOICELIST_ID)', invoiceListID)
			message.setProperty('openTRANS_INVOICE_LIST_ID', invoiceListID)

		} else if (remittanceadviceID.length() > 0) { // Remittance Advice
			// Get and compute values
			remittanceadviceID = trimZeroLeft(remittanceadviceID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', remittanceadviceID)
			}
			message.setHeader('SAP_MessageType', 'REMITTANCEADVICE')

			// Set log entries and properties
			messageLog.setStringProperty('openTRANS Remittance Advice ID (REMITTANCEADVICE_ID)', remittanceadviceID)
			message.setProperty('openTRANS_REMITTANCEADVICE_ID', remittanceadviceID)

		} else if (rfqID.length() > 0) { // RFQ Request for quotation
			// Get and compute values
			rfqID = trimZeroLeft(rfqID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', rfqID)
			}
			message.setHeader('SAP_MessageType', 'RFQ')

			// Set log entries
			messageLog.setStringProperty('openTRANS RFQ ID (RFQ_ID)', rfqID)
			message.setProperty('openTRANS_RFQ_ID', rfqID)

		} else if (quotationID.length() > 0) { // Quotation
			// Get and compute values
			quotationID = trimZeroLeft(quotationID)

			// Set values for search in header field
			if (interchangeID.length() == 0) {
				message.setHeader('SAP_ApplicationID', quotationID)
			}
			message.setHeader('SAP_MessageType', 'QUOTATION')

			// Set log entries
			messageLog.setStringProperty('openTRANS Quotation ID (QUOTATION_ID)', quotationID)
			message.setProperty('openTRANS_QUOTATION_ID', quotationID)
		}
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