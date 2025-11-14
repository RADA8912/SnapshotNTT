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
 */


import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.regex.Pattern;
import groovy.transform.Field;
import javax.xml.parsers.DocumentBuilderFactory
import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil


@Field def boolean isRawTextMode = false;

def Message processData(Message message) {
     def clsmas_xml = message.getBody(java.lang.String) as String;
	//message.setProperty("CLS_XML",clsmas_xml );
	def clsmas_text = new XmlSlurper().parseText(clsmas_xml);
	//message.setHeader("ATTRIBUTE",clsmas_text.IDOC.E1KLAHM.E1KSMLM.ATNAM.text());
	message.setHeader("ENTRY_ID",clsmas_text.IDOC.E1KLAHM.E1KSMLM.ATNAM.text());
	message.setHeader("DATASTORE_NAME","CHRMAS");
	message.setProperty("CLS_KLART",clsmas_text.IDOC.E1KLAHM.KLART.text());
	message.setProperty("CLS_CLASS",clsmas_text.IDOC.E1KLAHM.CLASS.text());
    return message;
}
