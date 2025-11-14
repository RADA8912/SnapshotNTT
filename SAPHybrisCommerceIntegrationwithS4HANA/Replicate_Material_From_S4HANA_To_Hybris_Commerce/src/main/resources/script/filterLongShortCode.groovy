import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    message.setHeader("ENTRY_ID", message.getProperty("LONG_ENTRY_ID") != null && message.getProperty("LONG_ENTRY_ID")!=''?message.getProperty("LONG_ENTRY_ID"):message.getProperty("SHORT_ENTRY_ID"));
    return message;
}