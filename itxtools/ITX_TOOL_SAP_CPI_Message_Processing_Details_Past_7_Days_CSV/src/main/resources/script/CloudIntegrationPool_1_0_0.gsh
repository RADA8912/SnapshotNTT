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
def String getHeader(String headerName,MappingContext context) {
	def returnValue = context.getHeader(headerName)
	return returnValue
}

/**
 * Gets a value from header
 * Execution mode: Context
 *
 * @param input value (not used)
 * @param context Mapping context
 * @return Returns tenant URL.
 */
def String getTenantURL(String input) {
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
* Gets tenant ID
* Execution mode: Context
*
* @param input value
* @param context Mapping context
* @return Returns tenant ID.
*/
def String getTenantID(String input) {
	// Set tenant ID
	// First check Cloud Foundry
	String tenantID = System.getenv('IT_SYSTEM_ID')
	if (tenantID != null && tenantID.length() > 0) {
		tenantID = tenantID.split("-")[1]
	} else {
		// Then check Neo
		String appUrl = System.getenv("HC_APPLICATION_URL")
		tenantID = appUrl.substring(8, appUrl.indexOf("ifl"))
	}
	
	return tenantID
}

/**
 * Gets date now added one hour
 * Execution mode: Context
 *
 * @param input value (not used)
 * @return Returns date now added one hour.
 */
def String getOneHourFromNow(String input) {
    use(groovy.time.TimeCategory) {
        def dateNew = new Date() + 1.hour
        date = dateNew.format("yyyy-MM-dd HH:mm:ss")
    }
    
	return date
}