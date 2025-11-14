import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body 
       def body = message.getBody(java.lang.String)as String;
       def messageHeaders = message.getHeaders();
       
       String Errorbody               = messageHeaders.get('ExceptionBody');
  
       
       String exceptionBody = 'Employee of id :'+ Errorbody;
    
       throw new Exception(exceptionBody);

       return message;
}