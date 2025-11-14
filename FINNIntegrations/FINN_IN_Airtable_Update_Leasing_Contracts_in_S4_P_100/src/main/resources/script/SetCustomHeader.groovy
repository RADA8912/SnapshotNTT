import com.sap.gateway.ip.core.customdev.util.Message;



def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
         //Read Subscription
        def subID = message.getProperties().get("SubscriptionID");        
        //Set as Custom Header
        if(subID!=null){
        messageLog.addCustomHeaderProperty("Subscription ID", subID);
        }
       
         //Read carID
        def carID = message.getProperties().get("CarID");        
        //Set as Custom Header
        if(carID!=null){
        messageLog.addCustomHeaderProperty("Car ID", carID);
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