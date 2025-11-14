import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    // get a map of properties
    def map = message.getProperties();
    
    def messageLog = messageLogFactory.getMessageLog(message);
    //save the logged payload as attachment
    messageLog.addAttachmentAsString("Origin_Payload", map.get("LoggedPayload"), "text/plain");
    
    messageLog.addAttachmentAsString("AM_Payload", map.get("LoggedPayloadAM"), "text/plain");
     
     
                
    // get an exception java class instance
    def ex = map.get("CamelExceptionCaught");
    def exType = ex.getClass().getCanonicalName();
     message.setProperty("test", exType);
    if (ex!=null) {
                                
        // an odata v2 adapter throws an instance of com.sap.gateway.core.ip.component.odata.exception.OsciException
        if (ex.getClass().getCanonicalName().equals("com.sap.gateway.core.ip.component.odata.exception.OsciException")) {
                                                
            // save the http error request uri as a message attachment 
            
            messageLog.addAttachmentAsString("http.requestUri", ex.getRequestUri(), "text/plain");
            // copy the http error request uri to an exchange property
            message.setProperty("http.requestUri",ex.getRequestUri());
            
            // copy the http error response body as an attachment 
            messageLog.addAttachmentAsString("http.response", message.getBody(), "text/plain");
            // copy the http error response body as a propert 
            message.setProperty("http.response", message.getBody());
            
            // copy the http error response body as an attachment 
            messageLog.addAttachmentAsString("http.statusCode", message.getHeaders().get("CamelHttpResponseCode").toString(), "text/plain");
            
             // copy the http error response body as a property 
            message.setProperty("http.statusCode", message.getHeaders().get("CamelHttpResponseCode").toString());
            
            

        }
    }

    return message;
}