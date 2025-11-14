import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    def properties = message.getProperties();
    value = message.getProperty("DateNow");
    Date newDate = new Date()
    value = newDate.getTime();
    value = value/1000
    value = value - 60
    message.setProperty("DateNow", value);
    return message;
}