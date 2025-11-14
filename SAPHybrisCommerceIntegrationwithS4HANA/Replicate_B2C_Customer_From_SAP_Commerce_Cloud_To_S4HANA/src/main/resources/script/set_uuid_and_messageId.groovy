import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.UUID; 
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;


//Set message headers' UUID and MessageId(SOAP Header)
def Message processData(Message message) {
    
    def customerId = message.getProperty("customerId").toString();
    def Customer_UUID = UUID.nameUUIDFromBytes(customerId.getBytes()).toString();
    message.setProperty("UUID", Customer_UUID);
    
    def messageId = UUID.randomUUID().toString();
    message.setProperty("MessageId", messageId);
    
    return message;
}