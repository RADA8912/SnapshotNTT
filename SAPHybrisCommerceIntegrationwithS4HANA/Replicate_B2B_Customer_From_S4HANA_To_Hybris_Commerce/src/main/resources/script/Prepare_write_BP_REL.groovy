import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set BP_Rel datastore name, entry ID to write BP_Rel, set WRITE_ACTION to "True"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String BP_REL = message.getProperty("BP_REL_PAYLOAD");
	message.setBody(BP_REL);
	String BP_Rel_DATASTORE_ENTRY_ID = processXml(BP_REL, "//BusinessPartnerRelationshipSUITEReplicateRequestMessage/BusinessPartnerRelationship/BusinessPartnerInternalID");

	message.setHeader("ENTRY_ID", BP_Rel_DATASTORE_ENTRY_ID);
    message.setHeader("DATASTORE_NAME", "BP_Relationship");
    message.setHeader("ACTION", "WRITE");
    
    

   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream( xml.bytes )
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}