import com.sap.it.api.mapping.*
import com.sap.it.api.ITApiFactory

/**
 * Get value mapping by given source value. Source value will changed to lower case. First argument is source value. Second argument is source agency.
 * Third argument is source identifier (schema).Fourth argument is target agency. Fifth argument is target identifier (schema).
 * Execution mode: Single value
 *
 * @param sourceValue Source value (key)
 * @param sourceAgency Source agency
 * @param sourceIdentifier Source Identifier (schema)
 * @param targetAgency Target agency
 * @param targetIdentifier Target identifier (schema)
 * @return target value.
 */
public def String dynamicVMLowerCase(String sourceValue, String sourceAgency, String sourceIdentifier, String targetAgency, String targetIdentifier) {
	// Change sourceValue to lower case
	sourceValue = sourceValue.toLowerCase()

	def service = ITApiFactory.getApi(ValueMappingApi.class, null)

	if(service != null) {
		String targetValue = service.getMappedValue(sourceAgency, sourceIdentifier, sourceValue, targetAgency, targetIdentifier)
		return targetValue
	} else {
		return null
	}
}

/**
 * Get value mapping by given source value. This function uses HTTP-header keys ('source-agency', 'source-identifier', 'target-agency' and 'target-identifier').
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
		throw new Exception("Custom Function exceptionIfNoVMHeader: You need to set HTTP-header keys 'source-agency', 'source-identifier', 'target-agency' and 'target-identifier'.")
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
