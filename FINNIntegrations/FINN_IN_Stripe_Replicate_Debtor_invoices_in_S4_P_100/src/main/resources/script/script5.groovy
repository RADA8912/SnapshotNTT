import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.DecimalFormat;

def Message processData(Message message) {
    
    
    
       def Double offsetTotal =  Double.parseDouble(message.getProperty("offsetTotal")) * -1;
      
       // Only add the offset segment when required    
        message.setProperty("docTotal", "1");    
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    if (messageLog != null) {
        
        def String myLog = new Date().getTime().toString() + "\n" +  message.getProperty("docTotal").toString();
        
        messageLog.addAttachmentAsString('Offset Total 2', message.getProperty("offsetTotal"), 'text/plain');
         messageLog.addAttachmentAsString('Document Total 2', myLog, 'text/plain');
    }
    
     


    
    return message;
}