import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.it.api.securestore.exception.SecureStoreException;
def Message processData(Message message) {
 //Body 
 def body = message.getBody(java.lang.String) as String
 def service = ITApiFactory.getApi(SecureStoreService.class, null) 
 if( service != null) {
 def jsonSlurper = new JsonSlurper()
 def requestObject = jsonSlurper.parseText(body)
 def aliasname = requestObject.alias 
 def credential = service.getUserCredential(aliasname)
 if (credential != null) {
 def username = credential.getUsername()
 def password = credential.getPassword()
 def properties = credential.getCredentialProperties()
 String passwordString = "" 
 if (password.getClass().isArray()) {
 if (password instanceof char[]) {
 passwordString = new String((char[]) password);
 }
 } 
 def json = JsonOutput.toJson([username: username, password: passwordString, properties: properties])
 message.setBody(json)
 }
 } 
 return message;
}