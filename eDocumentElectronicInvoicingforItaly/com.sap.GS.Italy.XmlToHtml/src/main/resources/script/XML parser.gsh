import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.*;
def Message processData(Message message) {
    
    def body = message.getBody(java.lang.String) as String;
    def root = new XmlSlurper().parseText(body);
    def Tagname = root.name( );
    
    map = message.getProperties();
	message.setProperty("RootTag", Tagname);
    return message;
}

