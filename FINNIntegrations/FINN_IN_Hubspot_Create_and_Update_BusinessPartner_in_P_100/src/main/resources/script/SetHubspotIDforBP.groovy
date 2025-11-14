/* This script looks up the individual Hubspot IDs depending whether the underlying
body represents a customer or a company. Based on that, the Hubspot ID to retrieve the 
BP form SAP is set. */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
 
    //Read Hubspot ID properties
    def properties = message.getProperties();
    hubspotOrg = properties.get("HubspotIDOrg");
    hubspotCustomer = properties.get("HubspotIDBP");
    
   if (hubspotOrg != ''){
       message.setBody(hubspotOrg);
   }
   else if (hubspotCustomer != ''){
       message.setBody(hubspotCustomer);
   }
   
   
    
    return message;
}