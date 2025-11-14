import com.sap.it.api.mapping.*;


def String getPostalCode(String header, MappingContext context){
    
    String postalCode = context.getProperty("PostalCode");
    
    return postalCode;
}
