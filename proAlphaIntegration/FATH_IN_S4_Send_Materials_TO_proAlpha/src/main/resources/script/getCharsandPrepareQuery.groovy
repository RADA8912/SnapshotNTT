import com.sap.gateway.ip.core.customdev.util.Message;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import groovy.util.*;





def Message processData(Message message) {
	

    //Define XML Parser
    def clfmas = new XmlParser().parseText(message.getBody(java.lang.String))
 
    //Get the atnam nodes
    def atnamNodes = clfmas.'**'.findAll { node -> node.name() == 'ATNAM' }
    

    //Define array list to build up query with relevant characteristics
    def charsList = []
    for (node in atnamNodes){
        charsList.add(node.text())
    }
    //Set query
    def http_query = '''$select=Characteristic, CharcDataType&$filter=Characteristic eq '''
    boolean initial_run = true

    for (value in charsList){
        if (!initial_run){
            http_query += "' or Characteristic eq "
        }
        http_query += "'" + value 
        initial_run = false

    }

    //Add a ' to the last part of the query so its fully consistent
    http_query += "'"

    //set http_query as property to retrieve relevnat characteristics
    message.setProperty("Z_HTTP_QUERY", http_query)
    
    //Set headers for better after processing
    message.setHeader("Content-Type", "application/json")
    message.setHeader("Accept", "application/json")

	return message
}
