import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// rest BP_REL property (indivisual one) set Person datastore name, entry ID to find Person, set WRITE_ACTION to "False"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	
	String Person_DATASTORE_ENTRY_ID = processXml(body, "//RelationshipBusinessPartnerInternalID");

    message.setProperty("BP_REL_PAYLOAD", body);
    
    message.setHeader("DATASTORE_NAME", "BP_Person");
    message.setHeader("ENTRY_ID", Person_DATASTORE_ENTRY_ID);
    message.setHeader("ACTION", "READ");
    
   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream( xml.bytes )
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}