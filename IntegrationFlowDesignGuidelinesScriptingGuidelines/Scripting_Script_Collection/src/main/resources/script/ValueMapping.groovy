import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.ITApiFactory
import com.sap.it.api.mapping.ValueMappingApi

def Message readValueMapping(Message message) {

       //Properties 
       def map = message.getProperties();
       productId = map.get("id");
       
       def valueMapApi = ITApiFactory.getService(ValueMappingApi.class, null)
       def productCode = valueMapApi.getMappedValue('CompanyA', 'ID', productId, 'CompanyB', 'ProductCode')
       message.setProperty("productCode", productCode);
       
       return message;
}