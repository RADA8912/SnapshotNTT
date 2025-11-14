
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def removeFirstAndLastChar(String str) {
    if (str.length() > 1) {
        return str[1..-2] // Gibt den Teilstring von Index 1 bis vor dem letzten Zeichen zurück
    } else {
        return "" // Gibt einen leeren String zurück, wenn der Input zu kurz ist
    }
}

def Message processData(Message message) {
    //Body
    def body = message.getBody();
    
    def modifiedPayload = removeFirstAndLastChar(body)
    
    message.setBody(modifiedPayload)
    
    return message;
}