import com.sap.it.api.mapping.*;


def String getCountry(String header, MappingContext context){
    
    String country = context.getProperty("Country");
    
    return country;
}