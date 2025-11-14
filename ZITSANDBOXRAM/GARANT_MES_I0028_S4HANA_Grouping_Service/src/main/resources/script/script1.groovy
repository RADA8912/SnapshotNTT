import org.apache.commons.io.FileUtils;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.examples.ByteArrayHandler;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.nio.charset.Charset;

import com.sap.gateway.ip.core.customdev.util.Message

import com.sap.it.api.ITApiFactory;
import com.sap.it.api.mapping.ValueMappingApi
import com.sap.it.api.securestore.SecureStoreService
import com.sap.it.api.securestore.UserCredential
import com.sap.it.api.securestore.exception.SecureStoreException

// Adding the Bouncy Castle Security Provider
Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider())

def Message processData(Message message) {
    try{
        // Get the encrypted data as a byte array
        def encryptedData = message.getBody().getBytes()
        
        def secureStorageService =  ITApiFactory.getService(SecureStoreService.class, null)
        
        //get secured parameter
        def secureParameter = secureStorageService.getUserCredential("GPG Key Kasse Euronet")
        
        //get saved passphrase
        def PASS = secureParameter.getPassword().toString()
        
        //decrypt message with passphrase
        byte[] decryptedByteArray = ByteArrayHandler.decrypt(encryptedData, PASS.toCharArray())
        String decryptedString = new String(decryptedByteArray, Charset.forName("Cp1252"))
        // String decryptedString = new String(decryptedByteArray, "UTF-8")
        // String decryptedString = new String(decryptedByteArray)
        
        // set decrypted message to body
        message.setBody(decryptedString)
        return message
        
    } catch(Exception e){
        throw new SecureStoreException("Secure Parameter or Encrypted Data not available")
    }
  }
