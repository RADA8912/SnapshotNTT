import com.sap.it.api.mapping.*;

def String getProperty(String prop, MappingContext context){
	String setValue = context.getProperty(prop)
	
	return setValue
}