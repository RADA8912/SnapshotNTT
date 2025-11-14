import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

def Message processData(Message message) {
    // Get Properties
    map = message.getProperties();
    long certExpirydate = map.get("CertificateValidUntil");
    // Calculate Expiry 
    long dateNow = System.currentTimeMillis(); 
    long dateDiff = certExpirydate - dateNow;
    def daysToExpire = TimeUnit.DAYS.convert(dateDiff, TimeUnit.MILLISECONDS); 
    // Set Properties
    Date certExpirydateDate = new Date(certExpirydate); 
    message.setProperty("daysToExpire", daysToExpire);
    message.setProperty("CertExpirydate", certExpirydateDate);
   return message;
}