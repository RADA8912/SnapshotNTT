/*
*This script creates two new elements CommonNameFormat and FullNameFormat whose values are described by the 
*end user in external parameters as CommonNameFormat & FullNameFormat
*The value of CommonNameFormat declared in the external parameter determines the format of DistinguishedName is to be created
*1.CN=FirstName+LastName
*2.CN=FirstName+MiddleName+LastName
*3.CN=LastName+FirstName
*4.CN=LastName+FirstName+MiddleName
*5.CN=personIdExternal
*6.CN=FirstName+"."+LastName
*
*Setting properties Location & HREmailID
*
*/
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
	
	int appendEmpJob = 1;
	StringBuilder sb = new StringBuilder();
	Element commonNameFormat = new Element("CommonNameFormat");
	Element fullNameFormat = new Element("FullNameFormat");
	Element empJobChild=new Element("EmpJob");
	SAXBuilder builder = new SAXBuilder();	
	InputStream stream = new ByteArrayInputStream(body.getBytes("UTF-8"));	
	Document doc = builder.build(stream);
	
	Element empJob = doc.getRootElement();
	if(empJob.getChild("EmpJob") != null)
	{
		appendEmpJob = 0;
	}
	if(empJob != null)
	{
		if(prop.get("CommonNameFormat") != null)
		{
			commonNameFormat.setText(prop.get("CommonNameFormat"));
			empJob.addContent(commonNameFormat);
		}
		else
		{
			commonNameFormat.setText("1");
			empJob.addContent(commonNameFormat);
		}
		if(prop.get("FullNameFormat") != null)
		{
			fullNameFormat.setText(prop.get("FullNameFormat"));
			empJob.addContent(fullNameFormat);
		}
		else
		{
			fullNameFormat.setText("1");
			empJob.addContent(fullNameFormat);
		}
		Element location = empJob.getChild("location");
		if(location != null)
		{
			//Read Value Map for the key. Signature : (SrcAgencey,SrcSchema,SrcValue,TargetAgency,TargetSchema)
                mappedValue = VMservice.getMappedValue( "EC", "Location_Code", location.getText(), "AD", "UserDNPath;HREmailID;DomainName");
                if(null != mappedValue)
				{
					String[] strArr = mappedValue.split(";"); 
					if(strArr[1] != '')
					   message.setProperty("HREmailID",strArr[1]);
					   
				}
		}
	}
	
	XMLOutputter outputter = new XMLOutputter(Format.getCompactFormat().setOmitDeclaration(true));
	if(appendEmpJob == 1)
	{
		List<String> documentsAsString = new ArrayList<String>();
		documentsAsString.add(outputter.outputString(doc));
		for(String s: documentsAsString)
		{
			sb.append("<EmpJob>");
			sb.append("\n");
			sb.append(s);		
			sb.append("\n");
			sb.append("</EmpJob>");
		}
		streams = Arrays.asList(new ByteArrayInputStream( sb.toString().getBytes("UTF-8") ) );	
		stream = new SequenceInputStream(Collections.enumeration(streams));
    	message.setBody(stream);
    }
    else
    {
    	message.setBody(outputter.outputString(doc));
    }
	return message;
}

