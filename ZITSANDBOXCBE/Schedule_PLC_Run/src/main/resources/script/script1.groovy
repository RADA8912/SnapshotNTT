import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.UUID;

def Message processData(Message message) {
    //Erzeugen einer Zufalls-ID
    UUID randomUUID = UUID.randomUUID();

    //Abrufen der Header-Map
    def messageHeaders = message.getHeaders();

    //Hinzufügen der Zufalls-ID zum Header
    messageHeaders.put('SAP_ApplicationID', randomUUID.toString());

    //Setzen des Headers zurück in die Nachricht
    message.setHeaders(messageHeaders);

    return message;
}
