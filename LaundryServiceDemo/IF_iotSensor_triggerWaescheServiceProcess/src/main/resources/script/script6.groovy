import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message){

     def headers = message.getHeaders();

     def sAgency = "HCI";
     def sSchema = "laundryService";
     def tAgency = "sensor";
     def tSchema = headers.get("bagId");
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