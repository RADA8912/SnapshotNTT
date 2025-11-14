import com.sap.it.api.mapping.*;

def String getB2BUnitId(String header, MappingContext context){
    
    String B2BUnitId = context.getProperty("DefaultB2BUnit");
    
	return B2BUnitId;
}