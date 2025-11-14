import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.op.agent.mpl.*;
import java.util.HashMap;
import groovy.transform.Field;
import groovy.json.JsonOutput;
import groovy.xml.XmlUtil;

@Field String ORIGINAL_ORDER_SERVICE_JSON_PAYLOAD = '1 - Inbound Order JSON';
@Field String MOD_ORDER_SERVICE_JSON_PAYLOAD = '2 - Enhanced Inbound Order JSON';
@Field String ORDER_SERVICE_XML_PAYLOAD = '3 - Enhanced Inbound Order XML';
@Field String MAPPED_S4_PAYLOAD = '4 - Outbound Order XML';
@Field String CUST_EXIT_OUTPUT = '4.1 - Exit Output XML';
@Field String IFLOW_JSON_RESPONSE = '5 - iFlow Response JSON';
@Field String IFLOW_JSON_EXCEPTION_RESPONSE = '5 - iFlow Error Response JSON';

def Boolean isDebug(Message message) { 
    final MplConfiguration config = message.getProperty("SAP_MessageProcessingLogConfiguration");
    return config.getOverallLogLevel() == MplLogLevel.DEBUG;
}

def Message logOriginalJsonPayload(Message message) {
    log(ORIGINAL_ORDER_SERVICE_JSON_PAYLOAD, false, message);
}

def Message logModJsonPayload(Message message) {    
    log(MOD_ORDER_SERVICE_JSON_PAYLOAD, false, message);
}

def Message logXmlPayload(Message message) {
    log(ORDER_SERVICE_XML_PAYLOAD, true, message);
}

def Message logMappedS4Payload(Message message) {
    log(MAPPED_S4_PAYLOAD, true, message);
}

def Message logCustExitOutput(Message message) {
    log(CUST_EXIT_OUTPUT, true, message);
}

def Message logiFlowJsonResponse(Message message) {
    log(IFLOW_JSON_RESPONSE, false, message);
}

def Message logiFlowJsonExceptionResponse(Message message) {
    log(IFLOW_JSON_EXCEPTION_RESPONSE, false, message);
}

def Message log(String title, boolean isXML, Message message) {
    if (isDebug(message)) {
        def pmap = message.getProperties();
        
        def body = message.getBody(java.lang.String) as String;

        def headers = message.getHeaders() as Map<String, Object>;
        def properties = message.getProperties() as Map<String, Object>;
        
        def propertiesAsString ="\n";
        properties.each{ iterator -> propertiesAsString = propertiesAsString + "${iterator}" + "\n" };
         
        def headersAsString ="\n";
        headers.each{ iterator -> headersAsString = headersAsString + "${iterator}" + "\n" };
        
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


