import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {	
    def messageLog = messageLogFactory.getMessageLog(message);
	def attribute = message.getHeader("ENTRY_ID",java.lang.String);
    if(messageLog != null){
	    messageLog.addAttachmentAsString("Related Attribute Value Not Existed:"+attribute ,  attribute   ,
	                                                        "text/xml");
    }
	return message;
}