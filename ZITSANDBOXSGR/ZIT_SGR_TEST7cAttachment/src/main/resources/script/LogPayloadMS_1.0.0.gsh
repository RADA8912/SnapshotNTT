import com.sap.gateway.ip.core.customdev.util.Message
import java.nio.charset.StandardCharsets

def Message processData(Message message) {
    final int MAX_LOG_BYTES = 10000 // Adjust to your acceptable limit

    String body = message.getBody(String)
    def messageLog = messageLogFactory?.getMessageLog(message)

    if (messageLog && body != null) {
        int bodyBytes = body.getBytes(StandardCharsets.UTF_8).length

        if (bodyBytes <= MAX_LOG_BYTES) {
            messageLog.addAttachmentAsString("Payload MS", body, "text/plain")
        } else {
            int previewChars = Math.min(body.length(), 2000)
            String preview = body.substring(0, previewChars)
            String note = "... (truncated, original size: ${bodyBytes} bytes)"
            messageLog.addAttachmentAsString("Payload MS (truncated)", preview + note, "text/plain")
        }
    }
    return message
}