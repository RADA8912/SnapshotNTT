import com.sap.gateway.ip.core.customdev.util.Message;


 def Message processData(Message message) {

 def messageLog = messageLogFactory.getMessageLog(message);

 def bodyAsString = message.getBody(String.class);

 if (bodyAsString.length() == (bodyAsString.length()/33).toInteger()*33){
     message.setHeader("Failed","Success");
 } else {
     message.setHeader("Failed","Failed");
 }
 return message;

 }
