import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.XmlSlurper;

def Message processData(Message message){

    //parsing the data 
	def root = new XmlSlurper().parseText(message.getBody(String.class))
	String rec_party = root.MessageHeader.RecipientParty.InternalID.text()
	String send_party = root.MessageHeader.SenderParty.InternalID.text()
	
	//storing all the c4c ticket ids which are released and has no btdreference 
	ArrayList service_order_ids = [] 

	root.ServiceRequestFollowUpDocumentsReplicateRequestMessage.ServiceRequest.each{
		def cond1 = it.ServiceOrderFollowUpReleaseStatusCode == "3"? true : false //checking if ticket is released 
		def cond2 = it.BTDReference.BTDReference.findAll({node -> node.BusinessSystemID.text().equals(rec_party)}).isEmpty()
		def cond3 = it.BTDReference.BTDReference.BTDReference.findAll({node -> node.TypeCode.text().equals("2977") & node.ID[0].@schemeID.text().equals("S4HOP")}).isEmpty()
		
		if(cond1 && cond2 && cond3){
		    	service_order_ids.add(it.ID.text())
		}
	}
	
	if(service_order_ids.isEmpty()){
	    //if ticket isnt released / released ticket already have BTDReference 
		message.setProperty("ActionReq","NO")
	}
	else{
		message.setProperty("service_order_ids",service_order_ids)
	}
	
	message.setProperty("Conf_Sender",send_party)
	message.setProperty("Conf_Receiver",rec_party)
	
	return message 
}