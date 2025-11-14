import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
	
    // Build XML request        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
    builder.'inventory-transaction' {
		'custom-fields' {
			def status = 'Failed'
			if(message.getProperty("s4_idoc_status").equals("53")){
				status = 'Success'
			}
			def errorText = message.getProperty("s4_description_error")
			if(errorText.length() > 246){
			    errorText = errorText.substring(0,246)
			}
			'integration-status' status + ": " + errorText
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}