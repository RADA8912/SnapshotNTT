import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //initialize
    def reader = message.getBody(Reader)
    def idoc = new XmlSlurper().parse(reader)
	
	//define variables
	def invoice_number = ""
	def invoice_number_prefix = ""
	def invoice_number_prefix_written = ""
	def vendor_invoice_number = ""
	def invoice_date = ""
	def amount = ""
	def sign = ""
	def currency = ""
	def base64_invoice = ""
	def isBase64Available = false
	def additionalCommentText = ""
	
	//set variables
    invoice_number = idoc.IDOC.E1EDK01.BELNR
    
    def vgart = idoc.IDOC.E1EDK01.Z1E1EDK01.VGART
    def shkzg = idoc.IDOC.E1EDK01.Z1E1EDK01.SHKZG
    if(vgart.equals("RD") && shkzg.equals("S"))  {
        invoice_number_prefix = 'INV'
        invoice_number_prefix_written = 'Invoice'
    }
    else if(vgart.equals("RD") && shkzg.equals("H"))  {
        invoice_number_prefix = 'CN'
        invoice_number_prefix_written = 'Credit Note'
    }
    else if(vgart.equals("RS") && shkzg.equals("H"))  {
        invoice_number_prefix = 'RVS'
        invoice_number_prefix_written = 'Invoice Cancellation'
    }
    
    vendor_invoice_number = idoc.IDOC.E1EDK01.Z1E1EDK01.XBLNR
    
	invoice_date = idoc.IDOC.E1EDK03.find{it.IDDAT == "016"}.DATUM
	invoice_date = Date.parse('yyyyMMdd', invoice_date.toString()).format('dd-MMM-yyyy')
	
	amount = idoc.IDOC.E1EDS01.find{it.SUMID == "002"}.SUMME
	
	if(shkzg.equals("S"))  {sign = '+'}
    else if(shkzg.equals("H"))  {sign = '-'}
	
	currency = idoc.IDOC.E1EDS01.find{it.SUMID == "002"}.WAERQ
	
	def base64_invoice_temp = idoc.IDOC.E1EDK01.Z1E1EDK02.INVOICE_PDF_CONTENT_X
	base64_invoice = base64_invoice_temp.join()
	
	if(!base64_invoice.equals("")) {
	    isBase64Available = true
	} else if (!invoice_number_prefix.equals("RVS")) {
	    additionalCommentText = " Unfortunately, the attachment could not be transferred because it either was not attached or the maximum file size was exceeded."
	}
	
	//set properties
    message.setProperty("invoice_number", invoice_number)
    message.setProperty("invoice_number_prefix", invoice_number_prefix)
    message.setProperty("invoice_number_prefix_written", invoice_number_prefix_written)
    message.setProperty("vendor_invoice_number", vendor_invoice_number)
	message.setProperty("invoice_date", invoice_date)
	message.setProperty("amount", amount)
	message.setProperty("sign", sign)
	message.setProperty("currency", currency)
	message.setProperty("base64_invoice", base64_invoice)
	message.setProperty("isBase64Available", isBase64Available)
	message.setProperty("additionalCommentText", additionalCommentText)
	
	//return
    return message
}