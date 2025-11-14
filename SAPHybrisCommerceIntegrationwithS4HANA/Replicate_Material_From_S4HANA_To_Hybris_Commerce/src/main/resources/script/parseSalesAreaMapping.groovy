import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.mapping.*; 
import com.sap.it.api.ITApiFactory;
import com.sap.it.api.ITApi;
import com.sap.it.api.mapping.ValueMappingApi;

def Message processData(Message message) {
    def salesArea = message.getProperty('CATALOG_VKORG');
    def channel = message.getProperty('CATALOG_VTWEG');

    def taxCountry = dynamicValueMap("SALESAREA","CHANNEL","TAX","TAXCOUNTRY",salesArea+"_"+channel );
 	if(taxCountry!='') {
	    message.setProperty("TaxCountry",taxCountry);
 	}
    return message;
}

def String dynamicValueMap(String sSalesArea, String sChannel, String tTax, String tTaxCountry, String key){
	def service = ITApiFactory.getApi(ValueMappingApi.class, null);
	if( service != null) {
		String val= service.getMappedValue(sSalesArea, sChannel, key, tTax, tTaxCountry);
		if ( val ==null )	{
    		return ""
		}
		else
    		return val
	}
	return "";
}