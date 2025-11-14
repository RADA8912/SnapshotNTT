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
import javax.xml.parsers.DocumentBuilderFactory
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil


def Message processData(Message message) {
    def chrmas_full_xml = message.getBody(java.lang.String) as String;
    def clsmas_full_xml = message.getHeader("CLSMAS_FULL_XML",java.lang.String) as String;

	def clsmas_full_text = new XmlSlurper().parseText(clsmas_full_xml);
 	def chrmas_full_text = new XmlSlurper().parseText(chrmas_full_xml);
	
	def systemVersion_catalog_id = "ERP_CLASSIFICATION_"+clsmas_full_text.IDOC.E1KLAHM.KLART.text();
	def systemVersion_version = "ERP_IMPORT";
	def classification_class = clsmas_full_text.IDOC.E1KLAHM.CLASS.text();
	
	message.setProperty("systemVersion_catalog_id",systemVersion_catalog_id);
	message.setProperty("systemVersion_version",systemVersion_version);
	message.setProperty("classification_class",classification_class);
	
	def classification_attribute_list  = clsmas_full_text.IDOC.E1KLAHM.E1KSMLM.findAll{it.ATNAM!=''};
	
	def type = "ERP_CLASSIFICATION_"+clsmas_full_text.IDOC.E1KLAHM.KLART.text()+":ERP_IMPORT";
 
	def all_matched_chrmas_xml = "<CHRMAS></CHRMAS>"
	def all_matched_chrmas = new XmlSlurper(false,false).parseText(all_matched_chrmas_xml);
	
	def value_list = classification_attribute_list.collect{cls->
		def value_name = cls.ATNAM.text();
		def matched_chrmas = chrmas_full_text.message.CHRMAS05.IDOC.E1CABNM.find{  it.ATNAM.text()  == value_name};
	 
	    if(matched_chrmas != '')
	        all_matched_chrmas.appendNode(matched_chrmas.parent().parent());
	}
	message.setBody(XmlUtil.serialize( new StreamingMarkupBuilder().bind { mkp.yield all_matched_chrmas } ));
    return message;
}
