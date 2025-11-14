import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.UUID;
import java.util.*;

/********************************/
/***  Set SOAP URL Endpoint  ***/
/******************************/
    
def Message processData(Message message) {
    
	def url = message.getProperty("url").toString();
 	def soapUrl = url.replaceAll("\\?", "/businesspartnersuitebulkreplic?");

    message.setProperty("soapBPUrl", soapUrl);
    
    return message;
  
}