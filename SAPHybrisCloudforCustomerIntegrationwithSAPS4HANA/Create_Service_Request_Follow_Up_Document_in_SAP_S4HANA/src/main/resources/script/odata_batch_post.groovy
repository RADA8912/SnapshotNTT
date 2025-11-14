import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.mapping.ValueMappingApi
import com.sap.it.api.ITApiFactory
import java.util.HashMap;
import groovy.xml.MarkupBuilder;
import groovy.json.JsonSlurper

def Message processData(Message message){
    
    def service_order_ids = message.getProperty("create_list");
    String C4C_payload = new String(message.getProperty("P_OriginalPayload"));
	def root = new XmlSlurper().parseText(C4C_payload);
	String sender = message.getProperty("Conf_Sender")
	String body = ""
	Node Ref_Node
	Node output_node = new NodeBuilder().batchParts{
	   }
	   
	//creating the body for create query 
	root.ServiceRequestFollowUpDocumentsReplicateRequestMessage.ServiceRequest.each{
	    if (service_order_ids.contains(it.ID.text())){
	        String party = it.Party.findAll({node -> node.RoleCode.text().equals("1001")}).ReceiverPartyID.text()
	        String shipto = it.Party.findAll({node -> node.RoleCode.text().equals("1005")}).ReceiverPartyID.text()
	        
	        //mapping the priority
	        String priority = it.ServiceTerms.PriorityCode.text()
	        def description = it.Name.text().take(40)
	        def request_start = it.TicketTimeline.RequestedStart.text()
	        request_start = request_start[0..-2]
	        def request_end = it.TicketTimeline.RequestedEnd.text()
	        request_end = request_end[0..-2]
	        def reference_service = sender+ "#" + it.ID.text()
	        def language = it.Name[0].@languageCode.text()
	        def salesOrg = it.BusinessArea.ReceiverSalesOrganisationID.text()
	        def distChannel = it.BusinessArea.DistributionChannelCode.text()
	        def division = it.BusinessArea.DivisionCode.text()
	        
	        
	        //getting the ServiceObjectType value from valuemapping 
	        def c4c_type= it.ProcessingTypeCode.text()
	        def valueMapApi = ITApiFactory.getApi(ValueMappingApi.class, null)
            def value = valueMapApi.getMappedValue('C4C', 'ProcessingTypeCode', c4c_type, 'S4', 'ServiceObjectType')
            
           
            
	        Ref_Node= new NodeBuilder().batchChangeSet{
					batchChangeSetPart{
						method("POST")
						uri('A_ServiceOrder')
						A_ServiceOrder{
							A_ServiceOrderType{
							    to_Text{
                                    A_ServiceOrderTextType{
                                        Language(language)
                                        LongText(description)
                                    }
							    }
								ServiceOrderType(value)
								ServiceOrderDescription(description)
								ServiceDocumentPriority(priority)
								RequestedServiceStartDateTime(request_start)
								RequestedServiceEndDateTime(request_end)
								SoldToParty(party)
								ShipToParty(shipto)
								ReferenceServiceOrder(reference_service)
								SalesOrganization(salesOrg)
								DistributionChannel(distChannel)
								Division(division)
								link{
                                    to_Text{
                                        A_ServiceOrderTextType{
                                            Language(language)
                                        }
                                    }
								}
							}
						}
					}
	        }
	        output_node.append(Ref_Node)
	    }
		
	}
	    
	//setting body for the batch post 
    String outxml = groovy.xml.XmlUtil.serialize( output_node )
    message.setBody(outxml)
    
    message.setProperty("SAP_BatchLineSeparator","CRLF")
	
	return message 

}
