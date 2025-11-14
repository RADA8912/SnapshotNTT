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
def Message processData(Message message) {
    //Get the File name
    def subject = message.getHeader("Subject", java.lang.String.class);
    def system_destination = message.getProperty("RCVPOR");


    if (subject.toLowerCase().contains("pl")) {

        message.setProperty("LIFNR", message.getProperty("AmazonPL"));
        
    } else if (subject.toLowerCase().contains("cz")) {
        message.setProperty("LIFNR", message.getProperty("AmazonCZ"));


    } else if (subject.toLowerCase().contains("fr")) {
        message.setProperty("LIFNR", message.getProperty("AmazonFR"));

    } else if (subject.toLowerCase().contains("uk")) {
        message.setProperty("LIFNR", message.getProperty("AmazonUK"));

    } else if (subject.toLowerCase().contains("de")) {
        message.setProperty("LIFNR", message.getProperty("AmazonDE"));

    } else {
        message.setProperty("LIFNR", 0);

    }


    

    return message;
}
