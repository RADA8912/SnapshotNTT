import com.sap.gateway.ip.core.customdev.util.Message;
def Message processData(Message message) {
    def value = message.getBody();
    message.setProperty("payload", value);
    return message;
}