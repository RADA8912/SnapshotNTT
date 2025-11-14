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
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import com.sap.it.api.mapping.*;

def Message processData(Message message) {
    def b2bCustomer_xml = message.getBody(java.lang.String) ;
    
    def builder = new StreamingMarkupBuilder()
    def b2bCustomer_root = new XmlSlurper().parseText(b2bCustomer_xml);
    if(!isMatchedOrLaterCommerceVersion(message, "1905-CEP"))
        b2bCustomer_root.B2BCustomer.sapBusinessPartnerID.findAll{it!='' || it.text()==''}*.replaceNode {};
    message.setBody(XmlUtil.serialize(b2bCustomer_root));
    return message;
}

def boolean isMatchedOrLaterCommerceVersion(Message message, String attributeVersionNumber){
    def commerceVersion = message.getProperty("COMMERCE_VERSION_NUMBER");
    if(commerceVersion == null) 
        return false;
    return commerceVersion.replace(".","").toUpperCase().compareTo(attributeVersionNumber.toUpperCase())>=0;
}
