import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.util.slurpersupport.GPathResult

def Message processData(Message message) {
    // Zugriff auf die HTTP-Response-Details
    def responseStatusCode = message.getHeader("CamelHttpResponseCode", Integer.class)
    def responseStatusMessage = message.getHeader("CamelHttpResponseText", String.class)
    def body = message.getBody(java.lang.String) as String
    body = body.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim()
    def xmlSlurper = new XmlSlurper().parseText(body)

    GPathResult errorMessage = xmlSlurper.'**'.find { node -> node.name() == 'message' }
    def extractedMessage = errorMessage.text()

    String vendorName = message.properties.get("vendorName") as String
    String logFileContent = message.properties.get("logFileContent") as String

    logFileContent = logFileContent + vendorName + ", Vendor: ${vendorName}, Statuscode: ${responseStatusCode}, Statusmessage: ${extractedMessage}\n"
    message.setProperty("logFileContent", logFileContent)

    return message
}
