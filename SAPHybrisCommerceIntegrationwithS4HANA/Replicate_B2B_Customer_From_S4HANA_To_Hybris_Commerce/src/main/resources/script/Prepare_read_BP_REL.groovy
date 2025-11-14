import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set BP_Rel datastore name, entry ID to find BP relationship, set WRITE_ACTION to "False"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String B2BUnit = message.getProperty("BP_PAYLOAD");
	
	String BP_REL_DATASTORE_ENTRY_ID = processXml(B2BUnit, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/InternalID");
	String Language = processXml(B2BUnit, "//CorrespondenceLanguageCode")
    message.setHeader("ENTRY_ID", BP_REL_DATASTORE_ENTRY_ID);
    
    message.setProperty("BP_PAYLOAD", "<Language>" + Language + "</Language>");
	
     message.setHeader("DATASTORE_NAME", "BP_Relationship");
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