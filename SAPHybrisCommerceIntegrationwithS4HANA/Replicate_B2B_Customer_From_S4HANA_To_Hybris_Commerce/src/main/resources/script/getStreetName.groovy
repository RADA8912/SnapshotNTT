import com.sap.it.api.mapping.*;


def String getStreetName(String header, MappingContext context){
    
    String streetName = context.getProperty("StreetName");
    
    return streetName;
}