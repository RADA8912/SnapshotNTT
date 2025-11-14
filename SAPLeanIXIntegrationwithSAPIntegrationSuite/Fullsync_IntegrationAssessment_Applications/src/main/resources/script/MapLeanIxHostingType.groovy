import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    map = message.getProperties();    
    String applicationHostingType;  
    applicationHostingTypeLeanIx = map.get("applicationHostingTypeLeanIx");
    
    if ("saas".equals(applicationHostingTypeLeanIx)) {
        // If "saas", set "applicationHostingType" to the value of exchangeProperty "publicCloudId"
        applicationHostingType = map.get("publicCloudId");
    } else if ("hybrid".equals(applicationHostingTypeLeanIx)) {
        // If "hybrid", set "applicationHostingType" to the value of exchangeProperty "privateCloudId"
        applicationHostingType = map.get("privateCloudId");
    }
        else {
        // For other cases, e.g., "onPremise", the value of exchangeProperty "onPremId" is set
        applicationHostingType = map.get("onPremId");
    }

    // Set the new "applicationHostingType" as an exchangeProperty
    message.setProperty("applicationHostingType", applicationHostingType);

    // Pass on the processed message
    return message;
}