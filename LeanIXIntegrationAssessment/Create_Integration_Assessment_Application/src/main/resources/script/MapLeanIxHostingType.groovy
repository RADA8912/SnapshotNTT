import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    map = message.getProperties();    
    // Erstellen einer exchangeProperty "applicationHostingType"
    String applicationHostingType;  
    applicationHostingTypeLeanIx = map.get("applicationHostingTypeLeanIx");
    
    if ("saas".equals(applicationHostingTypeLeanIx)) {
        // Wenn "saas", setze "applicationHostingType" auf den Wert der exchangeProperty "publicCloudId"
        applicationHostingType = map.get("publicCloudId");
    } else if ("hybrid".equals(applicationHostingTypeLeanIx)) {
        // Wenn "saas", setze "applicationHostingType" auf den Wert der exchangeProperty "publicCloudId"
        applicationHostingType = map.get("privateCloudId");
    }
        else {
        // Für andere Fälle, z.B. "onPremise", wird nichts gesetzt oder eine andere Logik könnte hier implementiert werden
        applicationHostingType = map.get("onPremId");
    }

    // Setze die neue "applicationHostingType" exchangeProperty
    message.setProperty("applicationHostingType", applicationHostingType);

    // Weitergabe der verarbeiteten Nachricht
    return message;
}
