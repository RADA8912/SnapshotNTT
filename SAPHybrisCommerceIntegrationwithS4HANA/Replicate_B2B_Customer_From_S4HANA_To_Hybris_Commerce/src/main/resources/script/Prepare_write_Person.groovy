import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// set BP_PERSON datastore name, entry ID to write the Person, set WRITE_ACTION to "True"
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	String BP_Person = message.getProperty("BP_PERSON");
	message.setBody(BP_Person);
	String BP_Person_ENTRY_ID = processXml(BP_Person, "//InternalID");
	message.setHeader("DATASTORE_NAME", "BP_Person");
	message.setHeader("ENTRY_ID", BP_Person_ENTRY_ID);
    message.setHeader("ACTION", "WRITE");
    
    message.setProperty("BP_PERSON", "");

   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream( xml.bytes )
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}