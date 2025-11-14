import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {

   def map = message.getHeaders();
//   def salesorder = map.get("SALESORDER");
   def eventType = map.get("eventType");

   
   def body = message.getBody(java.lang.String) as String;
   def messageLog = messageLogFactory.getMessageLog(message);


    //Properties
    def properties = message.getProperties();
    
  // String sBody = "SalesOrder " + salesorder  + " Event " + eventType + " from S4 HANA";
   String sBody = "Event " + eventType + " from S4 HANA";


   if(messageLog != null) {
       messageLog.setStringProperty(sBody, body);
       messageLog.addAttachmentAsString(sBody, body, 'application/json');
    }

   return message;
}