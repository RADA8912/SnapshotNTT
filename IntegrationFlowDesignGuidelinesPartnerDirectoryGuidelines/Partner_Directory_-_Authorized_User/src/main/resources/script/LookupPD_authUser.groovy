    import com.sap.gateway.ip.core.customdev.util.Message;
    import java.util.HashMap;
    import com.sap.it.api.pd.PartnerDirectoryService;
    import com.sap.it.api.ITApiFactory;

def Message processData(Message message) {

    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
    if (service == null){
        throw new IllegalStateException("Partner Directory Service not found");
    }
	
    // Partner Authorization
    def headers = message.getHeaders();
    def user = headers.get("SapAuthenticatedUserName");
    if (user == null){
        throw new IllegalStateException("User is not set in the header 'SapAuthenticatedUserName'")      
    }
    def Pid = service.getPartnerIdOfAuthorizedUser(user);
    if (Pid == null){
		throw new IllegalStateException("No partner ID found for user "+user);
    }
    message.setProperty("pid", Pid);    

    return message;
}


