import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	//create an instance of message log 
	def messageLog = messageLogFactory.getMessageLog(message);
	
	//typecast body to string to create an attachment of the payload in the message processing log
	def stringBody = message.getBody(java.lang.String) as String;
	
	//check if the messageLog object is null - this is when the MPL log level is set to none
        if(messageLog != null)
        {
               //Finally use the addAttachmentAsString API to add a string as an attachment
	       messageLog.addAttachmentAsString("myAttachment", stringBody, "text/plain");
	}
	return message;
}