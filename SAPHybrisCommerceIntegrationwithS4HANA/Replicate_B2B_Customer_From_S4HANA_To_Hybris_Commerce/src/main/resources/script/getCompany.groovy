import com.sap.it.api.mapping.*;


def String getCompany(String header, MappingContext context){
    
    String company = context.getProperty("Company");
    
    return company;
}
