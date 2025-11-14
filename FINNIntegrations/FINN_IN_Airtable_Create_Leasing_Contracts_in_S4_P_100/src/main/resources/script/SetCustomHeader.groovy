import com.sap.gateway.ip.core.customdev.util.Message;



def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
        //Read Contract ID
        def sapID = message.getProperties().get("RealEstateContract");        
        //Set as Custom Header
        if(sapID!=null){
        messageLog.addCustomHeaderProperty("Contract ID", sapID);
        }
        
         //Read carID
        def carID = message.getProperties().get("CarID");        
        //Set as Custom Header
        if(carID!=null){
        messageLog.addCustomHeaderProperty("Car ID", carID);
        }
       
          //Read Deal ID
        def subID = message.getProperties().get("SubscriptionID");        
        //Set as Custom Header
        if(subID!=null){
        messageLog.addCustomHeaderProperty("Subscription ID", subID);
        }
        //Read BP ID
		def hubID = message.getProperties().get("HubspotID");        
		//bpID as Custom Header
		if(hubID!=null){
		messageLog.addCustomHeaderProperty("Hubspot ID", hubID);
		}
		
		//Read BP ID
		def spendID = message.getProperties().get("SpendeskID");        
		//bpID as Custom Header
		if(spendID!=null){
		messageLog.addCustomHeaderProperty("Spendesk ID", spendID);
		}
                                        			
       
        //Read Contract Type
		def conTyp = message.getProperties().get("ContractType");        
		//bpID as Custom Header
		if(conTyp!=null){
		messageLog.addCustomHeaderProperty("Contract Type", conTyp);
		}
        
    }
    return message;
}