import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
	def rec_party = message.getProperty("Conf_Receiver")
	def send_party = message.getProperty("Conf_Sender")
	def headers = message.getHeaders()
	
	//creating the confirmation payload header  
	def root_output = new NodeBuilder()."n0:ServiceRequestFollowUpDocumentsReplicationConfirmation"('xmlns:n0': 'http://sap.com/xi/SAPGlobal20/Global'){
		MessageHeader{
			ID(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase())
			CreationDateTime(new Date().format("yyyy-MM-dd'T'HH:mm:ss'Z'"))
			ReferenceID(headers.get("SAP_ApplicationID"))
			SenderParty{
    				InternalID(schemeID:'CommunicationSystemID',schemeAgencyID:'310',rec_party)
    			}
    		RecipientParty{
    			InternalID(schemeID:'CommunicationSystemID',schemeAgencyID:'310',send_party)
    		}
    		BusinessScope{
    			TypeCode(listID:'25201',listAgencyID:'310',"3")
    			ID(schemeID:'10555',schemeAgencyID:'310',"106")
    		}
		}
	}
	
	message.setProperty("ConfirmationPayload",root_output)
	
	return message
	
}