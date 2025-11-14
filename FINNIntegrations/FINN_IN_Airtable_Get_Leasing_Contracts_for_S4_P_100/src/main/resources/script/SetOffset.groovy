
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/**
 * This messages sets the offset for the pagination mechanism. Maximum limit allowed is 1000.
 * Hence the offset counter is increased by the maxmimum limit.
 * @param message
 * @return message
 */


def Message processData(Message message) {

    properties = message.getProperties();
    value = properties.get("Offset");
    value = value + 1000;
    message.setProperty("Offset", value);


    return message;
}