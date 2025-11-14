import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
   def quantity = message.getProperty("quantity") as String;
   if (quantity.length() == 0 || quantity == null) {
       message.setProperty("quantity", "1");
   }
   return message;
}