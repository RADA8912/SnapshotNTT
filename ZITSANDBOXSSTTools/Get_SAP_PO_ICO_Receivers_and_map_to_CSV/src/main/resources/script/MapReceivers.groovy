import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	// Variables
	def reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
	
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
    
    // Build XML        
    builder.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')
    builder.InterfaceList {	
		input.Body.IntegratedConfigurationReadResponse.IntegratedConfiguration.each{ ico ->
		    def icoName = ico.IntegratedConfigurationID.SenderPartyID.toString() + "|" +
					      ico.IntegratedConfigurationID.SenderComponentID.toString() + "|" +
				    	  ico.IntegratedConfigurationID.InterfaceName.toString() + "|" +
			    		  ico.IntegratedConfigurationID.InterfaceNamespace.toString() + "|" +
				    	  ico.IntegratedConfigurationID.ReceiverPartyID.toString() + "|" +
			    		  ico.IntegratedConfigurationID.ReceiverComponentID.toString()
		    
		    def sender = ''
		    def senderParty = ico.IntegratedConfigurationID.SenderPartyID.toString()
		    def senderComponent = ico.IntegratedConfigurationID.SenderComponentID.toString()
		    if(senderParty != null && !senderParty.equals("")) {sender += senderParty + "|"}
		    sender += senderComponent

		    def iflow = ''
		    def icodescription = ''
			if(ico.Description.toString().startsWith("Generated for dir")){
			    iflow = ico.Description.toString().split("'")[1]
			}
			else{
			    icodescription = ico.Description.toString()
			}
    		
			ico.ReceiverInterfaces.Receiver.each { rec ->
			    def receiver = ''
    		    def receiverParty = rec.PartyID.toString()
    		    def receiverComponent = rec.ComponentID.toString()
    		    if(receiverParty != null && !receiverParty.equals("")) {receiver += receiverParty + "|"}
    		    receiver += receiverComponent
			
    			Interface{
    				'ICO' icoName
    				'Sender' sender
    				'Receiver' receiver
    				'IFlow' iflow
    				'ICO_Description' icodescription
				}
			}
		}
	}
	
	// Return
    message.setBody(writer.toString())
    return message
}