import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetTenantURL
* Sets tenant url to exchange property.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Get tenant url
	String tenantName = System.env['TENANT_NAME']
	String systemID = System.env['IT_SYSTEM_ID']
	String tenantDomain = System.env['IT_TENANT_UX_DOMAIN']
	
	String appUrl = "https://" + tenantName + "." + systemID + "." + tenantDomain

	//Set value to exchange property
	message.setProperty("AppUrl", appUrl)

	return message
}