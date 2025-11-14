import com.sap.gateway.ip.core.customdev.util.Message

import groovy.xml.XmlUtil

def Message processData(Message message) {

  
    body = message.getBody(java.lang.String) as String
    def messageLog = messageLogFactory.getMessageLog(message);
    
    	if(messageLog != null)
	{
	messageLog.addAttachmentAsString("Log current Payload:", body, "text/plain");
     }
  
    //Check if null value is present
    if (body.equals("[]")) {
        message.setHeader("PayloadEmpty", "true");
    } else {
        message.setHeader("PayloadEmpty", "false");
    }
    return message

}
