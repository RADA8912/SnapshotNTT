import com.sap.it.api.mapping.*;
import java.util.UUID; 
import java.util.Arrays;
import java.util.Set;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;

// Script to get number range group code of business partner
def String getGroupCode(String arg1, MappingContext context){
    
    def groupCode = context.getProperty("bpGroupCode");
    
    return groupCode;
	
}