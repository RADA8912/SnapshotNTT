import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;


def Message processData(Message message) {
   //Get CPI tenant
   JsonSlurper slurper = new JsonSlurper();
   Map envApplication = slurper.parseText(System.getenv("VCAP_APPLICATION"));  
   
   String appUrl = System.getenv("HC_APPLICATION_URL");
   
   message.setProperty("P_Tenant_Name",appUrl);
   message.setProperty("TenantName", envApplication.uris );
   
   return message;
}