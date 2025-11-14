import com.sap.gateway.ip.core.customdev.util.Message;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

//AddSOAPHeaderBean
//Version 1.0.0

def Message processData(Message message) {

    def props = message.getProperties();
    List headersList = new ArrayList<SoapHeader>();
    def headersToAdd = [];
    String moduleKey = "soap.";

    List<String> names = props.findAll { key, value ->
        key.startsWith(moduleKey) && (key.endsWith(".value") || key.endsWith(".namespace"));
    }.collect { key, value ->
        key.tokenize('.')[1];
    }.unique();

    for (String name : names) {
        String value = props.get(moduleKey + name + ".value") as String;
        String namespace = props.get(moduleKey + name + ".namespace") as String;
        CustomSOAPHeader cHeader = new CustomSOAPHeader(name, namespace, value);
        headersToAdd.add(cHeader);
    }

    for (CustomSOAPHeader cHeader : headersToAdd){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Element authHeader;

        if (cHeader.isXML){
            //encapsulate XML
            cHeader.value = "<root>" + cHeader.value + "</root>";
            Document document = builder.parse(new InputSource(new StringReader(cHeader.value)));
            Element rootElement = document.getDocumentElement();
            authHeader = document.createElementNS(cHeader.namespace, cHeader.name);
            authHeader.appendChild(rootElement);
            document.appendChild(authHeader);
            //remove encapsulation
            removeNodeKeepChildren(rootElement);
        }
        else {
            Document document = builder.newDocument();
            authHeader = document.createElementNS(cHeader.namespace, cHeader.name);
            authHeader.setTextContent(cHeader.value);
        }

        SoapHeader header = new SoapHeader(new QName(authHeader.getNamespaceURI(), authHeader.getLocalName()), authHeader);
        header.setMustUnderstand(true);
        headersList.add(header);
    }

    message.setHeader("org.apache.cxf.headers.Header.list", headersList);

    return message;
}

//remove encapsulation
static def removeNodeKeepChildren(node) {
    if (node.parentNode) {
        // Move all children of the node to be removed to its parent
        while (node.firstChild) {
            node.parentNode.insertBefore(node.firstChild, node);
        }
        // Now remove the node itself
        node.parentNode.removeChild(node);
    }
}

class CustomSOAPHeader {
    String name;
    String namespace;
    String value;
    Boolean isXML;

    CustomSOAPHeader(String name, String namespace, String value){
        this.name = name;
        this.namespace = namespace;
        this.value = value;
        isXML = isValidXml(value);
    }

    static boolean isValidXml(String xmlString) {
        String pattern = /^<.+?>/;
        boolean matches = (xmlString =~ pattern);
        return matches;
    }
}
