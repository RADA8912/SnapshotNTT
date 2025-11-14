import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.op.agent.mpl.*;
import groovy.json.JsonSlurper;
import groovy.transform.Field;

@Field String FULFILLMENT_REQUEST_ID = 'Fulfillment Request ID';
@Field String ORDER_ID = 'Order ID';

def Message setHeaders(Message message) {
	def body = message.getBody(java.lang.String);
	// Parse message body as JSON
	def bodyJson = new JsonSlurper().parseText(body);
	// Get orderId
	String orderId = bodyJson.orderId;
	// Get fulfillmentRequestId
	String fulfillmentRequestId = bodyJson.fulfillmentRequestId;
	// Set fulfillmentRequestId value in preset header 'SAP_ApplicationID' for monitoring and search
	message.setHeader("SAP_ApplicationID", fulfillmentRequestId);
	
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		// Add custom headers for properties 'orderId' and 'fulfillmentRequestId' using the same title text as in CALM
		messageLog.addCustomHeaderProperty(FULFILLMENT_REQUEST_ID, fulfillmentRequestId);
		messageLog.addCustomHeaderProperty(ORDER_ID, orderId);
    }
    return message;
}
