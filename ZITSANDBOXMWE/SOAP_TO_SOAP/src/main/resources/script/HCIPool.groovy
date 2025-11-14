
import com.sap.it.api.mapping.MappingContext

//Lesen von Exchange Properties
def String getProperty(String propertyName,MappingContext context){ 
    String PropertyValue= context.getProperty(propertyName); 
    PropertyValue= PropertyValue.toString(); return PropertyValue;
    }
//Lesen von Header Variablen 
def String getHeader(String headerName,MappingContext context){ 
    String HeaderValue = context.getHeader(headerName); 
    HeaderValue= HeaderValue.toString();
    return HeaderValue;
    }