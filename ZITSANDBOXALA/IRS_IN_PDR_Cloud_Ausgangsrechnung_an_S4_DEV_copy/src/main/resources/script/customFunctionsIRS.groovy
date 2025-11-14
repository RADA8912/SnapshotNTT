import com.sap.it.api.mapping.*;
import java.time.*;


/**
* func to load property from iflow context
*/
def String getProperty(String propertyName,MappingContext context)  { 
    String PropertyValue= context.getProperty(propertyName); 
    PropertyValue= PropertyValue.toString(); 
    return PropertyValue; 
    
}

/**
* read header vars
*/
def String getHeader(String headerName,MappingContext context) { 
    String HeaderValue = context.getHeader(headerName); 
    HeaderValue= HeaderValue.toString();

    return HeaderValue; 
    
}
