import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

/**
* GetHTTPResponseException
* Parses HTTP error JSON, extracts and shortens the error message,
* then sets it as a custom header for monitoring in CPI.
*
* @version 1.2.0
*/

def Message processData(Message message) {
    def map = message.getProperties()
    def ex = map.get("CamelExceptionCaught")

    if (ex != null && ex.getClass().getCanonicalName() == "org.apache.camel.component.ahc.AhcOperationFailedException") {
        def messageLog = messageLogFactory.getMessageLog(message)
        def responseBody = ex.getResponseBody()
        def shortError = "Unknown error"

        try {
            def json = new JsonSlurper().parseText(responseBody)
            def fullMessage = json?.errors?.getAt(0)?.ErrorMessage

            if (fullMessage) {
                // Optional: shorten/clean the message
                shortError = fullMessage.replaceAll(/\s*Owning_Obj.*$/, "") // removes everything after "Owning_Obj..."
                //shortError = shortError.take(200) // trim to first 200 characters if too long
            }
        } catch (Exception e) {
            shortError = "Failed to parse error message"
        }

        if (messageLog != null) {
            messageLog.addCustomHeaderProperty("HTTP_StatusCode", ex.getStatusCode().toString())
            messageLog.addCustomHeaderProperty("HTTP_StatusText", ex.getStatusText())
            messageLog.addCustomHeaderProperty("HTTP_ResponseBody", shortError)
        }

        message.setBody(responseBody)
    }

    return message
}
