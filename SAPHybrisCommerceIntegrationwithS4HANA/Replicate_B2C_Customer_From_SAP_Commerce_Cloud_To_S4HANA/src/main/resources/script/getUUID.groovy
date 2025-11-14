import com.sap.it.api.mapping.*;
import java.util.UUID; 
import java.util.Arrays;
import java.util.Set;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

// Script to get UUID
def String getUUID(String arg1, MappingContext context){
    
    def uuid = context.getProperty("UUID");
    
    return uuid;
    
}