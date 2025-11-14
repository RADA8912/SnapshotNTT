import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(java.lang.String)
    def messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog != null) {
        // Avoid logging the full payload to prevent memory consumption issues.
        // Log only a small snippet and the payload size instead.
        def bodyStr = body == null ? null : body.toString()
        def maxLen = 1000
        def snippet = bodyStr == null ? "null" :
            (bodyStr.length() > maxLen ? bodyStr.substring(0, maxLen) + "...[truncated]" : bodyStr)
        messageLog.addAttachmentAsString("PayloadSnippet", snippet, "text/plain")
        messageLog.addAttachmentAsString("PayloadSize", "Size: ${bodyStr?.length() ?: 0}", "text/plain")
    }
    return message
}