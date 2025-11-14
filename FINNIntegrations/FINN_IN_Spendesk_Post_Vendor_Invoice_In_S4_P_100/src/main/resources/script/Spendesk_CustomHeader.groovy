
import com.sap.gateway.ip.core.customdev.util.Message;

/**
 * This scripts stores custom header information for the monitoring. It 
 * mainly includes the dates for the dates used to verify if no 
 * payload is present.
 * 
 * @param mesasge
 * @return message
 * 
 * */

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
       
        //Read toDate
        def toDate = message.getProperties().get("toDate");        
        //Set as Custom Header
        if(toDate!=null){
        messageLog.addCustomHeaderProperty("toDate", toDate);    
        }
        
        //Read fromDate
        def fromDate = message.getProperties().get("fromDate");        
        //Set as Custom Header
        if(fromDate!=null){
        messageLog.addCustomHeaderProperty("fromDate", fromDate);
        }
        
        
        
    }
    return message;
}