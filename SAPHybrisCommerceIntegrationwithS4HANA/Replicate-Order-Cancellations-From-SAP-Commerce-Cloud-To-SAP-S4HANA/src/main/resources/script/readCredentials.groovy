
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import javax.xml.bind.DatatypeConverter;

/********************************************/
/***  Read S/4HANA Credentials from SCPI  ***/
/********************************************/
    
def Message processData(Message message) {
  
     def messageLog = messageLogFactory.getMessageLog(message);
     messageLog.setStringProperty("Info1", "ReadLoginCredentials Script Called...!");

    def properties = message.getProperties() as Map<String, Object>;
	def userAlias = properties.get("username");
	
	def service = ITApiFactory.getApi(SecureStoreService.class, null);
	def credential = service.getUserCredential(userAlias);
	
    if (credential == null){
       throw new IllegalStateException("No credential found for alias " + userAlias);
    }
    
    messageLog.setStringProperty("Info2", "S/4HANA Credentials Retrieved from SCPI!");
    
    String userName = credential.getUsername();
    String password = new String(credential.getPassword());
   
    def credentials = userName + ":" + password;
    def byteContent = credentials.getBytes("UTF-8");
   
    // Construct the login authorization in Base64
    def auth = DatatypeConverter.printBase64Binary(byteContent);
    message.setHeader("Authorization", "Basic " + auth);
  
   return message;
  
}