import com.sap.gateway.ip.core.customdev.util.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.*;
import java.lang.*;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.xml.crypto.*;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.*;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sap.it.api.ITApiFactory;
import com.sap.it.api.keystore.KeystoreService;
import com.sap.it.api.keystore.exception.KeystoreException;

import org.xml.sax.InputSource;

def Message extractCredentials(Message message) throws Exception {
// extract credentials from secure store

	return message;
}

def Message buildSignedXML(Message message) throws Exception {
// build the request signed XML

	return message;
}

def Message setIdTrasmittente(Message message) throws Exception {

     def properties = message.getProperties();
     def IdTrasmittente = properties.get("IdTrasmittente");
     
    message.setHeader("IdTrasmittente", IdTrasmittente);

	return message;
}

def Message ExchangeCodes(Message message) throws Exception {
// If tag SoggettoEmittente has value then take fiscal code and IVA from Cessionario/Committente
     def properties = message.getProperties();
     def IVAFiscalID_Committente = properties.get("IVAFiscalID_Committente");
     def FiscalCode_Committente = properties.get("FiscalCode_Committente");
     def SoggettoEmittente = properties.get("SoggettoEmittente");
     
     
     if (SoggettoEmittente == 'CC'){
        message.setProperty("FiscalCode", FiscalCode_Committente );
        message.setProperty("IVAFiscalID", IVAFiscalID_Committente );
        
    } else {
        
    }

	return message;
}




