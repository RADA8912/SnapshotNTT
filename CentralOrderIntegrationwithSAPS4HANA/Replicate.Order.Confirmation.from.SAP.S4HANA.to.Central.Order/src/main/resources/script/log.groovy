import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.op.agent.mpl.*;
import groovy.transform.Field;
import java.util.HashMap;
import groovy.xml.XmlUtil;
import groovy.json.JsonOutput;

@Field String S4_MESSAGE_XML = '1 - Inbound Message XML';
@Field String IFLOW_MESSAGE_JSON = '2 - Outbound Message JSON';
@Field String CUST_EXIT_OUTPUT = '2.1 - Exit Output JSON';
@Field String CO_RESPONSE_JSON = '3 - Central Order Response JSON';
@Field String EX_DETAILS = '3 - Exception Details';

def Boolean isDebug(Message message) { 
    final MplConfiguration config = message.getProperty("SAP_MessageProcessingLogConfiguration");
    return config.getOverallLogLevel() == MplLogLevel.DEBUG;
}

def Message logS4MessageXML(Message message) {
    return processData(S4_MESSAGE_XML, true, message);
}

def Message logiFlowMessageJson(Message message) {
    return processData(IFLOW_MESSAGE_JSON, false, message);
}

def Message logCustExitOutput(Message message) {
    return processData(CUST_EXIT_OUTPUT, false, message);
}

def Message logCOResponseJson(Message message) {
    return processData(CO_RESPONSE_JSON, false, message);
}

def Message logExceptionDetailsJson(Message message) {
    return processData(EX_DETAILS, false, message);
}

def Message processData(String title, boolean isXML, Message message) {
	if (isDebug(message)) {
		def pmap = message.getProperties();
		
		def body = message.getBody(java.lang.String) as String;
		def headers = message.getHeaders() as Map<String, Object>;
		def properties = message.getProperties() as Map<String, Object>;
		
		def propertiesAsString ="\n";
		properties.each{ it -> propertiesAsString = propertiesAsString + "${it}" + "\n" };
		 
		def headersAsString ="\n";
		headers.each{ it -> headersAsString = headersAsString + "${it}" + "\n" };		

		def messageLog = messageLogFactory.getMessageLog(message);
		def prettyPrintbodyString = "\n";
		if((messageLog != null) && (body != "")) {         
			if (isXML) {
				prettyPrintbodyString = XmlUtil.serialize(body);
			}
			else {                
                prettyPrintbodyString = JsonOutput.prettyPrint(body);
            }  
        }		
		
		messageLog.addAttachmentAsString(title , "\n Properties \n ----------   \n" + propertiesAsString +
												 "\n Headers \n ----------   \n" + headersAsString +
												 "\n Body \n ----------  \n\n" + prettyPrintbodyString, "text/plain");		
	}
	return message;
}



