import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    def messageLog = messageLogFactory.getMessageLog(message);
    def bodyAsString = message.getBody(String.class);

    if (bodyAsString.contains("<httpStatusCode>400</httpStatusCode>") == true){
        message.setHeader("Failed","Failed");
        Reader reader = message.getBody(Reader)
        def composite = new XmlSlurper().parse(reader)
        ErrorMessage = "Errors:"
        def Response = composite.compositeResponse.findAll {
            compositeResponse ->
                compositeResponse != ''
        }
        Response.each {
            compositeResponse -> 
            if(compositeResponse.httpStatusCode.toString() == '400'){
                messageLog.addAttachmentAsString("ErrorSalesforce", compositeResponse.referenceId.toString()+": "+compositeResponse.body.errorCode.toString()+"-"+compositeResponse.body.message.toString(), "text/xml");
                addResponse = compositeResponse.referenceId.toString()+": "+compositeResponse.body.errorCode.toString()+"-"+compositeResponse.body.message.toString()+" //"
                ErrorMessage = ErrorMessage+" "+addResponse.toString()
            }
        }
        message.setProperty("Error", ErrorMessage)
    } else {
        message.setHeader("Failed","Success");
    }
    
    return message;

}