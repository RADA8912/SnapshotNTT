import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.transform.Field;
import groovy.json.JsonOutput;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.mapping.ValueMappingApi;

@Field String ERROR_SHORTTEXT = 'ERROR';
@Field String SUCCESS_SHORTTEXT = 'SUCCESS';
@Field String WARNING_SHORTTEXT = 'WARNING';

@Field String STATUS_DELIVERY_A = 'NoDeliveryCreated';
@Field String STATUS_DELIVERY_B = 'PartialDeliveryCreated';
@Field String STATUS_DELIVERY_C = 'FullDeliveryCreated';

@Field String STATUS_PROCESS_A = 'Open';
@Field String STATUS_PROCESS_B = 'PartiallyCompleted';
@Field String STATUS_PROCESS_C = 'Completed';

@Field String WARNING_NO_MESSAGES = 'Message list could not be retrieved.';
@Field String UNKNOWN_ERROR = 'An unknown error occurred.';

class ResponseMsg {
	String type
	String message
}

class OrderItem {
	String itemLineNumber
	String fulfillmentRequestItemId
	String deliveryStatus
	String processStatus
	Boolean rejected
	String rejectionReasonCode
}

def Message confirmationResponse(Message message) {	
	def headers = message.getHeaders();
	headers.put("Content-Type",   "application/json");
	
	def properties = message.getProperties();	
	def response = [:];		
	response["documentNumber"] = properties.get("salesOrderId");
	response["fulfillmentRequestId"] = properties.get("externalDocumentId");
	
	List<ResponseMsg> respMsgs = [];		
	try {			
		def messageList = properties.get("messageList");		
		int length = messageList.getLength();		
		for (i = 0; i < length; i++) { 
			respMsgs.add(convertS4Message((Element)messageList.item(i)));                            
		}					
	} catch (Exception ex) { 	
		respMsgs.add(new ResponseMsg(type:WARNING_SHORTTEXT,message:WARNING_NO_MESSAGES));
		String exceptionText = 'Exception: ' + ex.getMessage();
		respMsgs.add(new ResponseMsg(type:WARNING_SHORTTEXT,message:exceptionText));			
	}
	if (respMsgs.size() > 0){
		response["messages"] = respMsgs;
	}
	
	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(response)));		
	return message;	
}

def Message notificationResponse(Message message) {
	def headers = message.getHeaders();
	headers.put("Content-Type",   "application/json");
		
	def properties = message.getProperties();	
	def response = [:];			
	response["fulfillmentSystemId"] = properties.get("fulfillmentSystemId");
	response["documentNumber"] = properties.get("salesOrderId");
	response["fulfillmentRequestId"] = properties.get("externalDocumentId");		
	response["documentLastChangedDateTime"] = properties.get("documentLastChangedDateTime");

	List<OrderItem> items = [];	 		

	def orderItemList = properties.get("orderItemList");		
	int length = orderItemList.getLength();		
	for (i = 0; i < length; i++) { 
		items.add(convertItem((Element)orderItemList.item(i)));                            
	}
	if (items.size() > 0) {
		response["items"] = items;
	}		
	
	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(response)));		
	return message;	
}

def Message exceptionDetails(Message message) {	
	
	def properties = message.getProperties();
	
	String errorMessage = "";	
	def exceptionProperty;
	
	def response = [:];		
	List<ResponseMsg> respMsgs = [];	
	
	try { 
		exceptionProperty = properties.get("CamelExceptionCaught");	
	
		// an http adapter throws an instance of org.apache.camel.component.ahc.AhcOperationFailedException
		if (exceptionProperty.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
			errorMessage = exceptionProperty.getResponseBody();	
		} 				
	
		if (!errorMessage.trim()) {
			errorMessage = exceptionProperty.message;
		}
	} catch (Exception ex) {
		
	}
		
	errorMessage = (errorMessage.trim()) ? errorMessage.replaceAll("\\P{Print}", "") : UNKNOWN_ERROR;			
	
	respMsgs.add(new ResponseMsg(type:ERROR_SHORTTEXT,message:errorMessage));	
	response["messages"] = respMsgs;		
	
	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(response)));		
	return message;	
}

def ResponseMsg convertS4Message(def msg){  
	String severity = msg.getElementsByTagName("SeverityCode").item(0).getTextContent();	
	String msgType = "";
	switch(severity) {
		case '1':
			msgType = SUCCESS_SHORTTEXT;
			break
		case '2':
			msgType = WARNING_SHORTTEXT;
			break
		case '3':
			msgType = ERROR_SHORTTEXT;
			break		
	}	
	String retMessage = msg.getElementsByTagName("Note").item(0).getTextContent();	             
	return new ResponseMsg(type:msgType,message:retMessage);   
}

def OrderItem convertItem(def orderItem){  
	String itemLineNumber = orderItem.getElementsByTagName("SalesOrderItemID").item(0).getTextContent();	 
	String fulfillmentRequestItemId = orderItem.getElementsByTagName("ExternalItemID").item(0).getTextContent();	       
	Element statusElement = (Element)orderItem.getElementsByTagName("Status").item(0);	
	String deliveryStatus = getDeliveryStatusText(statusElement.getElementsByTagName("DeliveryStatus").item(0).getTextContent());
	String processStatus = getProcessStatusText(statusElement.getElementsByTagName("SDProcessStatus").item(0).getTextContent());
	Boolean rejected = getRejectionStatusBool(statusElement.getElementsByTagName("SDDocumentRejectionStatus").item(0).getTextContent());
	
	String rejectionReason = null;	
	//check for the rejection reason if "rejected" flag is "true" (in S4/HANA, an item is rejected by setting the rejection reason).
	if (rejected) {		
		rejectionReason = getValueMappingRejectionReason(orderItem.getElementsByTagName("SalesDocumentRjcnReason").item(0).getTextContent());				
	}
	
	return new OrderItem(itemLineNumber:itemLineNumber,fulfillmentRequestItemId:fulfillmentRequestItemId,deliveryStatus:deliveryStatus,processStatus:processStatus,rejected:rejected,rejectionReasonCode:rejectionReason);   	
}

def String getDeliveryStatusText(String statusCode) {
	String statusText = null;
	
	switch(statusCode) {
		case 'A':
			statusText = STATUS_DELIVERY_A;
			break
		case 'B':
			statusText = STATUS_DELIVERY_B;
			break
		case 'C':
			statusText = STATUS_DELIVERY_C;
			break		
	}	
	return statusText;
}

def String getProcessStatusText(String statusCode) {
	String statusText = null;
	
	switch(statusCode) {
		case 'A':
			statusText = STATUS_PROCESS_A;
			break
		case 'B':
			statusText = STATUS_PROCESS_B;
			break
		case 'C':
			statusText = STATUS_PROCESS_C;
			break		
	}	
	return statusText;
}

def Boolean getRejectionStatusBool(String statusCode) {	
	Boolean statusBool = null;
	if (statusCode.equals('C')) {
		statusBool = true;
	}
	if (statusCode.equals('A')) {
		statusBool = false;
	}	
	return statusBool;	
}

def String getValueMappingRejectionReason(String rejectionReasonCode) {
		
	// retrieve rejection reason value mapping api instance		
	def valueMapApi = ITApiFactory.getService(ValueMappingApi.class, null);		
	
	//returns mapped value if mapping is found
	//returns null, if:
	//1st scenario: value not found in mapping
	//2st scenario: value mapping artifact not deployed
	String rejectionReasonText = valueMapApi.getMappedValue('S/4HANA', 'RejectionReason', rejectionReasonCode, 'OrderManagement', 'RejectionReason');
	
	return rejectionReasonText?rejectionReasonText:rejectionReasonCode;
}









