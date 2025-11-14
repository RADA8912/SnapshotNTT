import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*;

def Message processData(Message message){

     def headers = message.getHeaders();

    //Get Body 
    def body = message.getBody(String.class);
    def jsonSlurper = new JsonSlurper();
    def list = jsonSlurper.parseText(body);

    def tSchema=list.ID.toString();
    message.setHeader("bagId",tSchema);

     def sAgency = "HCI";
     def sSchema = "laundryService";
     def tAgency = "sensor";
     def key = "";

     def service = ITApiFactory.getApi(ValueMappingApi.class, null);

     if( service != null) {

         key = "customerName";
         def customerName=service.getMappedValue(sAgency, sSchema, key, tAgency, tSchema);
         message.setHeader("customerName",customerName);

         key = "customerMail";
         def customerMail=service.getMappedValue(sAgency, sSchema, key, tAgency, tSchema);
         message.setHeader("customerMail",customerMail);

         key = "customerId";
         def customerId=service.getMappedValue(sAgency, sSchema, key, tAgency, tSchema);
         message.setHeader("customerId",customerId);

         key = "customerLocation";
         def customerLocation=service.getMappedValue(sAgency, sSchema, key, tAgency, tSchema);
         message.setHeader("customerLocation",customerLocation);

         return message;

     }

     return null;

}