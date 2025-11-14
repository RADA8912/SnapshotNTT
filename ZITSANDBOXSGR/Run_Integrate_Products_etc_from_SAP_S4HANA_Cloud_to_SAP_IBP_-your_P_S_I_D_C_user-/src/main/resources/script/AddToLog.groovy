import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    
    	/*add message body to logs in case of an exception.*/
	def bodyAsString = message.getBody(java.lang.String);
	
	if (bodyAsString != null) {
	    /* def headers = message.getHeaders();*/
	    /* def ibpStep = headers.get("IBPStep"); */
	
    	def messageLog = messageLogFactory.getMessageLog(message);
	    messageLog.addAttachmentAsString('ProcessDirectResponseMessageBody', bodyAsString, 'text/xml');
	}
    return message;
}