/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import de.itelligence.itxpress.api.parser.ParameterContainer;
import de.itelligence.itxpress.transform.util.XMLUtil;
import de.itelligence.itxpress.api.parser.ItxParameterParser;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.w3c.dom.Element;


def Message processData(Message message) {
    String NAMESPACE_AUTH = "http://dpd.com/common/service/types/Authentication/2.0";
	def messageLog = messageLogFactory.getMessageLog(message);
	messageLog.setStringProperty("itxbegin", "start");

	Map<String, DataHandler> attachments = message.getAttachments();

	Iterator<DataHandler> it = attachments.values().iterator()
	ParameterContainer parameters = null;
	Document paramDoc = null;
	while(it.hasNext()) {
		DataHandler attachment = it.next()
		if(attachment.getContent() instanceof java.lang.String){
			paramDoc = XMLUtil.createDocument(attachment.getContent().getBytes());
		} else {
			StreamSource stream = (StreamSource) attachment.getContent();
			paramDoc = XMLUtil.createDocument(stream.getInputStream());
		}
		parameters = ItxParameterParser.parseParameters(paramDoc.getDocumentElement());
	}

	if(parameters != null) {
	    messageLog.setStringProperty("itxparams", "found");
	    if(parameters.getContentType() != null && !parameters.getContentType().isEmpty()) {
			message.setHeader("Content-Type", parameters.getContentType());
		}
        String delisIdStr = parameters.getApiParameter("delisId");
        String authTokenStr = parameters.getApiParameter("authToken");
        String messageLanguageStr = parameters.getApiParameter("messageLanguage");
        
        if(authTokenStr != null){
            messageLog.setStringProperty("itxpauthTokenStr", "found");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element authentication = doc.createElementNS(NAMESPACE_AUTH, "ns:authentication");
			Element delisId = doc.createElement("delisId");
			Element authToken = doc.createElement("authToken");
			Element messageLanguage = doc.createElement("messageLanguage");
			XMLUtil.setTextContent(delisId, delisIdStr);
			XMLUtil.setTextContent(authToken, authTokenStr);
			XMLUtil.setTextContent(messageLanguage, messageLanguageStr);
			authentication.appendChild(delisId);
			authentication.appendChild(authToken);
			authentication.appendChild(messageLanguage);
        
           // Create SOAP header instance.
           SoapHeader header = new SoapHeader(new QName(authentication.getNamespaceURI(), authentication.getLocalName()), authentication);
           messageLog.setStringProperty("itxsoapheader", "created");
           // header.setActor("actor_test");
           // header.setMustUnderstand(true);
        
           // Add the SOAP header to the header list and set the list to the message header "org.apache.cxf.headers.Header.list".
           List  headersList  = new ArrayList<SoapHeader>();
           headersList.add(header);
           message.setHeader("org.apache.cxf.headers.Header.list", headersList);
        }
	}

	attachments.clear();
	message.setAttachments(attachments);
	message.getAttachments().clear();
	message.setAttachmentWrapperObjects(new HashMap<>())
    messageLog.setStringProperty("itxattachments", "cleared");

	return message;
}