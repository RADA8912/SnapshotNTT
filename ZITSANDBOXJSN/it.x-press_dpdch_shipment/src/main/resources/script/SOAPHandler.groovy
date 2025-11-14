package com.nttdata.ndbs.btp.soap

import com.sap.gateway.ip.core.customdev.util.Message;
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document
import org.w3c.dom.Element
import de.itelligence.itxpress.api.parser.ParameterContainer
import de.itelligence.itxpress.transform.util.XMLUtil;
import de.itelligence.itxpress.api.parser.ItxParameterParser;
import com.nttdata.ndbs.btp.soap.SOAPEnvelope;



def Message processData(Message message) {


	def messageLog = messageLogFactory.getMessageLog(message);

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
		if(parameters.getSoapAction() != null && !parameters.getSoapAction().isEmpty()) {
			message.setHeader("SOAPAction", parameters.getSoapAction());
		}
		if(parameters.getContentType() != null && !parameters.getContentType().isEmpty()) {
			message.setHeader("Content-Type", parameters.getContentType());
		}
		if(parameters.getUrl() != null && !parameters.getUrl().isEmpty()) {
		    message.setHeader("CamelHTTPUri", parameters.getUrl());
			URI uri = new URI(parameters.getUrl());
			uri.getHost()
			message.setHeader("Host", uri.getHost() + ":" + uri.getPort());
			String path = uri.getPath();
		    String query = uri.getQuery();
		    if (query != null) {
			    path = path + "?" + query;
		    }
			message.setProperty("pathAndQuery", path);
		}
	}

	attachments.clear();
	message.setAttachments(new HashMap<>());
	message.setAttachmentObjects(new HashMap<>());
	message.setAttachmentWrapperObjects(new HashMap<>())


	Document doc = XMLUtil.createDocument(message.getBody((byte[]).class));
	//messageLog.setStringProperty("itxXMLBody", "created XML document")
	// message.setHeader("Content-Length", serialized.length);
	message.setBody(SOAPEnvelope.wrap(doc));
	//messageLog.setStringProperty("itxBodySet", "set body with SOAP envelope")

	//messageLog.setStringProperty("editedBody", message.getBody(java.lang.String));

	return message;
}

def Message addSOAPHeader(Message message) {
    	def messageLog = messageLogFactory.getMessageLog(message);

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

    // messageLog.setStringProperty("itxReadParams1", "trying to read parameters");
	if(parameters != null) {
		if(parameters.getSoapAction() != null && !parameters.getSoapAction().isEmpty()) {
			message.setHeader("SOAPAction", parameters.getSoapAction());
		}
		if(parameters.getContentType() != null && !parameters.getContentType().isEmpty()) {
			message.setHeader("Content-Type", parameters.getContentType());
			messageLog.setStringProperty("itxContent-Type", parameters.getContentType());
		}
		if(parameters.getUrl() != null && !parameters.getUrl().isEmpty()) {
		    message.setHeader("CamelHTTPUri", parameters.getUrl());
			URI uri = new URI(parameters.getUrl());
			//uri.getHost()
			//message.setHeader("Host", uri.getHost() + ":" + uri.getPort());
			String path = uri.getPath();
		    String query = uri.getQuery();
		    if (query != null) {
			    path = path + "?" + query;
		    }
			message.setProperty("pathAndQuery", path);
		}
		delisId = parameters.getApiParameter("delisId");
		authToken = parameters.getApiParameter("authToken");
	}

    



	attachments.clear();
	message.setAttachments(new HashMap<>());
	message.setAttachmentObjects(new HashMap<>());
	message.setAttachmentWrapperObjects(new HashMap<>())

	Document doc = XMLUtil.createDocument(message.getBody((byte[]).class));
	//messageLog.setStringProperty("itxXMLBody", "created XML document")
	// message.setHeader("Content-Length", serialized.length);
	doc = XMLUtil.createDocument(SOAPEnvelope.wrap(doc));
	
	Element authentication = doc.createElementNS("http://dpd.com/common/service/types/Authentication/2.0", "ns:authentication");
	Element delisId = doc.createElement("delisId");
	Element authToken = doc.createElement("authToken");
	Element messageLanguage = doc.createElement("messageLanguage");

	XMLUtil.setTextContent(delisId, parameters.getApiParameter("delisId"));
	XMLUtil.setTextContent(authToken, parameters.getApiParameter("authToken"));
	XMLUtil.setTextContent(messageLanguage, parameters.getApiParameter("messageLanguage"));

	authentication.appendChild(delisId);
	authentication.appendChild(authToken);
	authentication.appendChild(messageLanguage);
	
	// messageLog.setStringProperty("itxAuthToken", parameters.getApiParameter("authToken"));
	
	Element header = XMLUtil.getFirstChildElementOrNull(doc.getDocumentElement(), "Header");
	
	header.appendChild(authentication);

    message.setBody(XMLUtil.serialize(doc));
	
	// messageLog.setStringProperty("itxBodySet", "set body with SOAP envelope")

//	messageLog.setStringProperty("editedBody", message.getBody(java.lang.String));

	return message;
}


def Message removeSOAPEnvelope(Message message) {
	def bodyAsInputStream = message.getBody(InputStream.class);
	if (bodyAsInputStream == null) {
		throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
	}
    try {
        def serialized = SOAPEnvelope.removeSOAPEnvelope(bodyAsInputStream);
    	message.setBody(serialized);
    } catch (Exception e) {
    // SOAP Envelope already removed?
    }
    
    def body = message.getBody(java.lang.String) as String
    // Ersetzen des Start-Tags mit regulärem Ausdruck
    body = body.replaceAll(/<storeOrdersResponse[^>]*>/, "<n0:storeOrdersResponse xmlns:n0=\"http://dpd.com/common/service/types/ShipmentService/3.2\">")
    // Ersetzen des End-Tags
    body = body.replace("</storeOrdersResponse>", "</n0:storeOrdersResponse>")
    
     // Ersetzen des Start-Tags mit regulärem Ausdruck  EndOfDayService
    body = body.replaceAll(/<endOfDayResponse[^>]*>/, "<n0:endOfDayResponse xmlns:n0=\"http://dpd.com/common/service/types/EndOfDayService/1.0\">")
    // Ersetzen des End-Tags EndOfDayService
    body = body.replace("</endOfDayResponse>", "</n0:endOfDayResponse>")
    
    message.setBody(body)
    return message
}
