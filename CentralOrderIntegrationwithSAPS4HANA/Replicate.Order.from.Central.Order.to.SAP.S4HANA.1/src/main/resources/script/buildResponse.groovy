import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.transform.Field;
import groovy.json.JsonOutput;

@Field String ERROR_SHORTTEXT = 'ERROR';
@Field String INFO_SHORTTEXT = 'INFO';
@Field String GENERIC_ERROR_MESSAGE = 'A technical error occurred. Use the SAP Cloud Integration message ID to find additional error details.';
@Field String CPI_MSGID_TEXT = 'SAP Cloud Integration message ID: ';

class ResponseMsg {
	String type
	String message
}

def Message normalResponse(Message message) {
	buildResponse(message, false);
}

def Message exceptionResponse(Message message) {
	buildResponse(message, true);
}

def Message buildResponse(Message message, boolean isException) {	
	def properties = message.getProperties();
	
	def headers = message.getHeaders();
	headers.put("Content-Type",   "application/json");
	
	def response = [:];
	List<ResponseMsg> respMsgs = [];
	
	if (isException) {

		headers.put("CamelHttpResponseCode", "400");

		String messageString = "";
		def exceptionCause;

		try {
		    exceptionCause = properties.get("CamelExceptionCaught").getCause();
		} catch(Exception ex){
		}
		
		messageString = (exceptionCause) ? (exceptionCause.getMessage()): "";
		messageString = (messageString.trim()) ? messageString.replaceAll("\\P{Print}", "") : "";
		
		try {
			if (!exceptionCause) {
				messageString = properties.get("CamelExceptionCaught").message.replaceAll("\\P{Print}", "");
			}
		} catch (Exception ex) {
		}

		if (!messageString.trim()) {
			messageString = GENERIC_ERROR_MESSAGE;
		}

		respMsgs.add(new ResponseMsg(type:ERROR_SHORTTEXT,message:messageString));
	}

	//Add CPI MsgID to messages
	respMsgs.add(new ResponseMsg(type:INFO_SHORTTEXT,message:CPI_MSGID_TEXT + properties.get("SAP_MessageProcessingLogID")));
	response["messages"] = respMsgs;

	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(response)));

	return message;

}