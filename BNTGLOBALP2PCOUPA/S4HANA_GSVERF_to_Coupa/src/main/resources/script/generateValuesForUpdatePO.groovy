import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //initialize
    def reader = message.getBody(Reader)
    def idoc = new XmlSlurper().parse(reader)
	
	def coupaPayload = message.getProperty("coupa_order")
    def coupaXml = new XmlSlurper(false, false).parseText(coupaPayload)
	
	//define variables
	def order_line_id = ""
	def supplier_invoice_number = ""
	def sap_invoice_number = ""
	def invoice_amount = ""
	def consumed_value = 0
	
	//get posex from idoc and find the coupa order line id
	def order_line_num = ""
	order_line_num = idoc.order.orderitem.ZEILE.toString().replaceFirst("^0*", "")
	order_line_id = coupaXml.'order-lines'.'order-line'.find{it.'line-num' == order_line_num}.id.toString()
	
	//check if coupa order line was found
	if (order_line_id.equals(""))  {
	    message.setProperty("coupa_order_line_exist", false)
	    return message
	} else {
	    message.setProperty("coupa_order_line_exist", true)
	}
	
	//get current values from Coupa
	supplier_invoice_number = coupaXml.'order-lines'.'order-line'.find{it.id == order_line_id}.'custom-fields'.'supplier-document-number'.toString()
	sap_invoice_number = coupaXml.'order-lines'.'order-line'.find{it.id == order_line_id}.'custom-fields'.'sap-document-number'.toString()
	invoice_amount = coupaXml.'order-lines'.'order-line'.find{it.id == order_line_id}.'custom-fields'.'invoice-amount'.toString()
	consumed_value = coupaXml.'order-lines'.'order-line'.find{it.id == order_line_id}.'custom-fields'.'consumed-value'.toString()
	if(consumed_value.equals(""))  {
	    consumed_value = 0
	} else {
	    consumed_value = consumed_value.toDouble()
	}
	
	//check if coupa order line contains already the SAP invoice number
	if (sap_invoice_number.contains(message.getProperty("invoice_number_prefix") + message.getProperty("invoice_number")))  {
	    message.setProperty("coupa_order_line_contains_invoice", true)
	    return message
	} else {
	    message.setProperty("coupa_order_line_contains_invoice", false)
	}
	
	//update variables
	if(!supplier_invoice_number.equals(""))  {supplier_invoice_number += '|'}
	supplier_invoice_number += message.getProperty("vendor_invoice_number")
	
	if(!sap_invoice_number.equals(""))  {sap_invoice_number += '|'}
	sap_invoice_number += message.getProperty("invoice_number_prefix") + message.getProperty("invoice_number")
	
	def idoc_netwr = idoc.order.orderitem.NETWR.toString()
	
	if(!invoice_amount.equals(""))  {invoice_amount += '|'}
	invoice_amount += idoc_netwr + message.getProperty("sign") + ' ' + message.getProperty("currency")
	
	if(idoc_netwr.equals(""))  {
	    idoc_netwr = 0
	} else {
	    idoc_netwr = idoc_netwr.toDouble()
	}
	
	if(message.getProperty("sign").equals('+'))  {
		consumed_value += idoc_netwr
	}
	else if (message.getProperty("sign").equals('-'))  {
		consumed_value -= idoc_netwr
	}
	consumed_value = String.format("%.2f", consumed_value)

	//set properties
    message.setProperty("order_line_id", order_line_id)
    message.setProperty("supplier_invoice_number", supplier_invoice_number)
    message.setProperty("sap_invoice_number", sap_invoice_number)
	message.setProperty("invoice_amount", invoice_amount)
	message.setProperty("consumed_value", consumed_value)
	
	//return
    return message
}