/*
 * The integration developer needs to create the method processData 
 * This method takes Message object of package com.sap.gateway.ip.core.customdev.util
 * which includes helper methods useful for the content developer:
 * 
 * The methods available are:
    public java.lang.Object getBody()
    
    //This method helps User to retrieve message body as specific type ( InputStream , String , byte[] ) - e.g. message.getBody(java.io.InputStream)
    public java.lang.Object getBody(java.lang.String fullyQualifiedClassName)

    public void setBody(java.lang.Object exchangeBody)

    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()

    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)

    public void setHeader(java.lang.String name, java.lang.Object value)

    public java.util.Map<java.lang.String,java.lang.Object> getProperties()

    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 

	public void setProperty(java.lang.String name, java.lang.Object value)
 * 
 */
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataStreamGenerator;
import org.bouncycastle.util.encoders.Base64;

def Message getCredentials(Message message) {
	
    def properties = message.getProperties();
    def credentials_alias = properties.get("CredentialsAlias");
	
    def service = ITApiFactory.getApi(SecureStoreService.class, null);
	def credential = null;
	try {
		credential = service.getUserCredential( credentials_alias );
	} catch (all) {
		throw new Exception("No credentials found for alias " + credentials_alias );
	}

	//Get credential from keystore
    String userName = credential.getUsername();
    //String password = new String(credential.getPassword());
    
    message.setProperty("UserName", userName);
    message.setProperty("Password", (new String(credential.getPassword())));
    
    // get Delegate credentials - only Productive use	
	if (properties.get("Mode") != "TEST"){
    	def credentials_delegate_alias = properties.get("CredentialsDelegateAlias");
    	
       // def service = ITApiFactory.getApi(SecureStoreService.class, null);
    	def credential_delegate = null;
    	try {
    		credential_delegate = service.getUserCredential( credentials_delegate_alias );
    	} catch (all) {
    		throw new Exception("No credentials found for alias " + credentials_delegate_alias );
    	}
    
    	//Get credential from keystore
        String userName_delegate = credential_delegate.getUsername();
        //String password_delegate = new String(credential_delegate.getPassword());
        
        message.setProperty("UserNameDelegate", userName_delegate);
        message.setProperty("PasswordDelegate", (new String(credential_delegate.getPassword())));
	}

	
	return message;

}

def Message getP7M(Message message) {

	 properties = message.getProperties();
     String unsignedDocument = properties.get("UnsignedDocument");         							
     String binarySign = properties.get("BinarySign");     
     ByteArrayOutputStream signedDocument = new ByteArrayOutputStream();  
          
	 // decode base64 signature     
     byte[] derSignature = Base64.decode(binarySign);   
     
     // parse the signature to bc object       
     CMSSignedData sd = null;
     OutputStream dataOut = null;
     try {
        sd = new CMSSignedData(new CMSProcessableByteArray(null), derSignature);
        if (sd == null) {
            throw new Exception("Envelope format unknown or not implemented");
        }

        // build the p7m generator and add signature data to it
        CMSSignedDataStreamGenerator gen = new CMSSignedDataStreamGenerator();
        gen.addSigners(sd.getSignerInfos());
        gen.addCertificates(sd.getCertificates());
        gen.addCRLs(sd.getCRLs());

        // bundle the unsignedDocument with the BinarySign into the signedDocument
        ByteArrayInputStream inputStream = new ByteArrayInputStream(unsignedDocument.getBytes(StandardCharsets.UTF_8));    
        dataOut = gen.open(signedDocument, true);
        org.bouncycastle.util.io.Streams.pipeAll(inputStream, dataOut);
    } catch (Exception ex) {
        throw new Exception(ex.getMessage());
    } finally {
        if (dataOut != null) {
			dataOut.close();
        }
    }
    message.setBody(signedDocument);
              
	return message;
}



