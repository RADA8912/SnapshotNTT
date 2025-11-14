import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
    def exception = message.getProperty("CamelExceptionCaught")

    def responseBody = "Error"
    def statusCode = 500

    if (exception != null) {
        try {
            def responseBodyMethod = exception.getClass().getMethod("getResponseBody")
            def statusCodeMethod = exception.getClass().getMethod("getStatusCode")

            responseBody = responseBodyMethod.invoke(exception)
            statusCode = statusCodeMethod.invoke(exception)
        } catch (Exception e) {
            responseBody = exception.getMessage()
        }
    }

    // Show response body in error log 
    def errorMsg = "HTTP Error " + statusCode + ": " + responseBody

    // throw new exception, so the message is in status "error" in CPI monitoring
    throw new Exception(errorMsg)
}