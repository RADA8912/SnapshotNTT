import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
    
	def mapMoveType =	["101":"created",
						 "102":"voided"]
	
    // Build XML request        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
    builder.'integration-history-record' {
		'document-type' 'InventoryTransaction'
		'document-id' message.getProperty("s4_gr_no")
		'document-status' mapMoveType.get(message.getProperty("s4_gr_movement_type"))
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
			'id' '32'
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}