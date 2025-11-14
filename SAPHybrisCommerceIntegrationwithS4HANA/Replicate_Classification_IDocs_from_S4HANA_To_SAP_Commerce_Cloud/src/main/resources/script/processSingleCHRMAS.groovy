import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import groovy.time.*
import com.sap.it.api.mapping.*;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

def Message processData(Message message) {
    def chrmas_full_xml = message.getProperty("CHRMAS_XML") as String;
    def cls_full_xml = message.getBody(java.lang.String) as String;  

    def chr_root = new XmlSlurper(false,false).parseText(chrmas_full_xml);
    def cls_root = new XmlSlurper(false,false).parseText(cls_full_xml);
    
    def attributeValuePayload   = processClassificationAttributeValue( chr_root ,cls_root);
    
    message.setBody('<batchParts xmlns="">'+attributeValuePayload+ '</batchParts>');
    return message;
}
def processClassificationAttributeValue(groovy.util.slurpersupport.GPathResult chr_root, groovy.util.slurpersupport.GPathResult cls_root ){
   def attr_code = chr_root.IDOC.E1CABNM.ATNAM.text();

  def batchAttributeValueLangList = [:]
  def batchLangChangeSets = [:];
  chr_root.IDOC.E1CABNM.E1CAWNM.each{it->
    it.E1CAWTM.each { lang->
      batchLangChangeSets.put(lang.SPRAS_ISO.text(),lang.ATWTB.text())
    };
    batchAttributeValueLangList.put(it.ATWRT.text(),batchLangChangeSets )
    batchLangChangeSets = [:];
  };
  
  def cls_category_types = []; 

  cls_root.CLSMAS04.E1KSMLM.each{it->
    cls_category_types.add(it.KLART.text() );
  };
  
  def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
  def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
  
  def builder = new StreamingMarkupBuilder()
 
  def attributeValueBatchParts = builder.bind {
      cls_category_types.each{cls_category_type->
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