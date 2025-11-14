import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.XmlSlurper;
import java.util.HashMap;

def Message processData(Message message){

	def root = new XmlSlurper().parseText(message.getBody(String.class))
	ArrayList service_order_ids = message.getProperty("service_order_ids")
	//List containing ticket ids that need to be updated with BTDReference 
	def update_list = [:]
	//List of tickets for which service order need to be created
	def create_list = [] 
	
	//Extracting the response from query get
	root.batchQueryPartResponse.each{
	
	    def referenceserviceorder = it.body.A_ServiceOrder.A_ServiceOrderType.ReferenceServiceOrder.text()
	    
	    if(!referenceserviceorder.isEmpty()){
	        //update scenario
	        referenceserviceorder = referenceserviceorder.split("#")[-1]
	        def list = [] 
	        list.add(it.body.A_ServiceOrder.A_ServiceOrderType.ServiceOrderUUID[0].text())
	        list.add(it.body.A_ServiceOrder.A_ServiceOrderType.ServiceOrder[0].text())
	        update_list[referenceserviceorder]=list
	    }
	}
	
	for (id in service_order_ids){
	    if (!update_list.keySet().contains(id)){
	        //create scenario
	        create_list.add(id)
	    }
	}
    
    if (create_list.isEmpty()){
        message.setProperty("Creation","NO")
    }
    else{
        message.setProperty("create_list",create_list)
    }
    message.setProperty("update_list",update_list)
	
	return message 
}




