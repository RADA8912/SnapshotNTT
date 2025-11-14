import com.sap.it.api.mapping.*;


def String getPoBox(String header, MappingContext context){
    
    String poBox = context.getProperty("Pobox");
    
    return poBox;
}
