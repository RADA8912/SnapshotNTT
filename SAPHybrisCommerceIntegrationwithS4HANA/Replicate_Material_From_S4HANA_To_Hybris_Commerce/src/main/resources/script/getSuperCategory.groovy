import com.sap.it.api.mapping.*;

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

def String getSuperCategoryCode(String header, MappingContext context){
    
    def boolean isSuperCategory = context.getProperty("SUPERCATEGORY_FLAG") == "O";
    if(isSuperCategory)
        return context.getProperty("SUPERCATEGORY_CODE");
    
    return "";
}

def String getSuperCategoryCatelogVersion(String header, MappingContext context){
    
    def boolean isSuperCategory = context.getProperty("SUPERCATEGORY_FLAG") == "O";
    if(isSuperCategory)
        return  "ERP_IMPORT";
    
    return "";
}

def String getSuperCategoryCatelogID(String header, MappingContext context){
    
    def boolean isSuperCategory = context.getProperty("SUPERCATEGORY_FLAG") == "O";
    if(isSuperCategory)
        return "ERP_CLASSIFICATION_"+context.getProperty("SUPERCATEGORY_TYPE");
    
    return "";
}