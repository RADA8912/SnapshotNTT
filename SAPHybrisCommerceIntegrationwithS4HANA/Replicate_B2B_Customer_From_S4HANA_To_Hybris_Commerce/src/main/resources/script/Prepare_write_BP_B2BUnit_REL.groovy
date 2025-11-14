import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set B2BUnit + BP_REL datastore name, entry ID to write the two payload, set WRITE_ACTION to "True"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String BP_REL_Payload = message.getProperty("BP_REL_PAYLOAD");
	String BP_Payload = message.getProperty("BP_PAYLOAD");
	String Language = processXml(BP_Payload, "//Language");
	
	String B2BUnit_REL_ID = processXml(BP_REL_Payload, "//RelationshipBusinessPartnerInternalID");
	
	message.setHeader("DATASTORE_NAME", "BP_B2BUnit_REL");
	message.setHeader("ENTRY_ID", B2BUnit_REL_ID);
	message.setHeader("ACTION", "WRITE");
	
	message.setBody("<B2BUnitRel>" + BP_REL_Payload + "<Language>" + Language + "</Language>" + "</B2BUnitRel>");
    message.setProperty("BP_REL_PAYLOAD", "");
    message.setProperty("BP_PAYLOAD", "");
    
    
   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream( xml.bytes )
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}