import com.sap.it.api.mapping.*;


def String getProperty(String arg ,MappingContext context){
	return context.getProperty(arg); 
}