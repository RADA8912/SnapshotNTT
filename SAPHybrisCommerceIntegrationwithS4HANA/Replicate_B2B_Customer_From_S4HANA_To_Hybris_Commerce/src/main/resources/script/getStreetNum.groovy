import com.sap.it.api.mapping.*;


def String getStreetNum(String header, MappingContext context){
    
    String streetNum = context.getProperty("StreetNumber");
    
    return streetNum;
}
