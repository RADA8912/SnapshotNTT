
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.UUID;
import java.util.*;

/********************************/
/***  Set SOAP URL Endpoint  ***/
/******************************/
    
def Message processData(Message message) {
    
	def url = message.getProperty("url").toString();
 	def soapUrl = url.replaceAll("\\?", "/businesspartnersuitebulkreplic?");

    soapUrl = soapUrl + "&MessageId=" + UUID.randomUUID().toString();
    message.setProperty("soapBPUrl", soapUrl);
    
    def customerId = message.getProperty("customerId").toString();
    def Customer_UUID = UUID.nameUUIDFromBytes(customerId.getBytes()).toString();
    message.setProperty("UUID", Customer_UUID);
    
    return message;
  
}