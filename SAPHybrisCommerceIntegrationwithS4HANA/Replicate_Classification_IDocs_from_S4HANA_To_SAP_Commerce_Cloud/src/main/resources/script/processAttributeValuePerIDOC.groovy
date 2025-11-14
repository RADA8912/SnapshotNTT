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
import groovy.time.*
import java.util.regex.Pattern;
import com.sap.it.api.mapping.*;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

def Message processData(Message message) {
    def chrmas_full_xml = message.getBody(java.lang.String) as String;
    if( chrmas_full_xml == null|| chrmas_full_xml.contains("NOTFOUND") ||chrmas_full_xml ==''){
         message.setBody("<NOTFOUND/>");
        return message;
    }
    def chr_root = new XmlSlurper(false,false).parseText(chrmas_full_xml);
    
    def cls_category_type = message.getProperty("CLS_KLART") as String;
    
    def attributeValuePayload= processClassificationAttributeValue( chr_root ,cls_category_type);
   
    message.setBody(XmlUtil.serialize('<batchParts xmlns="">'+attributeValuePayload+'</batchParts>') );
    return message;
}
def processClassificationAttributeValue(groovy.util.slurpersupport.GPathResult chr_root, String cls_category_type){
   def attr_code = chr_root.IDOC.E1CABNM.ATNAM.text();

  def batchAttributeValueLangList = [:]
  def batchLangChangeSets = [:];
  if(chr_root.IDOC.E1CABNM.E1CAWNM.ATWRT == '')
    return '';
  chr_root.IDOC.E1CABNM.E1CAWNM.each{it->
    it.E1CAWTM.each { lang->
      batchLangChangeSets.put(lang.SPRAS_ISO.text(),lang.ATWTB.text())
    };
    batchAttributeValueLangList.put(it.ATWRT.text(),batchLangChangeSets )
    batchLangChangeSets = [:];
  };

  def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
  def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
  
  def builder = new StreamingMarkupBuilder()
 
  def attributeValueBatchParts = builder.bind {
      
      batchAttributeValueLangList.each{ key,value ->
          batchChangeSet {
            batchChangeSetPart{
              method('POST')
              ClassificationAttributeValues{
                ClassificationAttributeValue{
                  integrationKey('')
                  localizedAttributes{
                    value.each {langKey,langValue->
                        Localized___ClassificationAttributeValue{
                            language(langKey.toLowerCase())
                            name(langValue)
                        }
                    }
                  }
                  code(attr_code+"_"+key)
                  systemVersion{
                    ClassificationSystemVersion{
                      integrationKey('')
                      version(mappedClassificationCatalogVersion)
                      catalog{
                        Catalog{
                          integrationKey('')
                          id(mappedClassificationCatalogId+'_'+cls_category_type)
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
    return attributeValueBatchParts;
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