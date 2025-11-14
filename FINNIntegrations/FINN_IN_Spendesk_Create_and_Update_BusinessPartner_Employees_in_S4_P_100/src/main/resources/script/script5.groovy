import com.sap.gateway.ip.core.customdev.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs error message as attachment
 */
def Message processData(Message message) {
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	

		
	def map = message.getProperties();
	def ex = map.get("CamelExceptionCaught");
	def bodyAsString = message.getBody(String.class);


	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null)
	{
		messageLog.addAttachmentAsString("Caught Exception", ex.getMessage(), "plain/text");
		messageLog.addAttachmentAsString("Payload Exception", bodyAsString, "plain/text");

	}
	
	
	return message;
	
}