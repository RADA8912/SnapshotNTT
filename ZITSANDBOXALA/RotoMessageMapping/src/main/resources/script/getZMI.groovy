import com.sap.it.api.mapping.*;

def String getZmiFromProperties(String materialSales, MappingContext context){
    
    try{
        def propertyName = materialSales + "ZMI";
        def propertyValue = context.getProperty(propertyName);
        return propertyValue;
    } catch(Exception e) {
        return "";
    }
}