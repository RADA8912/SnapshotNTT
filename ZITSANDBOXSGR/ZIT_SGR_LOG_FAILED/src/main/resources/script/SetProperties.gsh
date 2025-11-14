import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
	String appUrl = System.getenv("HC_APPLICATION_URL")
	String tenantID = appUrl.substring(8,13)
	
	// Set Properties
	message.setProperty("TenantID", tenantID)
	message.setProperty("TenantURL", "https://" + tenantID + "-tmn.hci.eu1.hana.ondemand.com")
	
	return message;	
}