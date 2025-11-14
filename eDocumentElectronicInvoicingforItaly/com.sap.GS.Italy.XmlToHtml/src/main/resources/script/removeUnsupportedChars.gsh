import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    body = body.replaceAll(/[\x80-\x9F]+/, "");
    message.setBody(body);
    return message;
}