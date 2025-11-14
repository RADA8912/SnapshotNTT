import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    String username = message.getBody(String)
    // Mit dem Setzen des Dollar-Zeichen wird eine String-Interpolation eingeleitet. Damit wird ein Platzhalter „username“ eingefuegt.
    message.setBody("Hello, ${username}")
    return message
}