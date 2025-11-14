import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

/**
* SetTenantURL
* Sets tenant url to header.
*/

def Message processData(Message message) {
	String appUrl = ""

	// Get tenant url
	// First check Cloud Foundry
	String tenantName = System.getenv('TENANT_NAME')	
	String tenantID = System.getenv('IT_SYSTEM_ID')
	String tenantDomain = System.getenv('IT_TENANT_UX_DOMAIN')
	appUrl = "https://" + tenantName + "." + tenantID + "-rt." + tenantDomain
	
	if (appUrl == null || appUrl.length() <= 25) {
		// Then check Neo
		appUrl = System.getenv("HC_APPLICATION_URL")

		// Create new tenant application url
		appUrl = appUrl.substring(0, appUrl.indexOf("ifl")) + "-iflmap."

		// Add landscape
		String landscape = System.getenv("HC_LANDSCAPE")
		if (landscape.equals("production")) {
			appUrl = appUrl + "hcisbp."
		} else {
			appUrl = appUrl + "hcisbt."	
		}

		// Add region
		String region = System.getenv("HC_REGION").replaceAll('_','')
		if (!appUrl.contains(region)) {	
			appUrl = appUrl + System.getenv("HC_REGION").replaceAll('_','') + "."
		}

		// Add host
		appUrl = appUrl + System.getenv("HC_HOST")	
		appUrl = appUrl.toLowerCase()
	}

	//Set value to header
	message.setHeader("AppUrl", appUrl)

	return message
}