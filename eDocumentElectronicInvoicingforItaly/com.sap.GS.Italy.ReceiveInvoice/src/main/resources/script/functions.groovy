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
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSProcessable; 
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.util.Store;

def Message buildDatastoreName(Message message) {
	
	//Headers 
	headers = message.getHeaders();
	dataStoreName = headers.get("DataStoreName");
	
	//Properties  
	props = message.getProperties();
	fileName = props.get("NomeFile");
	
	String[] parts = fileName.trim().split("_");
	fiscalCode = parts[0].substring(2);

	message.setHeader("DataStoreName", dataStoreName + fiscalCode);
	
	return message;
}


def Message extractPayload(Message message) {
	
	byte[] buffer = message.getBody(byte[]);

   	try {
    	//Corresponding class of signed_data is CMSSignedData
       	CMSSignedData signature = new CMSSignedData(buffer);
        Store cs = signature.getCertificates();
   		SignerInformationStore signers = signature.getSignerInfos();
   		Collection c = signers.getSigners();
   		Iterator it = c.iterator();

	   	//the following array will contain the content of xml document
	   	byte[] data = null;
	
	   	while (it.hasNext()) {
	    	SignerInformation signer = (SignerInformation) it.next();
	        Collection certCollection = cs.getMatches(signer.getSID());
	        Iterator certIt = certCollection.iterator();
	        X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
	
	        CMSProcessable sc = signature.getSignedContent();
	        data = (byte[]) sc.getContent();
	    }
	    
	    message.setBody(data);	    
	      	       
		return message;
	
   } catch (CMSException ex) {
   	   	//Not a PKCS#7 signature (CAdES), but XML Digital Signature (XAdES)
       	return message;
   } 
}


def Message isPKCS7Signed(Message message) {
    
    String body = message.getBody(String);
    
    if (body.startsWith("<")) {
        message.setProperty("IsPKCS7Signed", "false");
    } else {
        message.setProperty("IsPKCS7Signed", "true");  
    }
    
    return message;
}


def Message isEncoded(Message message) {
    
    String body = message.getBody(String);
    
    if (body.contains("<")) {
        message.setProperty("IsEncoded", "false");
    } else {
        message.setProperty("IsEncoded", "true");
    }

    return message;
}


def Message trimFiscalCode(Message message) {
 
/*  Fiscal code can be: 
*   - 16 digits alphanumerical 
*   - 11 digits numerical
*
*   1.) If the fiscal code has 16 digts and is numerical, cut off the 5 leading zeros
*   Example: 0000012345678901 -> 12345678901
*
*   2.) If the fiscal code has 2 leading characters, cut them off
*   Example: IT12345678901 -> 12345678901
*/

    //Contains 5 zeros followed by 11 digits
    String regex5Zeros = "0{5}\\d{11}";
    
    //Contains 2 leading characters followed by 11 digits
    String regex2Chars = "[A-Z]{2}\\d{11}";
 
	props = message.getProperties();
	fiscalCode = props.get("FiscalCode");
    
    if (fiscalCode.matches(regex5Zeros)) {
        //Cut of leading 5 zeros
        fiscalCode =  fiscalCode.substring(5);
        message.setProperty("FiscalCode", fiscalCode);
    } else if (fiscalCode.matches(regex2Chars)) {
        fiscalCode =  fiscalCode.substring(2);
        message.setProperty("FiscalCode", fiscalCode);   
    }
    
    return message;
}


def Message formatFiscalCode(Message message) {
 
/*  Fiscal code can be: 
*   - 16 digits alphanumerical 
*   - 11 digits numerical
*   
*   This method will create the following properties and formats:
*   - FiscalCode:   
*       12345678901 (11 digits no leading zeros)
*   - FiscalCode16: 
*       0000012345678901 (16 digits with leading zeros) or
*       ABCDEFGHIJKLMNOP (16 chars)
*   - FiscalCodeIT:
*       IT12345678901 (11 digits with IT prefix)     
*/

    //Contains 5 zeros followed by 11 digits
    String regex11w0 = "0{5}\\d{11}";

    //Contains 11 digits
    String regex11 = "\\d{11}";
 
	props = message.getProperties();
	fiscalCode = props.get("FiscalCode");
	vatCode = props.get("VATCode");
    
    if (fiscalCode.matches(regex11w0)) {
        fiscalCode = fiscalCode.substring(5);
        fiscalCode16 = fiscalCode;
    } else if (fiscalCode.matches(regex11)) { 
        fiscalCode16 = "00000".concat(fiscalCode);
    } else { // 16 characters alphanumerical
        fiscalCode16 = fiscalCode;
    }
    
    fiscalCodeIT = "IT".concat(fiscalCode);
    
    message.setProperty("FiscalCode", fiscalCode);
    message.setProperty("FiscalCode16", fiscalCode16);
    message.setProperty("FiscalCodeIT", fiscalCodeIT);
    
    return message;
}

def Message getCurrentDate(Message message){
    
     format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
     timeZone = "CET";
     Date date = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat(format);
     formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
     synchronized (date) {
        message.setProperty("Timestamp",formatter.format(date));
     } 
     return message;
}