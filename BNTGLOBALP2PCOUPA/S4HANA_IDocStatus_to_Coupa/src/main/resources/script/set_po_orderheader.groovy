import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
    def date = new Date()
    	
	def mapMesType =	["PORDCR1":"PO-Create",
						 "PORDCH":"PO-Change"]
	
    // Build XML request        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8', standalone: 'no')
    builder.'order-header' {
		'custom-fields' {
			def status = 'Failed'
			if(message.getProperty("s4_idoc_status").equals("53")){
				status = 'Success'
			}
			def errorText = message.getProperty("s4_description_error")
			if(errorText.length() > 206){
			    errorText = errorText.substring(0,206)
			}
			'integration-status' status + " " + mapMesType.get(message.getProperty("s4_message_type")) + ": " + date.format("dd-MMM-yyyy HH:mm:ss", TimeZone.getTimeZone("CET")) + ":<br>" + errorText
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}