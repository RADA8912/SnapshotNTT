package com.nttdata.ndbs.btp.soap

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.*;
import java.util.Map.Entry;
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document
import de.itelligence.itxpress.api.parser.ParameterContainer
import de.itelligence.itxpress.transform.util.XMLUtil;
import de.itelligence.itxpress.api.parser.ItxParameterParser;

def Message processData(Message message) {

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
		if(parameters.getContentType() != null && !parameters.getContentType().isEmpty()) {
			message.setHeader("Content-Type", parameters.getContentType());
		}
		if(parameters.getUrl() != null && !parameters.getUrl().isEmpty()) {
			URI uri = new URI(parameters.getUrl());
			String path = uri.getPath();
		    String query = uri.getQuery();
		    if (query != null) {
			    path = path + "?" + query;
		    }
			message.setProperty("pathAndQuery", path);
		}
		if(parameters.getApiParameter("Operation") != null){
			message.setProperty("operation", parameters.getApiParameter("Operation"));
		}
		for (Entry<String, String> entry : parameters.getApiParameters().entrySet()) {
			if (entry.getKey().startsWith("Header_")) {
				message.setHeader(entry.getKey().substring(7), entry.getValue());
			}
		}
	}

	message.setProperty('ITX_Auth', message.getHeader('Authorization', String.class))

	attachments.clear();
	message.setAttachments(attachments);
	message.getAttachments().clear();
	message.setAttachmentWrapperObjects(new HashMap<>())

	return message;
}
