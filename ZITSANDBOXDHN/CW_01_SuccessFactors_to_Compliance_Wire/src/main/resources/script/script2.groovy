import java.util.HashMap;

def Message processData(Message message) {

def body = message.getBody(String.class);

body = body.replaceAll("[\n]","");

message.setBody(body);

return message;

}