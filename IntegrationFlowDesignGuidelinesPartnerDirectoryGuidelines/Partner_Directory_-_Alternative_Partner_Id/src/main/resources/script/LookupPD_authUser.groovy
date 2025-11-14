import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;

def Message processData(Message message) {

    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
    if (service == null){
        throw new IllegalStateException("Partner Directory Service not found");
    }
    
    //Agency, Scheme & Alternative Partner Id
    def properties = message.getProperties(); 
    def Agency = properties.get("Agency");
    if (Agency == null){
        throw new IllegalStateException("Agency not found in sent message");   
    }
    def Scheme = properties.get("Scheme");
    if (Scheme == null){
        throw new IllegalStateException("Scheme not found in sent message");   
    }
    def AlternativePid = properties.get("AlternativePid");
    if (AlternativePid == null){
        throw new IllegalStateException("AlternativePid not found the sent message");   
    }
    
    // map alternative partner ID to partner id
    def pid = service.getPartnerId(Agency, Scheme, AlternativePid);
    if (pid == null){
        throw new IllegalStateException("Partner ID not found for agency "+Agency+" , scheme "+Scheme+", and  id " + AlternativePid);   
        
    }
    message.setProperty("pid", pid);  

    return message;
}


