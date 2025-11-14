/**
* Custom Functions Cloud Integration Pool
*
* @author itelligence.de
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Gets a value from header
 * Execution mode: Context
 *
 * @param Header value
 * @param context Mapping context
 * @return Returns a header value.
 */
public def String getHeader(String headerName,MappingContext context) {

	def returnValue = context.getHeader(headerName)
	return returnValue
}

/**
 * Gets a value from header
 * Execution mode: Context
 *
 * @param input value
 * @param context Mapping context
 * @return Returns tenant URL.
 */
public def String getTenantURL(String input) {
	String appUrl = ""
	
	// Get tenant url
	// First check Cloud Foundry
	String tenantName = System.getenv('TENANT_NAME')	
	String tenantID = System.getenv('IT_SYSTEM_ID')
	String tenantDomain = System.getenv('IT_TENANT_UX_DOMAIN')
	appUrl = "https://" + tenantName + "." + tenantID + "." + tenantDomain

	if (appUrl == null || appUrl.length() <= 22) {
		// Then check Neo
		appUrl = System.getenv("HC_APPLICATION_URL")
		
		// Create new tenant url
		appUrl = appUrl.substring(0, appUrl.indexOf("ifl")) + "-tmn.hci."
		
		// Add region
		String region = System.getenv("HC_REGION").replaceAll('_','')
		if (!appUrl.contains(region)) {	
			appUrl = appUrl + System.getenv("HC_REGION").replaceAll('_','') + "."
		}
		
		// Add host	
		appUrl = appUrl + System.getenv("HC_HOST")
		appUrl = appUrl.toLowerCase()
	}
	
	return appUrl
}

/**
 * Gets date now added one hour
 * Execution mode: Context
 *
 * @param input value (not used)
 * @return Returns date now added one hour.
 */
public def String getOneHourFromNow(String input) {
    use(groovy.time.TimeCategory) {
        def dateNew = new Date() + 1.hour
        date = dateNew.format("yyyy-MM-dd HH:mm:ss")
    }
    
	return date
}

/**
 * Removes the trailing characters of the first argument leaving the number of characters given by the second argument. Attention: white spaces are significant, no trim is in action.
 * Execution mode: Single value
 *
 * @param value Value
 * @param headLength Head length
 * @return head string of input value.
 */
public def String headString(String value, String headLength){
	String output = null
	if (value != null && headLength != null && headLength.length() > 0) {
		int headLengthInt
		try {
			headLengthInt = Integer.parseInt(headLength)
		} catch (NumberFormatException numberFormatExp) {
			throw new RuntimeException("Custom Function headString: could not convert headLength '" + headLength + "' to integer.")
		}

		// DO NOT trim: in some cases the trailing whitespaces may be significant
		int length = value.length()
		if (length > headLengthInt) {
			output = value.substring(0, headLengthInt)
		} else {
			output = value
		}
	} else {
		output = value
	}

	return output
}