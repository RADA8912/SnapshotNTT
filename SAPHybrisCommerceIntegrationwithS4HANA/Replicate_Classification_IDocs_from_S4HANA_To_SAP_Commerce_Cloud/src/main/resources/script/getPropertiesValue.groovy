import com.sap.it.api.mapping.*;


def String getChrmasAttributeName(String header,MappingContext context)
{
    return context.getProperty("CHRMAS_ATTRIBUTE_NAME");
}

def String getClsmasSystemVersion(String header,MappingContext context)
{ 
        
    return context.getProperty("systemVersion_version");
}

def String getClsmasSystemCatalogId(String header,MappingContext context)
{ 
        
    return context.getProperty("systemVersion_catalog_id");
}

def String getClsmasClassificationClass(String header,MappingContext context)
{
    return context.getProperty("classification_class");
}

def String getClfmasCode(String header,MappingContext context){
     return context.getProperty("ATNAM")+"_"+context.getProperty("ATWRT");
}

def String getChrmasCode(String header,MappingContext context){
     return context.getProperty("ATNAM");
}

def String  getChrmasAttributeType(String attribute,String attributeValue,String attributeType, MappingContext context){

    if (attribute == null || attribute == '')
        return "string";
    else if (attributeValue!=null && attributeValue !='')
        return "enum";
    return attributeType == 'DATE'?'date':(attributeType =='NUM'?'number':'string');
}

def String formatDefination(String attributeType, String displayFormat,String charNumber, String decimalPlace,String template, MappingContext context ){
    if("NUM"!=attributeType || displayFormat!= '0')
		return "";
		 
	int char_number = charNumber.toInteger();
		 
	int decimal = decimalPlace.toInteger(); 
	if(template.count("_") != char_number)
		return "";
	template = template.replace("_","0");
		
	for(int i=0;i<(char_number-(decimal+1));i++ )
		template = template.replaceFirst("0", "#");
	return template;
}

def String getAssignmentUnitType(String header,MappingContext context){
    
     return context.getProperty("UNIT_TYPE");
}
