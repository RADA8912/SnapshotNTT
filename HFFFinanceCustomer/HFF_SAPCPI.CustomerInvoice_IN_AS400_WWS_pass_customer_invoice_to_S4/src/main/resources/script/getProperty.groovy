import com.sap.it.api.mapping.*;
import com.sap.it.api.mapping.MappingContext;

def String getProperty(String property_name, String default_value, MappingContext context) {
    def propValue= context.getProperty(property_name);
    if(propValue == "0000000000" && default_value != null) {
        return default_value;
    }
    return propValue;
}