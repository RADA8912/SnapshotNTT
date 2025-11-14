import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.XmlUtil;

def Message processData(Message message) {
    //Body 
       def body = message.getBody(java.lang.String);
       
//properties
map=message.getProperties();
def suppl = map.get("Payload");


       
      def root = new XmlParser().parseText(suppl);
	  def contact_node = new XmlParser().parseText(body);
	
    root.children().add(0,contact_node) 
 
       message.setBody(XmlUtil.serialize(root));
       
       return message;
}