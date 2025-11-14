import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set Extra ship-to party datastore name, entry ID to write B2BUnit for extra ship-to, set WRITE_ACTION to "True"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
    String B2BUnit_ADDRESS_DATASTORE_ENTRY_ID = processXml(body, "//BusinessPartnerSUITEReplicateRequestMessage/BusinessPartner/Customer/InternalID");
	
	message.setHeader("DATASTORE_NAME", "BP_B2BUnit_Address");
	message.setHeader("ENTRY_ID", B2BUnit_ADDRESS_DATASTORE_ENTRY_ID);
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