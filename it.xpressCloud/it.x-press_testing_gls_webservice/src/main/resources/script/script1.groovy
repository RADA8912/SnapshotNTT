/*
 The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
	public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
    public void setProperty(java.lang.String name, java.lang.Object value)
    public java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> getSoapHeaders()
    public void setSoapHeaders(java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> soapHeaders) 
       public void clearSoapHeaders()
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import de.itelligence.itxpress.transform.util.XMLUtil;
import de.itelligence.itxpress.api.parser.ParameterContainer;
import de.itelligence.itxpress.api.parser.ItxParameterParser;

def Message processData(Message message) {

	def messageLog = messageLogFactory.getMessageLog(message);

	Map<String, DataHandler> attachments = message.getAttachments();

	Iterator<DataHandler> it = attachments.values().iterator()
	ParameterContainer parameters = null;
	while(it.hasNext()) {
		DataHandler attachment = it.next()
		StreamSource stream = (StreamSource) attachment.getContent();
		Document doc = XMLUtil.createDocument(stream.getInputStream());
		parameters = ItxParameterParser.parseParameters(doc.getDocumentElement());
	}
	
	message.setProperty("soapAction", parameters.getSoapAction());
	
	attachments.clear();
	message.setAttachments(attachments);

    def body = message.getBody();
	def bodyAsInputStream = message.getBody(InputStream.class);
	if (bodyAsInputStream == null) {
		throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
	}
	Document doc = XMLUtil.createDocument(bodyAsInputStream);
    messageLog.addAttachmentAsString("Body Content", XMLUtil.serializeToString(doc), "text/xml");
    message.setBody(XMLUtil.serializeToString(doc));
    
    
	return message;
}