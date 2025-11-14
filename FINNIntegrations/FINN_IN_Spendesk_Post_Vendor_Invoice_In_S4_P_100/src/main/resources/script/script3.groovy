import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    
    Date yesterday = new Date() - 1;

    String yesterday_string = yesterday.format( 'yyyy-MM-dd' );

    message.setProperty("date", yesterday_string);
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    messageLog.addCustomHeaderProperty("Export Date", yesterday_string as String);    
 
    return message;
}