import com.sap.gateway.ip.core.customdev.util.Message;
import java.io.StringWriter;
import org.xml.sax.InputSource;
import javax.xml.xpath.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat
import java.util.HashMap;
import java.util.List;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter
import org.jdom.output.Format
import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath
import org.jdom.Namespace;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

def Message processData(Message message) {
	
	//Body 
	def body = message.getBody(String.class);
	
	StringBuilder sb = new StringBuilder();
	SAXBuilder builder = new SAXBuilder();	
	InputStream stream = new ByteArrayInputStream(body.getBytes("UTF-8"));	
	Document doc = builder.build(stream);
	Element root = doc.getRootElement();
	List<Element> records = doc.getRootElement().getChildren("Record");
	List<Element> elementsToDelete = new ArrayList<Element>();
	List<Element> elementsToAppend = new ArrayList<Element>();
	List<String> elemOfLocation = new  ArrayList<String>();
	if(records != null)
	{
		 for(Element record : records)
		 {
		 	Element Location = new Element("Location");
		 	String innerLocation = record.getChild("Elements").getChild("Location").getText();
		 	for(Element recordInnner : records)
		 	{
		 		if((innerLocation == recordInnner.getChild("Elements").getChild("Location").getText()) && !(elemOfLocation.contains(recordInnner.getChild("Elements").getChild("Location").getText())))
		 		{
		 			
		 			Element elemRecord = (Element)recordInnner.clone();
		 			Location.addContent(elemRecord);
		 			//elementsToDelete.add(recordInnner);
		 		}
		 	}
		 	if(Location.getChildren("Record").size()>0)	
		 	{	 	
		 		Element propertyLoc = new Element("PropertyLoc");
		 		propertyLoc.setText(innerLocation);
		 		Location.addContent(propertyLoc);
		 		elementsToAppend.add(Location);
		 	}
		 		
		 	elemOfLocation.add(innerLocation);
		 	
		 }
		 
	}
	 Element Locations = new Element("Locations");
	 if(elementsToAppend.size() > 0)
	 {
	 	for(Element element : elementsToAppend)
	 	{
	 		Locations.addContent(element);
	 	}	 	
	 }	 		
	List<String> documentsAsString = new ArrayList<String>();
	XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat().setOmitDeclaration(true)); 
	 
	 documentsAsString.add(outputter.outputString(Locations));
	for(String s: documentsAsString)
	{
		sb.append(s);
	}
	streams = Arrays.asList(new ByteArrayInputStream( sb.toString().getBytes("UTF-8") ) );	
	stream = new SequenceInputStream(Collections.enumeration(streams));
    message.setBody(stream);
	 
	/*XMLOutputter outputter = new XMLOutputter();
   stream = new ByteArrayInputStream(outputter.outputString(doc).getBytes("UTF-8"));
    message.setBody(stream);*/
    return message;  
}