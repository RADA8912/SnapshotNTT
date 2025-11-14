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
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

def Message processData(Message message) {
     def   clsmas_xml = message.getProperty('CLSMAS_FULL_XML') as java.lang.String;//message.getBody(java.lang.String) ;
     if(clsmas_xml == null || clsmas_xml == ''){
         clsmas_xml = message.getBody(java.lang.String) ;
     }
    def builder = new StreamingMarkupBuilder()
	def cls_root = new XmlSlurper().parseText(clsmas_xml);
	def batchChangeSets = [:];
	cls_root.IDOC.E1KLAHM.E1SWORM.each{it->if(it.KLPOS.text()=='01') batchChangeSets.put(it.SPRAS_ISO.text(), it.KSCHL.text())};
	
	def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
    def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
    
	if(batchChangeSets.size()==0)
	    batchChangeSets.put('','');
	def messageLog = messageLogFactory.getMessageLog(message);
  	def batchParts = builder.bind {
			  batchParts(xmlns:'') {
					 batchChangeSet {
						 batchChangeSetPart{
							 method('POST')
							 ClassificationClasses{
								 ClassificationClass{
									 integrationKey('')
									 code(cls_root.IDOC.E1KLAHM.CLASS.text())
									  
									 localizedAttributes{
									 	batchChangeSets.each{  key,value-> 
									 	    Localized___ClassificationClass{
									 	        language(key.toLowerCase())
									 	        name(value)
									 	    }
									 	}
									 }
									 catalogVersion{
										 CatalogVersion{
											 integrationKey('')
											 version(mappedClassificationCatalogVersion)
											 catalog{
												 Catalog{
													 integrationKey('')
													 id(mappedClassificationCatalogId+'_'+cls_root.IDOC.E1KLAHM.KLART.text())
												 }
											 }
										 }
									 }
								 }
							 }
						 }
					 }
			 }
		  
  	}

	message.setBody(XmlUtil.serialize(batchParts));
    return message;
}

def private  String loadMappedSuperCategoryCatalogId(String catalogVersion){
    return dynamicValueMap("CATALOG","CLASSIFICATIONSYSTEMVERSION","EXTERNAL","PARAMETERIZATION",catalogVersion);
}

def private String dynamicValueMap(String  sAgency, String sIdentifier, String tAgency, String tIdentifier, String key){
	def service = ITApiFactory.getApi(ValueMappingApi.class, null);
	if( service != null) {
		String value= service.getMappedValue(sAgency, sIdentifier,key, tAgency, tIdentifier);
		if ( value == null )	{
    		return key;
		}
		else
    		return value;
	}
	return key;
}