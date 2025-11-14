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
import de.itelligence.itxpress.transform.dhl2mh.RequestTransformation;
import javax.activation.DataHandler;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document
import de.itelligence.itxpress.transform.util.XMLUtil;

def Message processData(Message message) {

	def messageLog = messageLogFactory.getMessageLog(message);

	Map<String, DataHandler> attachments = message.getAttachments();
	attachments.each { entry -> messageLog.addAttachmentAsString("$entry.key","$entry.key", "text/plain") }
	

	//		Iterator<DataHandler> it = attachments.values().iterator()
	//	int counter;
	//	while(it.hasNext()) {
	//		counter++;
	//		DataHandler attachment = it.next()
	//		//	String contentType = attachment.getContentType();
	//		StreamSource stream = (StreamSource) attachment.getContent();
	//		Document doc = XMLUtil.createDocument(stream.getInputStream());
	//		messageLog.addAttachmentAsString("Attachment Content " + counter, XMLUtil.serializeToString(doc), "text/xml");
	//	}

	def body = message.getBody();
	def bodyAsInputStream = message.getBody(InputStream.class);
	if (bodyAsInputStream == null) {
		throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
	}
	def baos = new ByteArrayOutputStream();
	RequestTransformation.transform(bodyAsInputStream, baos);
	message.setBody(baos.toByteArray());


	return message;
}