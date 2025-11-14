import com.sap.it.api.mapping.*;
import com.sap.it.api.mapping.MappingContext;

def String getHeader(String header_name, String default_value, MappingContext context) {
    def propValue= context.getHeader(header_name);
    if(propValue == "0000000000" && default_value != null) {
        return default_value;
    }
    return propValue;
}