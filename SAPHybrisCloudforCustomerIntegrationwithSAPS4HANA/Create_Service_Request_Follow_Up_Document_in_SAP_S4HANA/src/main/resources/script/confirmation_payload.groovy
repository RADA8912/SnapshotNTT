import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
	
	def root = new XmlParser().parseText(message.getBody(String.class))
	message.setProperty("CreateResponse",root)
	Node sr_node
	def rec_party = message.getProperty("Conf_Receiver")
	def root_output= message.getProperty("ConfirmationPayload")
	
	//creating confirmation node for successful create scenarios 
	root.batchChangeSetResponse.each{
	    if(it.batchChangeSetPartResponse.statusCode.text().equals("201")){// 201 is the Statuscode for successful create
	    def uuid = it.batchChangeSetPartResponse.body.ServiceOrderUUID.text()
	    def serviceorderid = it.batchChangeSetPartResponse.body.ServiceOrder.text()
	    def referenceservice = it.batchChangeSetPartResponse.body.ReferenceServiceOrder.text().split("#")[1]
	    sr_node = new NodeBuilder().ServiceRequestFollowUpDocumentsConfirmation{
			BasicMessageHeader()
			ServiceRequestFollowUpDocumentsConfirmation(actionCode: "04"){
				ID(referenceservice)
				ServiceRequestConfirmation(actionCode: "04"){
				    BusinessTransactionDocumentRelationshipRoleCode("1")
				    BusinessSystemID(rec_party)
				    BTDReference{
				        //UUID(uuid)
				        ID(schemeID:'S4HOP',serviceorderid)
				        TypeCode("2977")
				    }
				}
			}
	    }
	    root_output.append(sr_node)
	}
	}

    //updating the body to confirmation payload 
    message.setProperty("ConfirmationPayload",root_output)//updating the confirmation payload 
    StringWriter stringWriter = new StringWriter()
    XmlNodePrinter nodePrinter = new XmlNodePrinter(new PrintWriter(stringWriter))
    nodePrinter.setPreserveWhitespace(true)
	nodePrinter.print(root_output)
    message.setBody(stringWriter.toString())
    
	return message
}