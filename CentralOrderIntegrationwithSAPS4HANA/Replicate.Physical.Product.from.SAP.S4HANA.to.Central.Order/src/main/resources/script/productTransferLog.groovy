import com.sap.gateway.ip.core.customdev.util.Message
import groovy.transform.Field
import groovy.json.*
import src.main.resources.script.ProductLoggingUtils


@Field String PRODUCT_ERROR_LOG 			= '- Product Error Log'
@Field String INCOMING_IDOC_LOG 			= '1. Incoming IDoc'
@Field String PRE_EXIT_BEFORE_LOG  			= 'Pre Exit A'
@Field String PRE_EXIT_AFTER_LOG  			= 'Pre Exit B'
@Field String XML_MAPPING_RESULT_LOG 		= '3. Mapping Result'
@Field String POST_EXIT_BEFORE_LOG 			= 'Post Exit A'
@Field String POST_EXIT_AFTER_LOG  			= 'Post Exit B'
@Field String XML_TO_JSON_RESULT_LOG		= '4. XML to JSON Result'
@Field String UPSERT_PAYLOAD_LOG 			= '5. Upsert Payload'
@Field String PRODUCT_TRANSFER_LOG 			= '6. Product Transfer Log'
@Field String FORMAT_XML	= 'application/xml'

// - Log Product Errors if there's any replication errors (redis lock, validation error etc.)
def Message outputProductErrorLog(Message message) {
	def productError = message.getProperty("productError");
	if (productError.size > 0) {
	    def log = new JsonSlurper().parseText('{}');
        log.put("Products with errors", productError);
        this.getLoggingUtils().outputToLog(message, PRODUCT_ERROR_LOG, JsonOutput.prettyPrint(JsonOutput.toJson(log)));    
	}
    return message;
}

// 1. Log Incoming IDOC
def Message logIncomingIDoc(Message message) {
	this.getLoggingUtils().outputDebugLog(message, INCOMING_IDOC_LOG, this.getLoggingUtils().prettyPrintXML(message), FORMAT_XML);
	return message;
}

// Pre Exit A
def Message logPreExitBefore(Message message) {
	this.getLoggingUtils().outputDebugLog(message, PRE_EXIT_BEFORE_LOG, this.getLoggingUtils().prettyPrintXML(message), FORMAT_XML );
	return message;
}

// Pre Exit B
def Message logPreExitAfter(Message message) {
	this.getLoggingUtils().outputDebugLog(message, PRE_EXIT_AFTER_LOG, this.getLoggingUtils().prettyPrintXML(message), FORMAT_XML );
	return message;
}

// Post Exit A
def Message logPostExitBefore(Message message) {
	this.getLoggingUtils().outputDebugLog(message, POST_EXIT_BEFORE_LOG, this.getLoggingUtils().prettyPrintXML(message), FORMAT_XML );
	return message;
}

// Post Exit B
def Message logPostExitAfter(Message message) {
	this.getLoggingUtils().outputDebugLog(message, POST_EXIT_AFTER_LOG, this.getLoggingUtils().prettyPrintXML(message), FORMAT_XML );
	return message;
}

// 3. Log Mapping Result
def Message logMappingResult(Message message) {
	this.getLoggingUtils().outputDebugLog(message, XML_MAPPING_RESULT_LOG, this.getLoggingUtils().prettyPrintXML(message), FORMAT_XML );
	return message;
}

// 4. Log XML to JSON Result
def Message logXmlToJsonResult(Message message) {
	this.getLoggingUtils().outputDebugLog(message, XML_TO_JSON_RESULT_LOG, this.getLoggingUtils().prettyPrintJson(message));
	return message;
}

// 5. Log Upsert Payload
def Message logUpsertPayload(Message message) {
	this.getLoggingUtils().outputDebugLog(message, UPSERT_PAYLOAD_LOG, this.getLoggingUtils().prettyPrintJson(message));
	return message;
}

// 6. Log Product Transfer Output
def Message outputProductTransferLog(Message message) { 
	this.getLoggingUtils().outputToLog(message, PRODUCT_TRANSFER_LOG, this.getLoggingUtils().prettyPrintJson(message));
	return message;
}

def ProductLoggingUtils getLoggingUtils() {
	return new ProductLoggingUtils(messageLogFactory)
}

// Process Response
def Message processResponse(Message message) {
	def body = message.getBody(java.lang.String) as String; //response from product service
	
	def productError = message.getProperty("productError");
	
	def errors = [];
	def successes = [];
	def log = new JsonSlurper().parseText('{}');
	
	if (productError instanceof String) {
	    productError = new ArrayList();
	    message.setProperty("productError", productError);
	}
	
	def responses = new JsonSlurper().parseText(body);

    def headers = message.getHeaders();
    def httpStatus = headers.get("CamelHttpResponseCode");
    log.put("HttpStatusCode: ", httpStatus);
    
    if (httpStatus >= 400){
        def error = new JsonSlurper().parseText('{}');
        error.put("message":"Something went wrong!");
        errors.push(error);
    }

    def batchResponseProducts = responses.batchResponseProducts;

	if (batchResponseProducts instanceof Collection) { // request error message
		batchResponseProducts.each { response ->
			if (response.status == "Error") {
				def error = new JsonSlurper().parseText('{}');
				// get displayId
                error.put("displayId", response.displayId);
                error.put("code", response.error.code);
                error.put("message", response.error.message);
				productError.add(error);
			}
			else {
				def success = new JsonSlurper().parseText('{}');
				success.put("id", response.id);
				success.put("displayId", response.displayId);
				success.put("name", response.name);
				success.put("status", response.status);
				successes.push(success);
			}
		}
	}
	else {
		errors.push(responses);
	}

	if (errors.size > 0) {
		message.setProperty("hasErrors", true);
	}
	
    log.put("Product Replication Response : ",responses);
	message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(log)));
	message.setProperty("productError", productError);
	

	return message;
}

// Exceptions
def Message raiseIDocTypeNotSupportedException(Message message) { 
	throw new ProductTransferException("IDoc transfer failed, because the IDoc type included in the request is not supported. Please send IDocs of type ARTMAS (for articles) or MATMAS (for materials).");
}

def Message raiseProductTransferException(Message message) { 
	throw new ProductTransferException("One or more error(s) occurred during transfer. Please look at the Logs for more details.");
}

public class ProductTransferException extends Exception { 
	public ProductTransferException(String message) { 
		super(message);
	}
}
