import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    // get a map of properties
    def map = message.getProperties();

    // get an exception java class instance
    def body = message.getBody(String)
        def ex = map.get("CamelExceptionCaught");
    def exType = ex.getClass().getCanonicalName();
    def messageLog = messageLogFactory.getMessageLog(message);

    //save the logged payload as attachment
    messageLog.addAttachmentAsString("Origin_Payload", map.get("LoggedPayload"), "text/plain");

    if (ex != null) {

        // an odata v2 adapter throws an instance of com.sap.gateway.core.ip.component.odata.exception.OsciException
        if (ex.getClass().getCanonicalName().equals("com.sap.gateway.core.ip.component.odata.exception.OsciException")) {

            messageLog.addAttachmentAsString("http.requestUri", ex.getRequestUri(), "text/plain");
            // copy the http error request uri to an exchange property
            message.setProperty("http.requestUri", ex.getRequestUri());

            // copy the http error response body as an attachment
            messageLog.addAttachmentAsString("http.response", message.getBody(), "text/plain");

            // copy the http error response body as a propert
            message.setProperty("http.response", message.getBody());

            // copy the http error response body as an attachment
            messageLog.addAttachmentAsString("http.statusCode", message.getHeaders().get("CamelHttpResponseCode").toString(), "text/plain");

            // copy the http error response body as a property
            message.setProperty("http.statusCode", message.getHeaders().get("CamelHttpResponseCode").toString());

        }
        if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {

            // save the http error response as a message attachment
            messageLog.addAttachmentAsString("http.ResponseBody", ex.getResponseBody(), "text/plain");

            // copy the http error response to an exchange property
            message.setProperty("http.ResponseBody", ex.getResponseBody());

            // copy the http error response to the message body
            message.setBody(ex.getResponseBody());

            // copy the value of http error code (i.e. 500) to a property
            message.setProperty("http.StatusCode", ex.getStatusCode());

            // copy the value of http error text (i.e. "Internal Server Error") to a property
            message.setProperty("http.StatusText", ex.getStatusText());

        }
    }

    return message;
}
