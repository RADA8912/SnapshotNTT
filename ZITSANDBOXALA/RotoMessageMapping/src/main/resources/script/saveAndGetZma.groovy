import com.sap.it.api.mapping.*;

def String getZmaFromProperties(String materialSales, MappingContext context){
    try{
        def propertyName = materialSales + "ZMA";
        def propertyValue = context.getProperty(propertyName);
        return propertyValue;
    } catch(Exception e) {
        return "";
    }

}