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
import com.sap.it.api.mapping.*;

def Message processData(Message message) {
    
    def clsmas_full_xml = message.getBody(java.lang.String) as String;
    def chrmas_full_xml = message.getHeader("CHRMAS_XML",java.lang.String) as String;
	
    def clsmas_full_text = new XmlSlurper().parseText(clsmas_full_xml);
	def chrmas_full_text = new XmlSlurper().parseText(chrmas_full_xml);
	
    def classification_attribute_list  = chrmas_full_text.IDOC.E1CABNM.E1CAWNM.findAll{it.ATWRT!=''};
	def classification_attribute_name =  chrmas_full_text.IDOC.E1CABNM.ATNAM.text();
	def matched_clsmas = clsmas_full_text.message.CLSMAS04.IDOC.E1KLAHM.E1KSMLM.find{  it.ATNAM.text()  == classification_attribute_name};
		
	if(classification_attribute_list=='' || matched_clsmas == '') {
		message.setBody("");
		return message;
	}
			
	def insert_update_attribute_value_statements_wo_lang_list = [];
	def insert_update_attribute_value_statements_list = [];
	def type = "ERP_CLASSIFICATION_"+matched_clsmas.parent().KLART.text()+":ERP_IMPORT";
	 
	def systemVersion_catalog_id = "ERP_CLASSIFICATION_"+matched_clsmas.parent().KLART.text();
	def systemVersion_version = "ERP_IMPORT";
	 
	message.setProperty("systemVersion_catalog_id",systemVersion_catalog_id);
	message.setProperty("systemVersion_version",systemVersion_version);
 
	message.setBody(chrmas_full_xml);
	return message;
}
