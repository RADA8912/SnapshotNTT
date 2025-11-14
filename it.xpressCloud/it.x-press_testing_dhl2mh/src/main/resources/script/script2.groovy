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
import java.util.HashMap;
import de.itelligence.itxpress.transform.dhl2mh.ResponseTransformation;
def Message processData(Message message) {
    //Body 
       def body = message.getBody();
       def bodyAsInputStream = message.getBody(InputStream.class);
       if (bodyAsInputStream == null) {
           throw new NullPointerException("No Content transformer from " + body.class + " to InputStream registered.")
       }
       
       def baos = new ByteArrayOutputStream();
       ResponseTransformation.transform(bodyAsInputStream, baos);
       message.setBody(baos.toByteArray());
       return message;
}