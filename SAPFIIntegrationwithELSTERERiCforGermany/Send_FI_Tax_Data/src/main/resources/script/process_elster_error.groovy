import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message process_elster(Message message) {
    def map = message.getProperties();
    def caughtException = map.get("CamelExceptionCaught")

    if (caughtException instanceof java.lang.Throwable) {
        if (caughtException instanceof com.sap.it.eric.api.ResponseException) {
            message.setHeader("CamelHttpResponseCode", 200);
            message.setBody(((com.sap.it.eric.api.ResponseException)caughtException).getResponse());
            //message.setHeader('Content-Type', 'text/xml; charset=ISO-8859-15');
        } else if (caughtException instanceof com.sap.it.eric.api.EricException) {
            message.setHeader("CamelHttpResponseCode", 500);
            message.setBody(((com.sap.it.eric.api.EricException)caughtException).getMessage() + " rc=" + ((com.sap.it.eric.api.EricException)caughtException).getErrorCode().getErrorcode());
        } else if (caughtException instanceof javax.script.ScriptException) {
            message.setHeader("CamelHttpResponseCode", 500);
            java.lang.Throwable previousException = caughtException;
            java.lang.Throwable currentException = previousException;
            int cnt = 0;
            while ((currentException!=null)&&(cnt<10))  {
                previousException = currentException;
                currentException = previousException.getCause();
                cnt ++;
            }
            message.setBody(previousException.getMessage());
        } else {
            message.setHeader("CamelHttpResponseCode", 500);
            message.setBody(caughtException.getMessage());
        }
    } else {
        message.setHeader("CamelHttpResponseCode", 500);
        message.setBody("Unexpected error");
    }
    return message;
}


def Message process_other(Message message) {
    def map = message.getProperties();
    def caughtException = map.get("CamelExceptionCaught")
    message.setHeader("CamelHttpResponseCode", 500);
    
    if (caughtException instanceof javax.script.ScriptException) {
            java.lang.Throwable previousException = caughtException;
            java.lang.Throwable currentException = previousException;
            int cnt = 0;
            while ((currentException!=null)&&(cnt<10))  {
                previousException = currentException;
                currentException = previousException.getCause();
                cnt ++;
            }
            
            message.setBody(previousException.getMessage());
    } else {
            message.setBody(caughtException.getMessage());
    }

    return message;
}