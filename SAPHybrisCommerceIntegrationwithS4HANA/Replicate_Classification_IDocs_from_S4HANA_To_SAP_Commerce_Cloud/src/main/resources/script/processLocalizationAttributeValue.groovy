
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import groovy.time.*
import com.sap.it.api.mapping.*;
import java.util.regex.Pattern;
def Message processData(Message message) {
    def chrmas_full_xml = message.getBody(java.lang.String) as String;
         
    def cls_category_type = message.getProperty("CLS_KLART") as String;
    
    def chr_root = new XmlSlurper(false,false).parseText(chrmas_full_xml);
    def attributeValuePayload = processClassificationAttributeValue( chr_root ,cls_category_type);
    message.setBody('<chrmas>'+attributeValuePayload+'</chrmas>');
    return message;
}
def processClassificationAttributeValue(groovy.util.slurpersupport.GPathResult chr_root, String cls_category_type){
   def attr_code = chr_root.IDOC.E1CABNM.ATNAM.text();

  def batchAttributeValueLangList = [:]
  def batchLangChangeSets = [:];
  chr_root.IDOC.E1CABNM.E1CAWNM.each{it->
    it.E1CAWTM.each { lang->
      batchLangChangeSets.put(lang.SPRAS_ISO.text().toLowerCase(),lang.ATWTB.text())
    };
    batchAttributeValueLangList.put(it.ATWRT.text(),batchLangChangeSets )
    batchLangChangeSets = [:];
  };

  def builder = new StreamingMarkupBuilder()
 
  def attributeValueBatchParts = builder.bind {
       
      batchAttributeValueLangList.each{ key,value ->
        value.each {langKey,langValue->
            Content{
              language(langKey)
              ClassificationAttributeValues{
                ClassificationAttributeValue{
                  integrationKey('')
                  name(langValue)
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