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
 * Gets a value from a property
 * Execution mode: Context
 *
 * @param Property value
 * @param context Mapping context
 * @return Returns a property value.
 */
def String getProperty(String propertyName,MappingContext context) {

	def returnValue = context.getProperty(propertyName)
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