import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.lang.String;
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory


def Message processData(Message message) {
    	
	def messageLog = messageLogFactory.getMessageLog(message);
	def body = message.getBody(java.lang.String) as String;
	def properties = message.getProperties() as Map<String, Object>;
	def headers = message.getHeaders() as Map<String, Object>;
	
	String EXTRA_SHIP_TO_B2BUNIT = message.getHeader("ENTRY_ID", java.lang.String);
	String COUNT = message.getProperty("notFoundCount");
	
	if(COUNT.equals('1'))
	{
	    throw new Exception("B2BUnit " + EXTRA_SHIP_TO_B2BUNIT + " doesn't exist in BP_B2BUnit_Address data store !");
	}
	

   return message;
}    

def processXml( String xml, String xpathQuery ) {
    
   def xpath = XPathFactory.newInstance().newXPath()
   def builder     = DocumentBuilderFactory.newInstance().newDocumentBuilder()
   def inputStream = new ByteArrayInputStream( xml.bytes )
   def records     = builder.parse(inputStream).documentElement
  
   xpath.evaluate( xpathQuery, records )
}