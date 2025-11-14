import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	//create an instance of message log 
	def messageLog = messageLogFactory.getMessageLog(message);
	
	//check if the messageLog object is null - this is when the MPL log level is set to none
        if(messageLog != null)
        {
               //call the 
	       messageLog.addCustomHeaderProperty("MyCustomeHeader","MyValue");
	}
	return message;
}