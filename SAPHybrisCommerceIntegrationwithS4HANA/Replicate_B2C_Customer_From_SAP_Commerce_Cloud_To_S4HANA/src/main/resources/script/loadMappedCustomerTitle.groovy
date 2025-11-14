import com.sap.it.api.mapping.*;
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;
 

def public String loadMappedCustomerTitle(String title){
    if(title ==null)
        return ''; 
    return dynamicValueMap("CUSTOMER","TITLE","ERP","VALUE",title.toUpperCase());
}

def private String dynamicValueMap(String  sAgency, String sIdentifier, String tAgency, String tIdentifier, String key){
	def service = ITApiFactory.getApi(ValueMappingApi.class, null);
	if( service != null) {
		String value= service.getMappedValue(sAgency, sIdentifier,key, tAgency, tIdentifier);
		if ( value == null )	{
    		return key;
		}
		else
    		return value;
	}
	return key;
}