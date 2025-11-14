import com.sap.gateway.ip.core.customdev.util.Message;
import java.text.SimpleDateFormat;

def Message processData(Message message) {
    def ex = message.getProperty("CamelExceptionCaught")
    def errorMessage = ex?.getMessage() ?: "Unknown Error"
    message.setProperty("errorMessage", errorMessage)

    // Message ID aus Header
    def messageId = message.getHeaders().get("SAP_MessageProcessingLogID")
    message.setProperty("MessageID", messageId ?: "Unknown")

    // Sender- und Receiver-System aus Headern
    def senderSystem = message.getHeaders().get("SenderSystem")
    def receiverSystem = message.getHeaders().get("ReceiverSystem")
    message.setProperty("SenderSystem", senderSystem ?: "Unknown")
    message.setProperty("ReceiverSystem", receiverSystem ?: "Unknown")

    // Timestamp setzen
    def sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def timestamp = sdf.format(new Date())
    message.setProperty("Timestamp", timestamp)

    // Environment setzen – statisch oder dynamisch
    def host = message.getHeaders().get("CamelHttpHost")  // z. B. https://xyz-dev.it-cp.io
    def environment = host?.toLowerCase()?.contains("dev") ? "DEV" :
                      host?.toLowerCase()?.contains("test") ? "TEST" :
                      host?.toLowerCase()?.contains("prod") ? "PROD" : "Unknown"
    message.setProperty("Environment", environment)

    return message
}

