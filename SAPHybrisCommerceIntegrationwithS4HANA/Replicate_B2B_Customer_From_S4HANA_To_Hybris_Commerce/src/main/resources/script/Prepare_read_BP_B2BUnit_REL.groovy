import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set B2BUnit + BP_REL datastore name, entry ID to read the BP_B2BUnit_REL, set WRITE_ACTION to "False"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String BP_B2BUnit_REL_ENTRY_ID = processXml(body, "//InternalID");
	message.setHeader("DATASTORE_NAME", "BP_B2BUnit_REL");
	message.setHeader("ENTRY_ID", BP_B2BUnit_REL_ENTRY_ID);
	message.setHeader("ACTION", "READ");
	
	message.setProperty("BP_PERSON", body);
	
    
   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream( xml.bytes )
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}