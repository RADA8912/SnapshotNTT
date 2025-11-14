import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

def Message processData(Message message) {
	map = message.getProperties();
	property_EnableLogging = map.get("EnableLogging");
	message.setHeader("SAP_IsIgnoreProperties",new Boolean(true));

	def ex = map.get("CamelExceptionCaught");
	
	if (property_EnableLogging.toUpperCase().equals("TRUE") && ex != null) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ex.printStackTrace(new PrintStream(baos));

		def body = baos.toString();

		def logLocation = message.getProperty("log.location");
		if (logLocation == null || logLocation.length() <= 0) {
			logLocation = "Error Payload";
		}

		def logContentType = message.getProperty("log.contenttype");
		if (logContentType == null || logContentType.length() <= 0) {
			logContentType = "text/plain";
		}

		def messageLog = messageLogFactory.getMessageLog(message);

		messageLog.addAttachmentAsString(logLocation, body, logContentType);
	}	

	return message;
}

