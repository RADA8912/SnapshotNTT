import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.op.agent.mpl.*;
import groovy.transform.Field;

@Field String FULFILLMENT_REQUEST_ID = 'Fulfillment Request ID';

def Message setHeaders(Message message) {
	String fulfillmentRequestId = message.getProperties().get("externalDocumentId");
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
		// Add custom header for property 'fulfillmentRequestId' using the same text as in CALM
		messageLog.addCustomHeaderProperty(FULFILLMENT_REQUEST_ID, fulfillmentRequestId);
    }
    return message;
}