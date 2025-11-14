import com.sap.gateway.ip.core.customdev.util.Message;

 import java.util.HashMap;

 def Message processData(Message message) {​​

 def messageLog = messageLogFactory.getMessageLog(message);

 def bodyAsString = message.getBody(String.class);

 messageLog.addAttachmentAsString("Payload", bodyAsString, "text/xml");

 return message;

 }