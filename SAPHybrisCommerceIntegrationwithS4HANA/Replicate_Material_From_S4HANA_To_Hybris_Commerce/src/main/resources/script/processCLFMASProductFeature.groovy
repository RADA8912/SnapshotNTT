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
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;

def Message processData(Message message) {
	def root = new XmlSlurper().parseText(message.getBody(java.lang.String) as String);
	def clfmas_xml = message.getProperty('CLFMAS_XML') ;
	if(root.IDOC.E1MARAM.E1CUCFG !=''  || clfmas_xml == null || clfmas_xml ==''|| !clfmas_xml.contains('E1AUSPM')) {
		return message;
	}

    def productFeatueCollectionDelimiter = message.getProperty('productFeatureCollectionDelimiter') ;
    
    if(productFeatueCollectionDelimiter == null){
        productFeatueCollectionDelimiter = ",";
    }
    
    def cleanHook = message.getProperty("productFeatureCleanHook");
    def persistenceHook = message.getProperty("productFeaturePersistenceHook");
    
    cleanHook = cleanHook != null? cleanHook : "sapCpiProductFeatureCleanHook";
    persistenceHook = persistenceHook != null? persistenceHook : "sapCpiProductFeaturePersistenceHook";
    
	def clfmas_root = new XmlSlurper(false,false).parseText(clfmas_xml as String);

    def productFeature = processProductFeatures(root,clfmas_root,productFeatueCollectionDelimiter, persistenceHook);
    
    message.setBody('<batchParts xmlns="">'+processCleanFeatures(root, cleanHook)+productFeature+'</batchParts>')
	return message;
}

def String processCleanFeatures(groovy.util.slurpersupport.GPathResult material_text, String cleanHook) {
    def sale_areas = []
    def mappedCatalogId = loadMappedCatalogId("Catalog");
    def mappedCatalogVersion = loadMappedCatalogId("ERP_IMPORT");
    def mappedDefaultCatalogId = loadMappedCatalogId("Default");
    def mappedDefaultCatalogVersion = loadMappedCatalogId("Staged");
    
		material_text.IDOC.E1MARAM.E1MVKEM.each{ it->
			sale_areas.add(mappedCatalogId+'_'+it.VKORG.text()+'_'+it.VTWEG.text())
		}
		sale_areas.add(mappedDefaultCatalogId);
		def materialCode = material_text.IDOC.E1MARAM.MATNR_LONG == ''?material_text.IDOC.E1MARAM.MATNR.text():material_text.IDOC.E1MARAM.MATNR_LONG.text();
		def builder = new StreamingMarkupBuilder()
		def productFeatureBatchParts = builder.bind {
			sale_areas.each { area ->
				batchChangeSet {
					batchChangeSetPart{
						method('POST')
						headers{
							header{
								headerName('Pre-Persist-Hook')
								headerValue(cleanHook)
							}
						}
						Products{
							Product{
								integrationKey('')
								code(materialCode)
								catalogVersion {
									CatalogVersion{
										integrationKey('')
										version(!area.equals(mappedDefaultCatalogId)?mappedCatalogVersion:mappedDefaultCatalogVersion)
										catalog{
											Catalog{
												integrationKey('')
												id(area)
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
	return productFeatureBatchParts
}
def String processProductFeatures(groovy.util.slurpersupport.GPathResult material_text,groovy.util.slurpersupport.GPathResult clfmas_text, String productFeatueCollectionDelimiter , String persistenceHook) {
    def sale_areas = []
    
    def mappedCatalogId = loadMappedCatalogId("Catalog");
    def mappedCatalogVersion = loadMappedCatalogId("ERP_IMPORT");
    def mappedDefaultCatalogId = loadMappedCatalogId("Default");
    def mappedDefaultCatalogVersion = loadMappedCatalogId("Staged");
    
	material_text.IDOC.E1MARAM.E1MVKEM.each{ it->
		sale_areas.add(mappedCatalogId+'_'+it.VKORG.text()+'_'+it.VTWEG.text())
	}
	sale_areas.add(mappedDefaultCatalogId);
	def materialCode = material_text.IDOC.E1MARAM.MATNR_LONG == ''?material_text.IDOC.E1MARAM.MATNR.text():material_text.IDOC.E1MARAM.MATNR_LONG.text();

    //def featureList = processFeatures(clfmas_text);
	//def featureList_xml = new XmlSlurper().parseText( featureList );
	def builder = new StreamingMarkupBuilder()
	def productFeatureBatchParts = builder.bind {
		sale_areas.each { area ->
		    def featureList = processFeatures(clfmas_text,materialCode, area, productFeatueCollectionDelimiter);
	        def featureList_xml = new XmlSlurper().parseText( featureList );
			batchChangeSet {
				batchChangeSetPart{
					method('POST')
					headers{
						header{
							headerName('Pre-Persist-Hook')
							headerValue(persistenceHook)
						}
					}
					Products{
						Product{
							integrationKey('')
							code(materialCode)
							catalogVersion {
								CatalogVersion{
									integrationKey('')
									version(!area.equals(mappedDefaultCatalogId)?mappedCatalogVersion:mappedDefaultCatalogVersion)
									catalog{
										Catalog{
											integrationKey('')
											id(area)
										}
									}
								}
							}
							mkp.yield  featureList_xml
						}
					}
				}
			}
		}
	}
	return productFeatureBatchParts
}

def String processFeatures(groovy.util.slurpersupport.GPathResult clfmas_root,String materialCode, String area, String productFeatueCollectionDelimiter){
    def productFeatureValue=[]
	def builder = new StreamingMarkupBuilder()
	int i=0; 
    def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
    def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
    def mappedCatalogId = loadMappedCatalogId("Catalog");
    def mappedCatalogVersion = loadMappedCatalogId("ERP_IMPORT");
    def mappedDefaultCatalogId = loadMappedCatalogId("Default");
    def mappedDefaultCatalogVersion = loadMappedCatalogId("Staged");
    
	def productFeatureBatchParts = '';
	clfmas_root.children().each{ clfmas_text->
		clfmas_text.IDOC.E1OCLFM.E1AUSPM.each{value->productFeatureValue.add(value.ATNAM.text()+'#'+(value.ATWRT!=''?value.ATWRT.text():value.ATFLV.text()+productFeatueCollectionDelimiter+ value.ATFLB.text()) +'#'+value.AUTHOR.text())};
		def classType = clfmas_text.IDOC.E1OCLFM.MAFID.text() == 'O' ? clfmas_text.IDOC.E1OCLFM.KLART.text():'';
		productFeatureBatchParts  += builder.bind {
			productFeatureValue.each {fature->
				def (attribute, attributeValue,authorValue) =fature.tokenize( '#' )
				ProductFeature{
					integrationKey('')
					author(authorValue)
					qualifier(mappedClassificationCatalogId+'_'+classType+'/'+mappedClassificationCatalogVersion+'/'+attribute)
					value(attribute+'_'+attributeValue)
					valuePosition(i++)
					product{
						Product{
							integrationKey('')
							code(materialCode)
							catalogVersion{
								CatalogVersion{
									integrationKey('')
									version(!area.equals(mappedDefaultCatalogId)?mappedCatalogVersion:mappedDefaultCatalogVersion)
									catalog{ 
										Catalog{ 
											integrationKey('')
											id(area)
										} 
									}
								}
							}
						}
					}
				}
			}
		}
		productFeatureValue=[]
	}
	return '<features>'+productFeatureBatchParts+'</features>';
}

def private  String loadMappedSuperCategoryCatalogId(String catalogVersion){
    return dynamicValueMap("CATALOG","CLASSIFICATIONSYSTEMVERSION","EXTERNAL","PARAMETERIZATION",catalogVersion);
}

def private String loadMappedCatalogId(String catalogVersion){
    return dynamicValueMap("CATALOG","CATALOG","EXTERNAL","PARAMETERIZATION",catalogVersion);
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