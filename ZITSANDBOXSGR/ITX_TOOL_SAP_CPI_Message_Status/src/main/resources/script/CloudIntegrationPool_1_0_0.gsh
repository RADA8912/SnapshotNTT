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