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
def Message processData(Message message) {
    
    def body = message.getBody(java.lang.String) as String;
    def root = new XmlSlurper().parseText(body);
    def description = [:];
	root.IDOC.E1CABNM.E1TEXTL.each{it->
		description.containsKey(it.LANGUAGE_ISO.text()) ? !description.get(it.LANGUAGE_ISO.text()).contains(it.TDLINE.text()+"|"+it.TDFORMAT.text())? description.put( it.LANGUAGE_ISO.text(),description.get(it.LANGUAGE_ISO.text())+"=="+it.TDLINE.text()+"|"+it.TDFORMAT.text()):"": description.put( it.LANGUAGE_ISO.text(), it.TDLINE.text()+"|"+it.TDFORMAT.text());
	};
    
    message.setProperty("ATNAM", root.IDOC.E1CABNM.ATNAM.text());
    message.setProperty("CHRMAS_FULL_DESCRIPTION", description);
    message.setProperty("CHRMAS_XML", body);
    return message;
}