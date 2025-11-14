import com.sap.it.api.ITApiFactory;
import com.sap.it.api.mapping.ValueMappingApi;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.util.Map;

def Message processData(Message message)
{
    def service = ITApiFactory.getApi(ValueMappingApi.class, null);
    def headers = message.getHeaders();
    def headervalue = headers.get("HeaderValue");
    def mappedValue = 
        service.getMappedValue("Frucht","US",headervalue,"Frucht","DE");

    message.setHeader("ValueMapped", mappedValue);

    def messageLog = messageLogFactory.getMessageLog(message);
    messageLog.setStringProperty("MappedValue", mappedValue);

    return message;
}