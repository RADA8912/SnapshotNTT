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
def Message processData(Message message) {
    	def CHRMAS_XML = message.getBody(java.lang.String) as String;
    	
    	def MATMAS_XML = message.getProperty("MATMAS_SALES_DATA") as String;
    	def CLFMAS_XML = message.getProperty("CLFMAS_XML") as String;
    	def root_MATMAS = new XmlSlurper().parseText(MATMAS_XML);
    	def root_CLFMAS = new XmlSlurper().parseText(CLFMAS_XML);
    	def root_CHRMAS = new XmlSlurper().parseText(CHRMAS_XML);
        def code = root_CLFMAS.IDOC.E1OCLFM.OBJEK_LONG.text() == null || root_CLFMAS.IDOC.E1OCLFM.OBJEK_LONG.text().trim().size()<=0?root_CLFMAS.IDOC.E1OCLFM.OBJEK.text():root_CLFMAS.IDOC.E1OCLFM.OBJEK_LONG.text() ;
        def categoryType =  root_CLFMAS.IDOC.E1OCLFM.MAFID == "O"? root_CLFMAS.IDOC.E1OCLFM.KLART:"";
        def attributeID = root_CLFMAS."*".E1OCLFM.collect{it.MAFID == "O" ? it.E1AUSPM.ATNAM.text():""}.unique()[0];
        def productAttributes_header = "################ProductAttribute####################\nINSERT_UPDATE Product;code[unique=true];catalogVersion(Catalog(id),version)[unique=true];@"+attributeID+"[system=ERP_CLASSIFICATION_"+categoryType+",version=ERP_IMPORT,translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]";
        def String valueList = determineAttributeValue(root_CHRMAS,root_CLFMAS);
        def baseProductAttributes_value = ";"+code+";Default:Staged;"+valueList;
        def salesProductAttributes="";
        
        def catalogVersionList = root_MATMAS.IDOC.E1MARAM.E1MVKEM.collect{"Catalog_"+it.VKORG.text()+"_"+it.VTWEG.text()+":ERP_IMPORT"}.unique();
         
        for(String catalogVersion:catalogVersionList){
            salesProductAttributes +=salesProductAttributes.trim().size()>0?"\n":"";
            salesProductAttributes += ";"+code+";" + catalogVersion+";"+valueList;
        }
        message.setBody(productAttributes_header+"\n"+baseProductAttributes_value+"\n"+salesProductAttributes+"\n################ProductAttribute####################\n");
       return message;
}
