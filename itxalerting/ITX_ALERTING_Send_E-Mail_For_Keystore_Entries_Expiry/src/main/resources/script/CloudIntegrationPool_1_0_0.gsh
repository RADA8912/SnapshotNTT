/**
* Custom Functions Cloud Integration Pool
*
* @author itelligence.de
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Sets a value to header
 * Execution mode: Context
 *
 * @param Header value
 * @param context Mapping context
 * @return Returns a header value.
 */
def String setHeader(String headerName,String headerValue,MappingContext context) {

	def returnValue = context.setHeader(headerName,headerValue)
	return returnValue
}

/**
 * Gets tenant URL
 * Execution mode: Context
 *
 * @param input value
 * @param context Mapping context
 * @return Returns tenant URL.
 */
def String getTenantURL(String input) {
    //Get tenant url
	String appUrl = System.getenv("HC_APPLICATION_URL")
	appUrl = appUrl.substring(0,13) + "-tmn.hci."
    appUrl = appUrl + System.getenv("HC_REGION").replaceAll('_','')
    appUrl = appUrl + "." + System.getenv("HC_HOST")
    appUrl = appUrl.toLowerCase()
	
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
    //Get tenant ID
	String appUrl = System.getenv("HC_APPLICATION_URL")
	appId = appUrl.substring(8, appUrl.indexOf("ifl"))
	
	return appId
}

/**
 * Get 30 days from now
 * Execution mode: Context
 *
 * @param input value (not used)
 * @return Returns date now add 30 days.
 */
def String get30DaysFromNow(String input) {
    use(groovy.time.TimeCategory) {
        def dateNew = new Date() + 30.days
        date = dateNew.format("yyyy-MM-dd")
    }
    
	return date
}