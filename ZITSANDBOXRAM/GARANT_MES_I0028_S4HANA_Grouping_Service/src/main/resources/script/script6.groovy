import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    // Body der Nachricht
    def body = message.getBody(String);  // Konvertiere Body in einen String

    // Properties der Nachricht
    def properties = message.getProperties();
    def taskBody = properties.get("TaskBody");
    

    // Konvertiere taskBody zu einem String
    def taskString = taskBody != null ? taskBody.toString() : "";

    // Füge body und taskString zusammen
    def combinedString = body+","+ taskString;

    // Setze das kombinierte Ergebnis als neuen Body der Nachricht
    message.setBody(combinedString);

    return message;
}
