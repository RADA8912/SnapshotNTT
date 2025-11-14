import com.sap.gateway.ip.core.customdev.util.Message;

import java.util.HashMap;

/**
 * This method returns the error payload.
 * 
 * @param message
 * @return message
 */

def Message processData(Message message) {

    def messageLog = messageLogFactory.getMessageLog(message);
    def bodyAsString = message.getBody(String.class);

    messageLog.addAttachmentAsString("Hubspot Payload", bodyAsString, "text/xml");

    return message;

}