import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {
    
    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
    if (service == null){
        throw new IllegalStateException("Partner Directory Service not found");
    }
    
    // get pid
    def headers = message.getHeaders();
    def Pid = headers.get("partnerID");
    if (Pid == null){
        throw new IllegalStateException("Partner ID not found in sent message");   
    }

    // read the extended receiver determination end point from the Partner Directory
    def reuseXRDEndpoint = service.getParameter("ReuseXRDEndpoint", Pid , String.class);
    
    // if the extended receiver determination end point exists, create a new exchange property containing the end point
    // otherwise, the exchange property is not created
    message.setProperty("reuseXRDEndpoint", reuseXRDEndpoint ?: null);
    
    // create a new exchange property with value true or false depending on whether the end point exists
    message.setProperty("reuseXRDBoolean", reuseXRDEndpoint ? 'true' : 'false');
    
    return message;
}