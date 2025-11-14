import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {	
    def logEnabled = message.getProperty("enableLog") as String;
    if(logEnabled == "true") {
    	def body = message.getBody(java.lang.String) as String;
    	if(body.contains("MATMAS05"))
    	    return message;
	    
	    def headers = message.getHeaders() as Map<String, Object>;
	    def properties = message.getProperties() as Map<String, Object>;
	
	    def propertiesAsString ="\n";
	    properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };
	
	    def headersAsString ="\n";
	    headers.each{ it -> headersAsString = headersAsString + "${it}" + "\n" };
	

	    def messageLog = messageLogFactory.getMessageLog(message);
        if(messageLog != null){
		    messageLog.addAttachmentAsString("Log - Message",   "\n Properties \n ----------   \n" + propertiesAsString +
                                                                "\n Headers \n ----------   \n" + headersAsString +
		                                                        "\n Body \n ----------  \n\n" + body,
		                                                        "text/xml");
	    }
    }
	return message;
}