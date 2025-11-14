import com.sap.it.api.mapping.*;

def String getFax(String header, MappingContext context){
    
    String internalID = context.getProperty("InternalID");
    String fax = context.getProperty(internalID + "_fax");
    
	return fax;
}