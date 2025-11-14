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
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

def Message processData(Message message) {
	def root = new XmlSlurper().parseText(message.getBody(java.lang.String) as String);
   if(root.IDOC.E1MARAM.E1CUCFG =='' || root.IDOC.E1MARAM.SATNR =='' && root.IDOC.E1MARAM.E1MARA1.SATNR_LONG =='') {
	     return message;
	}
   
	//CLFMAS
	def clfmas_xml = message.getProperty('CLFMAS_XML') ;
	 
    def productFeatueCollectionDelimiter = message.getProperty('productFeatureCollectionDelimiter') ;
    
    if(productFeatueCollectionDelimiter == null){
        productFeatueCollectionDelimiter = ",";
    }
    
	def cleanHook = message.getProperty("productFeatureCleanHook");
    def persistenceHook = message.getProperty("productFeaturePersistenceHook");
    
    cleanHook = cleanHook != null? cleanHook : "sapCpiProductFeatureCleanHook";
    persistenceHook = persistenceHook != null? persistenceHook : "sapCpiProductFeaturePersistenceHook";
    
	def clfmas_root = new XmlSlurper(false,false).parseText((clfmas_xml!=null?clfmas_xml:'<CLFMAS02/>') as String);
    
    message.setBody('<batchParts xmlns="">'+processCleanFeatures(root, cleanHook)+processProductFeatures(root, clfmas_root,productFeatueCollectionDelimiter, persistenceHook)+'</batchParts>')
	return message;
}

def String processCleanFeatures(groovy.util.slurpersupport.GPathResult material_text, String cleanHook) {
    def sale_areas = [];
    def mappedCatalogId = loadMappedCatalogId("Catalog");
    def mappedCatalogVersion = loadMappedCatalogId("ERP_IMPORT");
    def mappedDefaultCatalogId = loadMappedCatalogId("Default");
    def mappedDefaultCatalogVersion = loadMappedCatalogId("Staged");
    
	material_text.IDOC.E1MARAM.E1MVKEM.each{ it->
		sale_areas.add(mappedCatalogId+'_'+it.VKORG.text()+'_'+it.VTWEG.text());
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
						ERPVariantProducts{
								ERPVariantProduct{
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

def String processProductFeatures(groovy.util.slurpersupport.GPathResult material_text,groovy.util.slurpersupport.GPathResult clfmas_text, String productFeatueCollectionDelimiter, String persistenceHook) {
	def sale_areas = []
	def mappedCatalogId = loadMappedCatalogId("Catalog");
	def mappedCatalogVersion = loadMappedCatalogId("ERP_IMPORT");
	def mappedClassificationCatalogId = loadMappedSuperCategoryCatalogId("ERP_CLASSIFICATION");
	def mappedClassificationCatalogVersion = loadMappedSuperCategoryCatalogId("ERP_IMPORT");
	def mappedDefaultCatalogId = loadMappedCatalogId("Default");
	def mappedDefaultCatalogVersion = loadMappedCatalogId("Staged");

	material_text.IDOC.E1MARAM.E1MVKEM.each{ it->
		sale_areas.add(mappedCatalogId+'_'+it.VKORG.text()+'_'+it.VTWEG.text())
	}
	sale_areas.add(mappedDefaultCatalogId);
	def materialCode = material_text.IDOC.E1MARAM.MATNR_LONG == ''?material_text.IDOC.E1MARAM.MATNR.text():material_text.IDOC.E1MARAM.MATNR_LONG.text();
	def productFeatureValue=[]
	material_text.IDOC.E1MARAM.E1CUCFG.E1CUVAL.each{value->productFeatureValue.add(value.CHARC.text()+'#'+value.VALUE.text()+'#'+value.AUTHOR.text())};
	def classType = material_text.IDOC.E1MARAM.E1CUCFG.POSEX=='' ? material_text.IDOC.E1MARAM.E1CUCFG.E1CUINS.CLASS_TYPE.text():''
	def baseProductCode = material_text.IDOC.E1MARAM.E1MARA1.SATNR_LONG == ''?material_text.IDOC.E1MARAM.SATNR.text():material_text.IDOC.E1MARAM.E1MARA1.SATNR_LONG.text();

	int i=0;

	def clfmasFeatureValue=[];
		
	def builder = new StreamingMarkupBuilder();
	def productFeatureBatchParts = builder.bind {
		sale_areas.each { area ->
			batchChangeSet {
				batchChangeSetPart{
					method('POST')
					headers{
						header{
							headerName('Pre-Persist-Hook')
							headerValue(persistenceHook)
						}
					}
					ERPVariantProducts{
						ERPVariantProduct{
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
							features{
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

								clfmas_text.children().each{ clfmas_value->
									clfmas_value.IDOC.E1OCLFM.E1AUSPM.each{value->clfmasFeatureValue.add(value.ATNAM.text()+'#'+(value.ATWRT!=''?value.ATWRT.text():value.ATFLV.text()+productFeatueCollectionDelimiter+ value.ATFLB.text()) +'#'+value.AUTHOR.text())};
									def clfclassType = clfmas_value.IDOC.E1OCLFM.MAFID.text() == 'O' ? clfmas_value.IDOC.E1OCLFM.KLART.text():'';
									if(classType != clfclassType) {
										clfmasFeatureValue.each {fature->
											def (attribute, attributeValue,authorValue) =fature.tokenize( '#' )
											ProductFeature{
												integrationKey('')
												author(authorValue)
												qualifier(mappedClassificationCatalogId+'_'+clfclassType+'/'+mappedClassificationCatalogVersion+'/'+attribute)
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
									clfmasFeatureValue = [];
								}
								
							}
							baseProduct{
								Product{
									integrationKey('')
									code(baseProductCode)
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
			}
		}
	}
	return productFeatureBatchParts
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