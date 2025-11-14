import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.XmlParser;

def Message processData(Message message){
    
	def rec_party = message.getProperty("Conf_Receiver")
	def update_list = message.getProperty("update_list")
	def confirmation_payload = message.getProperty("ConfirmationPayload")
	Node sr_node
	
	//Building node of the confirmation message
	for ( key in update_list.keySet()){
        sr_node = new NodeBuilder().ServiceRequestFollowUpDocumentsConfirmation{
            BasicMessageHeader()
	        ServiceRequestFollowUpDocumentsConfirmation(actionCode: "04"){
	            ID(key)
			    ServiceRequestConfirmation(actionCode:"04"){
			        BusinessTransactionDocumentRelationshipRoleCode("6")
    			    BusinessSystemID(rec_party)
    			    BTDReference{
    			        //UUID(update_list.get(key)[0])
    			        ID(schemeID:'S4HOP',update_list.get(key)[1])
    			        TypeCode('2977')
    		        }
			    }
		    }
	    }
	    confirmation_payload.append(sr_node)
	}
	
	//If there is not create scenario then setting the body to confirmation payload for the confirmation call 
	message.setProperty("ConfirmationPayload",confirmation_payload)
	if(message.getProperty("Creation")=="NO"){
	     StringWriter stringWriter = new StringWriter()
    XmlNodePrinter nodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))
    nodePrinter.setPreserveWhitespace(true)
	nodePrinter.print(confirmation_payload)
    message.setBody(stringWriter.toString())
	}
	return message;
}

