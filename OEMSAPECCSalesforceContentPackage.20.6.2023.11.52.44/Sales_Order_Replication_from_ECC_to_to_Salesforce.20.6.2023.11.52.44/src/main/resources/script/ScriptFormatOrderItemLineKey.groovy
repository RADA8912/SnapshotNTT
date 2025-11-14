import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*;

def Message processData(Message message) {
    
    //Body 
    def body = message.getBody(java.lang.String) as String;
    def map = message.getProperties();
    
    def xml = new XmlSlurper(false, false).parseText(body);
    
    def orderId = xml.IDOC.E1EDK01.BELNR.text();
    
    if(orderId != null && orderId != "")
    {
      	message.setProperty("orderId", orderId);   
    }

    def OrderItems = xml.depthFirst().findAll({ it -> it.name() == 'E1EDP01' });
    OrderItems.each({ OI ->
        def orderItemId = OI.POSEX.text();
        orderItemId = orderId + orderItemId.padLeft(6,'0');
        OI.POSEX = orderItemId;
    })
    
    //Remove namespace
    xml.@'xmlns' = ""
    
    message.setBody(XmlUtil.serialize(xml));
    
    return message;
}