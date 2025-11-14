import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //initialize
    def reader = message.getBody(Reader)
    def coupaXml = new XmlSlurper().parse(reader)
	
	//define variables
	def sap_invoice_number = ""
	def consumed_value = 0.0
	
	//Get current Invoice Numbers from Coupa Purchase Order Lines
	def sap_invoice_number_list = []
	coupaXml.'order-lines'.'order-line'.'custom-fields'.'sap-document-number'.each {
    	if(!it.text().equals(""))  {
    	    sap_invoice_number_list += it.toString().tokenize('|')
    	}
	}
	sap_invoice_number = sap_invoice_number_list.unique().join("|")
	
	//Get current Consumed Value from Coupa Purchase Order Lines
	coupaXml.'order-lines'.'order-line'.'custom-fields'.'consumed-value'.each {
    	if(!it.text().equals(""))  {
    	    consumed_value += it.text().toDouble()
    	}
	}
	consumed_value = String.format("%.2f", consumed_value)

	//set properties
    message.setProperty("sap_invoice_number", sap_invoice_number)
	message.setProperty("consumed_value", consumed_value)
	
	//return
    return message
}