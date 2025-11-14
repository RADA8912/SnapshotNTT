import com.sap.it.api.mapping.*;
import  java.util.Random
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

/*Add MappingContext parameter to read or set headers and properties
def String customFunc1(String P1,String P2,MappingContext context) {
         String value1 = context.getHeader(P1);
         String value2 = context.getProperty(P2);
         return value1+value2;
}

Add Output parameter to assign the output value.
def void custFunc2(String P1,String P2, Output output, MappingContext context) {
        String value1 = context.getHeader(P1);
        String value2 = context.getProperty(P2);
        output.addValue(value1);
        output.addValue(value2);
}*/

def String getAttributeValueList(String header, MappingContext context){
    return context.getProperty(header);
}
def String getMaterialCode(String header, MappingContext context){
    
    return (context.getProperty("MATERIAL_LONG_CODE") == ''?context.getProperty("MATERIAL_CODE"):context.getProperty("MATERIAL_LONG_CODE")) ;
}
def String getCategoryType(String header, MappingContext context){
    def superCategoryType = context.getProperty("CATEGORY_POSEX") == '' ?context.getProperty("CATEGORY_CLASS_TYPE"):context.getProperty("CATEGORY_POSEX");
    return superCategoryType == null || superCategoryType == ''  ? context.getProperty("SUPERCATEGORY_TYPE"):superCategoryType;
}

def String getCatalogId(String header, MappingContext context){
    return loadMappedCatalogId('Catalog')+'_'+context.getProperty("CATALOG_VKORG")+"_"+context.getProperty("CATALOG_VTWEG");
}

def String getCLFCatalogVersion(String header, MappingContext context){
    if(context.getProperty("VKORG").equalsIgnoreCase(loadMappedCatalogId('Default')))
        return loadMappedCatalogId('Staged');
    return loadMappedCatalogId('ERP_IMPORT')
}

def String getCLFCatalogId(String header, MappingContext context){
    if(context.getProperty("VKORG").equalsIgnoreCase(loadMappedCatalogId('Default')))
        return loadMappedCatalogId('Default');
    return loadMappedCatalogId('Catalog')+'_'+context.getProperty("VKORG")+'_'+context.getProperty("VTWEG")
}

def String getMaterialUnitCode(String header, MappingContext context){
    return context.getProperty("MEINS")
}

def String getMaterialBaseProductCode(String header, MappingContext context){
    if(getNewSatnrLongValue(header, context) != null && getNewSatnrLongValue(header, context) != '') return getNewSatnrLongValue(header, context);
    return (context.getProperty("SATNR_LONG") == ''?context.getProperty("SATNR"):context.getProperty("SATNR_LONG")) ;
}

def String getNewSatnrLongValue(String header, MappingContext context){
    return context.getProperty("new_SATNR_LONG") ;
}

def String getValuePosition(String header, MappingContext context){
    Random random = new Random()
    return random.nextInt(10 ** 3)
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