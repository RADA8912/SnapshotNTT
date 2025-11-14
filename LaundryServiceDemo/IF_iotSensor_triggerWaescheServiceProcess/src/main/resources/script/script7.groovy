import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message){

     def headers = message.getHeaders();

     def sAgency = "HCI";
     def sSchema = "laundryService";
     def tAgency = "WFS";
     def tSchema ="recipients";
     def key = "taskRecipients";

     def service = ITApiFactory.getApi(ValueMappingApi.class, null);

     if( service != null) {

         def taskRecipients=service.getMappedValue(sAgency, sSchema, key, tAgency, tSchema);
         message.setHeader("taskRecipients",taskRecipients);

         return message;

     }

     return null;

}