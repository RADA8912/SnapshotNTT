import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    def bodyAsString = message.getBody(String.class);
    
    //Get Properties  
	map = message.getProperties();
	errorLogFlag = map.get("ErrorLogFlag");
	
    
    if(errorLogFlag == "true"){
	
	    messageLog.addAttachmentAsString("Error Body", bodyAsString, "text/xml");
        def prop = message.getProperties()

    }

	
	return message;

}