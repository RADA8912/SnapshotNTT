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
import org.w3c.dom.Document
import com.sap.gateway.ip.core.customdev.util.Message;
import de.itelligence.itxpress.transform.IFTSTAToXML
import de.itelligence.itxpress.transform.util.XMLUtil;

def Message processData(Message message) {

	def body = message.getBody();
	def bodyAsInputStream = message.getBody(InputStream.class);
	if (bodyAsInputStream == null) {
		throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
	}

	Reader inputStreamReader = null;
	Document doc = null;

	try {
		IFTSTAToXML converter = new IFTSTAToXML();

		StringBuffer stringBuffer = new StringBuffer();
		inputStreamReader = new InputStreamReader(input.getInputPayload().getInputStream(), "UTF-8");
		char[] buffer = new char[1024];
		int rsz;
		while ((rsz = inputStreamReader.read(buffer, 0, buffer.length)) != -1) {

			if (rsz < 0)
				break;
			stringBuffer.append(buffer, 0, rsz);
		}

		String iftsta93a = stringBuffer.toString();
		doc = converter.transform(iftsta93a, "DHL");
	} catch (IOException e) {
		throw new Exception("Error while converting IFTSTA to XML: " + e.getMessage(), e);
	} finally {
		if (inputStreamReader != null) {
			try {
				in.close();
			} catch (IOException e) {
				throw new Exception("Error while closing input stream: " + e.getMessage(), e);
			}
		}
	}

	message.setBody(XMLUtil.serialize(doc));

	return message;
	
}