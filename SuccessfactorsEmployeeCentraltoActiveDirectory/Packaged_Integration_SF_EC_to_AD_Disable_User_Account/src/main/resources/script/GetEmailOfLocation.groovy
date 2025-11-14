import com.sap.aii.mapping.value.api.XIVMService;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.exception.InvalidContextException;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.it.spi.ITApiHandler;
import com.sap.xi.mapping.camel.valmap.VMStore;
import com.sap.xi.mapping.camel.valmap.VMValidationException;
import com.sap.xi.mapping.camel.valmap.ValueMappingApiHandler;
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
	//Header
	def map = message.getHeaders();
	//Properties
	def prop = message.getProperties();
	def VMservice = ITApiFactory.getApi(ValueMappingApi.class, null);
	StringBuilder sb = new StringBuilder();
	SAXBuilder builder = new SAXBuilder();	
	InputStream stream = new ByteArrayInputStream(body.getBytes("UTF-8"));	
	Document doc = builder.build(stream);
	Element mainLocation=doc.getRootElement();
	List<Element> Record = mainLocation.getChildren("Record");
	for(Element e: Record)
	{
	Element	ele = e.getChild("Elements");
		
	
	Element location = ele.getChild("Location");
	String value = location.getText();
		if(location != null)
		{
			//Read Value Map for the key. Signature : (SrcAgencey,SrcSchema,SrcValue,TargetAgency,TargetSchema)
                mappedValue = VMservice.getMappedValue( "EC", "Location_Code", location.getText(), "AD", "UserDNPath;HREmailID;DomainName");
                if(null != mappedValue)
				{
					String[] strArr = mappedValue.split(";"); 
					if(strArr[1].trim() != '')
					message.setProperty("HREmailID",strArr[1]);
				}
		}
		break;
	}
	return message;
}