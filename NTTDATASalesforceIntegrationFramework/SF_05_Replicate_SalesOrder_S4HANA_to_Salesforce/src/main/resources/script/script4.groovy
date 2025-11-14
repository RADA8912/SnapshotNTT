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
            def status = xml.'**'.find{it.name() == "status"}.toString()
            def messageHTTP = xml.'**'.find{it.name() == "message"}.toString()
            //Set text
            def responseText = """${messageHTTP}"""
            
            //def responseText = """HTTP status code: ${httpStatusCode}
            //Content status: ${status}
            //Content message: ${messageHTTP}"""

            message.setProperty("HTTPError", messageHTTP)
        }
    }
    return message
}
