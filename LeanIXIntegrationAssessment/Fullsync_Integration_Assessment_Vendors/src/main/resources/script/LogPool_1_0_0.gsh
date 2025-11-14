import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
    def map = message.getProperties()
    //Get exception object
    def ex = map.get("CamelExceptionCaught")
    if (ex!=null) {  
        //Check if HTTP exception      
        if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
            //Get HTTP status code
            def httpStatusCode = ex.getStatusCode()
            //Parse HTTP response
            def xml = new XmlSlurper().parseText(ex.getResponseBody())
            //Get fields of HTTP response
            def status = xml.'**'.find{it.name() == "status"}.text()
            def message = xml.'**'.find{it.name() == "message"}.text()
            //Set mail text
            def mailText = """HTTP status code: ${httpStatusCode}
Content status: ${status}
Content message: ${message}"""
            //Set body (later use body in mail receiver channel)
            message.setBody(mailText)
        }
    }
    return message
}