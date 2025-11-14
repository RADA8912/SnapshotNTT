import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory

// Write B2BUnit directly then set B2BUnit as BP_Payload property
def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	
	message.setProperty("BP_PAYLOAD", body);

	String B2BUnit_DATASTORE_ENTRY_ID = processXml(body, "//InternalID");
	String CorrespondenceLanguageCode = processXml(body, "//CorrespondenceLanguageCode");

    message.setHeader("ENTRY_ID", B2BUnit_DATASTORE_ENTRY_ID);

    message.setBody("<Language>" + CorrespondenceLanguageCode +"</Language>");
    message.setHeader("DATASTORE_NAME", "BP_B2BUnit");
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