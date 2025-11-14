import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.UserCredential
import com.sap.it.api.securestore.exception.SecureStoreException
import com.sap.it.api.ITApiFactory

/**
* GetUserCredentials
* This Groovy script gets the secure parameter for a specific alias from the security material. For example, to get a stored API key from the cloud integration security material.
*
* Groovy script parameters (exchange properties)
* - GetUserCredentials.Alias = name of the credential in the security materials
*
* Groovy script read only exchange property
* - GetUserCredentials.Password = user credentials password
* - GetUserCredentials.Username = user credentials username
*
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	// Get exchange properties
	String secureParameterAlias = getExchangeProperty(message,'GetUserCredentials.Alias', true)
	
	// Define secure store service
	def secureStorageService =  ITApiFactory.getService(SecureStoreService.class, null)
	
	try{
		// Get the user credentials by apikey_alias  
		def secureParameter = secureStorageService.getUserCredential(secureParameterAlias)
		// Get the api key 
		def password = secureParameter.getPassword().toString()
		def username = secureParameter.getUsername().toString()
		
		message.setProperty("GetUserCredentials.Password", password)
		message.setProperty("GetUserCredentials.Username", username)
		
	} catch(Exception e){
		throw new SecureStoreException("Secure Parameter '$secureParameterAlias' not available.")
	}
	return message
}

/**
 * getExchangeProperty
 * @param message This is message.
 * @param propertyName This is name of property.
 * @param mandatory This is parameter if property is mandatory.
 * @return propertyValue Return property value.
 */
private getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (propertyValue == null || propertyValue.length() == 0) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}