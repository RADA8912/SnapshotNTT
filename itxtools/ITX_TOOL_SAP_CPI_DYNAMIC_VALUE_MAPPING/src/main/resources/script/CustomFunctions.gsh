import com.sap.it.api.mapping.*
import com.sap.it.api.ITApiFactory

/**
 * Get value mapping by given source value. This function uses header values ('source-agency', 'source-identifier', 'target-agency' and 'target-identifier').
 * Set Integration Flow/Runtime/Allow Header(s) = 'source-agency|source-identifier|target-agency|target-identifier' to get header values.
 * Execution mode: Single value
 *
 * @param sourceValue Source value (key)
 * @return target value.
 */
public def String dynamicVMHeader(String sourceValue, MappingContext context) {
	// Get header values
	String sourceAgency = context.getHeader("source-agency")
    String sourceIdentifier = context.getHeader("source-identifier")
    String targetAgency = context.getHeader("target-agency")
    String targetIdentifier = context.getHeader("target-identifier")    
    
	if (sourceAgency == null || sourceAgency.length() == 0 || sourceIdentifier == null || sourceIdentifier.length() == 0 ||
	    targetAgency == null || targetAgency.length() == 0 || targetIdentifier == null || targetIdentifier.length() == 0) {
		throw new Exception("Custom Function exceptionIfNoVMHeader: You need to set header values 'source-agency', 'source-identifier', 'target-agency' and 'target-identifier'.")
	}
	
	// Get value mapping
	def service = ITApiFactory.getApi(ValueMappingApi.class, null)

	if(service != null) {
		String targetValue = service.getMappedValue(sourceAgency, sourceIdentifier, sourceValue, targetAgency, targetIdentifier)
		return targetValue
	} else {
		return null
	}
}

/**
 * Sets a value to header.
 * There is an empty response in Message Mapping 'Display Queue' or 'Simulation' because message header is not accessible.
 * Execution mode: Single value
 *
 * @param headerName header name
 * @param headerValue header value
 * @param context Mapping context
 * @return a header value.
 */
public def String setHeader(String headerName, String headerValue, MappingContext context) {
	def returnValue = context.setHeader(headerName,headerValue)
	return returnValue
}