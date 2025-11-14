import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.*;
import com.sap.it.api.mapping.*;

//This method returns value of a property, expects one input prop_name i.e. name of the property to be retrieved
def String get_property(String prop_name,MappingContext context) {

    def prop = context.getProperty(prop_name);

    return prop;

}

