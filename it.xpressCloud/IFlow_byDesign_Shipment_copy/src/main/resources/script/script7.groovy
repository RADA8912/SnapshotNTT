// adaptiert von http://saphcidemo.blogspot.com/2018/03/setting-soap-headers-in-sap-hci.html

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;

def Message processData(Message message) {

  /* def service = ITApiFactory.getApi(SecureStoreService.class, null);
   def credential = service.getUserCredential("ups_credentials");
   if (credential == null){
      throw new IllegalStateException("No credential found for alias 'partner1_credential_alias'");
   }*/
   String username_input = "itelligence"; //credential.getUsername();
   String password_input = "itelli2011";
   String accesskey_input = "1C784C8A6BB21188";

   
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   dbf.setNamespaceAware(true);
   dbf.setIgnoringElementContentWhitespace(true);
   dbf.setValidating(false);
   DocumentBuilder db = dbf.newDocumentBuilder();
   Document doc = db.newDocument();
   Element upssecurity = doc.createElementNS("http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", "UPSSecurity");
   doc.appendChild(upssecurity);
   Element usernametoken = doc.createElementNS("http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", "UsernameToken");
   
   Element username = doc.createElementNS("http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", "Username");
   username.setTextContent(username_input);
   usernametoken.appendChild(username);
   
   Element password = doc.createElementNS("http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", "Password");
   password.setTextContent(password_input);
   usernametoken.appendChild(password);

   Element serviceaccesstoken = doc.createElementNS("http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", "ServiceAccessToken");
   Element accesslicensenumber = doc.createElementNS("http://www.ups.com/XMLSchema/XOLTWS/UPSS/v1.0", "AccessLicenseNumber");
   accesslicensenumber.setTextContent(accesskey_input);
   serviceaccesstoken.appendChild(accesslicensenumber);
   
   upssecurity.appendChild(usernametoken);
   upssecurity.appendChild(serviceaccesstoken);

   // Create SOAP header instance.
   SoapHeader header = new SoapHeader(new QName(upssecurity.getNamespaceURI(), upssecurity.getLocalName()), upssecurity);
   header.setActor("actor_test");
   header.setMustUnderstand(true);

   // Add the SOAP header to the header list and set the list to the message header "org.apache.cxf.headers.Header.list".
   List  headersList  = new ArrayList<SoapHeader>();
   headersList.add(header);
   message.setHeader("org.apache.cxf.headers.Header.list", headersList);
   
   return message;
}

