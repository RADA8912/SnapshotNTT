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
import com.sap.it.api.mapping.*;
import java.util.regex.Pattern;
def Message processData(Message message) {
    def chrmas_full_xml = message.getBody(java.lang.String) as String;
    
    def chr_root = new XmlSlurper(false,false).parseText(chrmas_full_xml);
    
    def cls_category_type = message.getProperty("CLS_KLART") as String;
    
    def attributeValuePayload= '';
  
    def matched_chrmas = chr_root.children().each{ chr-> 
      attributeValuePayload  += processClassificationAttributeValue( chr ,cls_category_type);
    }
 
    message.setBody(XmlUtil.serialize('<batchParts>'+attributeValuePayload+'</batchParts>') );
    return message;
}
def processClassificationAttributeValue(groovy.util.slurpersupport.GPathResult chr_root, String cls_category_type){
   def attr_code = chr_root.CHRMAS05.IDOC.E1CABNM.ATNAM.text();

  def batchAttributeValueLangList = [:]
  def batchLangChangeSets = [:];
  chr_root.CHRMAS05.IDOC.E1CABNM.E1CAWNM.each{it->
    it.E1CAWTM.each { lang->
      batchLangChangeSets.put(lang.SPRAS_ISO.text(),lang.ATWTB.text())
    };
    batchAttributeValueLangList.put(it.ATWRT.text(),batchLangChangeSets )
    batchLangChangeSets = [:];
  };

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
                        LocalizedClassificationAttributeValue{
                            language(langKey.toLowerCase())
                            name(langValue)
                        }
                    }
                  }
                  code(attr_code+"_"+key)
                  systemVersion{
                    ClassificationSystemVersion{
                      integrationKey('')
                      version('ERP_IMPORT')
                      catalog{
                        Catalog{
                          integrationKey('')
                          id('ERP_CLASSIFICATION_'+cls_category_type)
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