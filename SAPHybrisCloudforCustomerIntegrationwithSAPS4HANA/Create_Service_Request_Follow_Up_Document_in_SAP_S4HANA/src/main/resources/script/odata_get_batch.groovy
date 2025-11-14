import com.sap.gateway.ip.core.customdev.util.Message;


def Message processData(Message message){
    
    def c4c_ticket_ids = message.getProperty("service_order_ids");
	String sender = message.getProperty("Conf_Sender")
	String body = ""
	
	Node output_node = new NodeBuilder().batchParts{
	   }
	                    
	//creating the body for create query 
	
	        for (id in c4c_ticket_ids){
            def req = 'A_ServiceOrder?$filter=ReferenceServiceOrder%20eq%20' + "'" + sender + "%23" + id + "'"
	        Node Ref_Node= new NodeBuilder().batchQueryPart{
	            uri(req)
	        }
				output_node.append(Ref_Node)
	    }
	
	//setting the body for the batch query get request to S/4
    String outxml = groovy.xml.XmlUtil.serialize( output_node )
    message.setBody(outxml)
    
    message.setProperty("SAP_BatchLineSeparator","CRLF")
	
	return message 
}
