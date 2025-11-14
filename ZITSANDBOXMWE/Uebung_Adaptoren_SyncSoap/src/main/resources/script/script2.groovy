import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def exception = message.getProperty("CamelExceptionCaught")
    def fullError = "Unbekannter Fehler"

    if (exception != null) {
        fullError = exception.toString()
    }

    message.setProperty("fullErrorMessage", fullError)
    return message
}
