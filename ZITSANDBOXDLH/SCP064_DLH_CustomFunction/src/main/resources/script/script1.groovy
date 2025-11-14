import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    String username = message.getBody(String)
    message.setBody("Body, ${username}");
    return message;
}