import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Properties
    def properties = message.getProperties();
    value = properties.get("ptime");
    zahl = value as String;
    zahl = zahl - 4;
    delete = zahl as String;
    message.setProperty("ptime", "delete");
    return message;
}