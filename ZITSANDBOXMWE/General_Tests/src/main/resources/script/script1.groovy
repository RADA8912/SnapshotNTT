import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {
    def rawValue = message.getProperty("myNumber")
    def numberAsString = rawValue.toString()

    def messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog != null) {
        messageLog.addAttachmentAsString("DEBUG: rawValue class", rawValue.getClass().getName(), "text/plain")
        messageLog.addAttachmentAsString("DEBUG: numberAsString", numberAsString, "text/plain")
    }

    def formatted = numberAsString.substring(0, 2) + "00"
    message.setProperty("myFormattedNumber", formatted)
    return message
}
