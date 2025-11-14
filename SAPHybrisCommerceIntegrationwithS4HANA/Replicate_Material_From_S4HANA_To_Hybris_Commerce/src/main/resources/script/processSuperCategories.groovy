import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;

def Message processData(Message message) {
	def product_xml = message.getBody(java.lang.String) as String;
    def clfmas_xml = message.getProperty('CLFMAS_XML');
    
    def isConfigurable = 'X'.equalsIgnoreCase (message.getProperty('isConfigurableProduct')) && 'true'.equalsIgnoreCase(message.getProperty('BASEPRODUCT')) && 'true'.equalsIgnoreCase(message.getProperty('cpq-configurable-category.enable'));
    def CPQCategoryPayload = '';
    
    if(isConfigurable){
        def CPQCategory = message.getProperty('cpq-configurable-category');
        CPQCategory = CPQCategory ==null||CPQCategory==''?'CPQConfigurableCategory':CPQCategory as java.lang.String;
        def sales_area_VTWEG = message.getProperty('CATALOG_VTWEG');
        def sales_area_VKORG = message.getProperty('CATALOG_VKORG');
        CPQCategoryPayload =  processCPQSuperCategory(CPQCategory,sales_area_VKORG,sales_area_VTWEG);
    }
    
    def root = new XmlSlurper(false,false).parseText(product_xml);
    
    if(clfmas_xml == null||!clfmas_xml.contains('CLFMAS02')){
        if(CPQCategoryPayload!= ''){
            def superCategories_xml = new XmlSlurper().parseText('<supercategories>'+CPQCategoryPayload+'</supercategories>');
            root.batchChangeSet.batchChangeSetPart.Products.Product.appendNode( superCategories_xml );
            message.setBody( XmlUtil.serialize( new StreamingMarkupBuilder().bind { mkp.yield root } ));
        }
        return message;
    }
    
    def clfmas_root = new XmlSlurper(false,false).parseText(clfmas_xml);
    def spuerCategoriesPayload= '';
    
    def superCategories = clfmas_root.children().each{ clf-> 
      spuerCategoriesPayload  += processSuperCategory( clf);
    }

	def clfmas = new XmlSlurper().parseText(clfmas_xml as String);
    def superCategories_xml = new XmlSlurper().parseText('<supercategories>'+CPQCategoryPayload+spuerCategoriesPayload+'</supercategories>');
    
    if(product_xml.contains('ERPVariantProducts'))
        root.batchChangeSet.batchChangeSetPart.ERPVariantProducts.ERPVariantProduct.appendNode( superCategories_xml );
    else
        root.batchChangeSet.batchChangeSetPart.Products.Product.appendNode( superCategories_xml );
        
    message.setBody( XmlUtil.serialize( new StreamingMarkupBuilder().bind { mkp.yield root } ));
	return message;
}
def String processSuperCategory(groovy.util.slurpersupport.GPathResult clfmas){
	def builder = new StreamingMarkupBuilder()
	def mappedCatalogVersion = loadMappedSuperCategoryCatalogId('ERP_IMPORT');
 	def mappedCatalogId = loadMappedSuperCategoryCatalogId('ERP_CLASSIFICATION'); 
 	
	def attributeValueBatchParts = builder.bind {
	    clfmas.IDOC.E1OCLFM.E1KSSKM.each{ classification ->
    		Category{
    			integrationKey('')
    			code(classification.CLASS[0].text())
    			catalogVersion {
    				CatalogVersion{
    					integrationKey('')
    					version(mappedCatalogVersion)
    					catalog{
    						Catalog{
    							integrationKey('')
    							id(mappedCatalogId+'_'+clfmas.IDOC.E1OCLFM.KLART.text())
    						}
    					}
    				}
    			}
    		}
	    }
	}
	return attributeValueBatchParts
}

def String processCPQSuperCategory(String CPQCategory,String sales_area_VKORG, String sales_area_VTWEG){
 	def builder = new StreamingMarkupBuilder()
 	
 	def mappedCatalogVersion = loadMappedCatalogId('ERP_IMPORT');
 	def mappedCatalogId = loadMappedCatalogId('Catalog');
 	def mappedDefaultCatalogVersion = loadMappedCatalogId('Staged');
 	def mappedDefaultCatalogId = loadMappedCatalogId('Default');
 	
	def attributeValueBatchParts = builder.bind {
		Category{
			integrationKey('')
			code(CPQCategory)
			catalogVersion {
				CatalogVersion{
					integrationKey('')
					version(sales_area_VKORG==null||sales_area_VKORG==''?mappedDefaultCatalogVersion:mappedCatalogVersion)
					catalog{
						Catalog{
							integrationKey('')
							id(sales_area_VKORG==null||sales_area_VKORG==''?mappedDefaultCatalogId:(mappedCatalogId +'_'+sales_area_VKORG+'_'+sales_area_VTWEG))
						}
					}
				}
			}
		}
	}
	return attributeValueBatchParts
}

def String loadMappedCatalogId(String catalogVersion){
    return dynamicValueMap("CATALOG","CATALOG","EXTERNAL","PARAMETERIZATION",catalogVersion);
}

def String loadMappedSuperCategoryCatalogId(String catalogVersion){
    return dynamicValueMap("CATALOG","CLASSIFICATIONSYSTEMVERSION","EXTERNAL","PARAMETERIZATION",catalogVersion);
}

def String dynamicValueMap(String  sAgency, String sIdentifier, String tAgency, String tIdentifier, String key){
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

