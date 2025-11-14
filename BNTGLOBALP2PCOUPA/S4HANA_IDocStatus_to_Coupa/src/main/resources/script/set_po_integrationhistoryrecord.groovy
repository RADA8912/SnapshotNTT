import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
    
    // Build XML request        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
    builder.'integration-history-record' {
		'document-type' 'OrderHeader'
		'document-id' message.getProperty("s4_po_no")
		'document-status' 'issued'
		'contact-alert-type' 'Technical'
		'responses' {
			'response' {
				def responseCode = 'fail'
				if(message.getProperty("s4_idoc_status").equals("53")){
					responseCode = 'success'
				}
				'response-code' responseCode
				'response-message' message.getProperty("s4_description_error")
			}
		}
		'integration' {
			'id' '2'
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}