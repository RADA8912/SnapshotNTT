import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.UserCredential

def Message processData(Message message) {
    def credentialName = message.getProperty("signavioCredential")
    def signavioTenantId = message.getProperty("signavioTenantId")
    
    SecureStoreService secureStoreService = ITApiFactory.getService(SecureStoreService.class, null)
    UserCredential userCredential = secureStoreService.getUserCredential(credentialName)
    def username = userCredential.getUsername().toString()
    def password = userCredential.getPassword().toString()
    
    message.setBody("tokenonly=true&name=" + username + "&password=" + password + "&tenant=" + signavioTenantId)
    return message;
}
