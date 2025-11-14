import com.sap.gateway.ip.core.customdev.util.Message

import groovy.xml.XmlUtil


/**
 * 
 * This script sets a property, when the payload is empty.
 * */

def Message processData(Message message) {

    body = message.getBody(java.lang.String) as String
  
    //Check if null value is present
    if (body.equals("<root></root>")) {
        message.setProperty("PayloadEmpty", "true");

    } else {
        message.setProperty("PayloadEmpty", "false");
        
    }
    
    
    def messageLog = messageLogFactory.getMessageLog(message);
    def bodyAsString = message.getBody(String.class);
    messageLog.addAttachmentAsString("Payload", bodyAsString, "text/xml");
    

    return message

}
