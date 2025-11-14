import com.sap.it.api.mapping.*;
import com.sap.it.api.mapping.MappingContext;

/*Add MappingContext parameter to read or set headers and properties
def String customFunc1(String P1,String P2,MappingContext context) {
         String value1 = context.getHeader(P1);
         String value2 = context.getProperty(P2);
         return value1+value2;
}

Add Output parameter to assign the output value.
def void custFunc2(String[] is,String[] ps, Output output, MappingContext context) {
        String value1 = context.getHeader(is[0]);
        String value2 = context.getProperty(ps[0]);
        output.addValue(value1);
        output.addValue(value2);
}*/

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
    
    
    return header_value;
}