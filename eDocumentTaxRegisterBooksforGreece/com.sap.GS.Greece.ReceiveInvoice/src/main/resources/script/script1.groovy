/*
 The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
	public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
    public void setProperty(java.lang.String name, java.lang.Object value)
    public java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> getSoapHeaders()
    public void setSoapHeaders(java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> soapHeaders) 
       public void clearSoapHeaders()
 */
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
       //Properties 
       def map = message.getProperties();
       def op_mode = map.get("Mode");
       def testURL = map.get("Test_URL");
       def prodURL = map.get("Prod_URL");
       def vatID   = map.get("AFM");
       def receiver_endpoint;
       def credentials_alias;
       def service = ITApiFactory.getApi(SecureStoreService.class, null);
	   def credential = null;
	   def errorMsg;
	   
       if(op_mode == "TEST"){
           receiver_endpoint = testURL;
           credentials_alias = "edoc_greece_myDATA_test_" + vatID;
       }else if(op_mode == "PROD") {
           receiver_endpoint = prodURL;
           credentials_alias = "edoc_greece_myDATA_prod_" + vatID;
       }else{
           errorMsg = "Communication Mode: " + op_mode + " is incorrect; Only 'TEST' or 'PROD' are allowed";
           message.setHeader("Error", errorMsg);
           throw new Exception(errorMsg);
       }
       
    	credential = service.getUserCredential( credentials_alias );
        if(credential == null) {
            errorMsg = "No credentials maintained for VAT ID " + vatID;
            message.setHeader("Error", errorMsg);
	        throw new IllegalStateException(errorMsg);
        }

       //Get credential from keystore
       message.setHeader("aade-user-id", credential.getUsername());
       message.setHeader("Ocp-Apim-Subscription-Key", (new String(credential.getPassword())));
       message.setProperty("receiver_endpoint", receiver_endpoint);
       return message;
}