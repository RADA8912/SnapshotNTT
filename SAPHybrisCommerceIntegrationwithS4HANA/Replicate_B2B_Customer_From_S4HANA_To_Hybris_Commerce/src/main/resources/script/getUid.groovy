import com.sap.it.api.mapping.*;

// get UID as email and UID
def String getUid(String header, MappingContext context){
    
    String internalId = context.getProperty("InternalID");
    String uid = context.getProperty(internalId + "_uid");
    
	return uid;
}