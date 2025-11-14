import com.sap.gateway.ip.core.customdev.util.Message
import java.nio.charset.StandardCharsets

def Message processData(Message message) {
    String body = message.getBody(String)
    def messageLog = messageLogFactory?.getMessageLog(message)

    if (messageLog && body != null) {
        final int MAX_LOG_BYTES = 10000 // adjust to your acceptable limit
        int bodyBytes = body.getBytes(StandardCharsets.UTF_8).length

        if (bodyBytes <= MAX_LOG_BYTES) {
            messageLog.addAttachmentAsString("Payload", body, "text/plain")
        } else {
            int previewChars = Math.min(body.length(), 2000)
            String preview = body.substring(0, previewChars)
            String note = "... (truncated, original size: ${bodyBytes} bytes)"
            messageLog.addAttachmentAsString("Payload (truncated)", preview + note, "text/plain")
            messageLog.addAttachmentAsString("Payload.size", Integer.toString(bodyBytes), "text/plain")
        }
    }

    return message
}