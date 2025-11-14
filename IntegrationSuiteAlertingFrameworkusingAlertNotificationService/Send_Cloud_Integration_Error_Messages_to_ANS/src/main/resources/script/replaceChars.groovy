import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message)
{
    def body = message.getBody(java.lang.String);
    
    def input = body.replaceAll("&","&amp;")
        .replaceAll("<","&lt;")
        .replaceAll(">","&gt;")
        .replaceAll("'","&apos;")
        .replaceAll(/\"/,"&quot;");
    
    message.setBody(input);
    return message;
}