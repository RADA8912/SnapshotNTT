import com.sap.gateway.ip.core.customdev.util.Message

/**
* SetTenantSystemID
* Sets tenant name to exchange property.
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Set tenant system name
	String systemID = System.env['IT_SYSTEM_ID']

	//Set value to exchange property
	message.setProperty("SystemID", systemID)

	return message
}