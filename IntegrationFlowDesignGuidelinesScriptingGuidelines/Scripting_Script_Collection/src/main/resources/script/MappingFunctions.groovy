import com.sap.it.api.mapping.*;
import com.sap.it.api.mapping.MappingContext;

def String getheader(String header_name, MappingContext context) {

    def headervalue= context.getHeader(header_name);

    return headervalue;

}

def String getProperty(String property_name, MappingContext context) {

    def propValue= context.getProperty(property_name);

    return propValue;

}

def String setHeader(String header_name, String header_value, MappingContext context) {
    
    context.setHeader(header_name, header_value);
    
    
    return header_value;
}

def String setProperty(String property_name, String property_value, MappingContext context) {
    
    context.setProperty(property_name, property_value);
    
    
    return property_value;
}