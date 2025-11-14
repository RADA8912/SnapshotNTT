import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.Random;

def Message processData(Message message) {
    //Erstellen einer Instanz von Random
    Random random = new Random();

    //Erzeugen einer zufälligen Nummer zwischen 0 und 1
    int randomNum = random.nextInt(2);

    //Bestimmen des zufälligen Strings auf der Grundlage der zufälligen Nummer
    String randomString = randomNum == 0 ? "1." : "2.";

    //Abrufen der Header-Map
    def messageHeaders = message.getHeaders();

    //Abrufen des vorhandenen SAP_ApplicationID-Werts
    String sapAppID = messageHeaders.get('SAP_ApplicationID');

    //Hinzufügen des zufälligen Strings zum Anfang des vorhandenen SAP_ApplicationID-Werts
    String newSAPAppID = randomString + sapAppID;

    //Setzen des aktualisierten SAP_ApplicationID-Werts zurück in den Header
    messageHeaders.put('SAP_ApplicationID', newSAPAppID);

    //Setzen des Headers zurück in die Nachricht
    message.setHeaders(messageHeaders);

    return message;
}
