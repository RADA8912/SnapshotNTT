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