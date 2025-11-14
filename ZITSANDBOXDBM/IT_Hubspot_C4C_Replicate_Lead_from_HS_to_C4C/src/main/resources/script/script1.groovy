import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
    def messageLog = messageLogFactory.getMessageLog(message);
	
	messageLog.addAttachmentAsString("Error Note", "For further details on the error please check the C4C Webservice Monitoring.", "text/xml");
	
	return message;

}