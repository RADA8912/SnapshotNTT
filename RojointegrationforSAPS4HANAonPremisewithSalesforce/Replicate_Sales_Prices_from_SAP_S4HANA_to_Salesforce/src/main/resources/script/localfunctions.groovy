import com.sap.it.api.mapping.*;

def String getProperty(String arg1, MappingContext context){
	return context.getProperty(arg1) 
}