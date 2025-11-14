package com.nttdata.ndbs.btp.dhlex

import com.sap.gateway.ip.core.customdev.util.Message

import de.itelligence.itxpress.transform.dhl.ShipmentRequestPdfTransformation
import de.itelligence.itxpress.transform.labelconversion.IPdfProcessor
import de.itelligence.pi.itxpress.labelconversion.PdfToPngConverter
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document
import de.itelligence.itxpress.api.parser.ParameterContainer
import de.itelligence.itxpress.transform.util.XMLUtil;
import de.itelligence.itxpress.api.parser.ItxParameterParser;

def Message convertPdfToPng(Message message) {
	def body = message.getBody()
	def bodyAsInputStream = message.getBody(InputStream.class)
	if (bodyAsInputStream == null) {
		throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
	}

	final IPdfProcessor pdfToPngConverter = new PdfToPngConverter(true, null)
	ByteArrayOutputStream output = new ByteArrayOutputStream()

	new ShipmentRequestPdfTransformation().transform(bodyAsInputStream, output, pdfToPngConverter)

	message.setBody(output.toByteArray())
	return message
}


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
			URI uri = new URI(parameters.getUrl());
			uri.getHost()
			message.setHeader("Host", uri.getHost() + ":" + uri.getPort());
		}
	}

	attachments.clear();
	message.setAttachments(attachments);
	message.getAttachments().clear();
	message.setAttachmentWrapperObjects(new HashMap<>());

	def body = message.getBody();
	def bodyAsInputStream = message.getBody(InputStream.class);
	if (bodyAsInputStream == null) {
		throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
	}
	Document doc = XMLUtil.createDocument(bodyAsInputStream);
	messageLog.setStringProperty("itxContent", XMLUtil.serializeToString(doc));
	def bytes = XMLUtil.serialize(doc);
	message.setHeader("Content-Length", bytes.length);
	message.setBody(bytes);

	return message;
}