import com.sap.it.api.mapping.*;


def String getRegion(String header, MappingContext context){
    
    String region = context.getProperty("Region");
    
    return region;
}
