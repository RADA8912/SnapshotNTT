import com.sap.it.api.mapping.*;


def String getTown(String header, MappingContext context){
    
    String town = context.getProperty("Town");
    
    return town;
}
