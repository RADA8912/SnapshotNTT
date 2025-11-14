import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {
    
    // get headers
    def headers = message.getHeaders();
    // get properties
    def properties = message.getProperties();
    
    // prep Alternative Partner
    String Agency = headers.get("SAP_Sender");
    String Scheme = 'SenderInterface';
    String AlternativePid = headers.get("SAP_SenderInterface");
    String Pid;
    
    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
        if (service == null){
            throw new IllegalStateException("Partner Directory Service not found");
    }
    
    // Use alternative partner if maintained
    Pid = service.getPartnerId(Agency, Scheme, AlternativePid);
    
    // Otherwise use a combination of sender component and sender interface
    if (Pid == null){
        Pid = Agency + '~' + AlternativePid;
    }
    
    // create new header holding the Partner ID
    message.setHeader("partnerID", Pid);

	// adding information to audit log using helper PipelineLogger class
	PipelineLogger logger = PipelineLogger.newLogger(message)
	logger.addEntry("determinePartnerID", "Header SAP_Sender=" + message.headers.SAP_Sender.toString()?:"n/a")
	logger.addEntry("determinePartnerID", "Header SAP_SenderInterface=" + message.headers.SAP_SenderInterface.toString()?:"n/a")
	logger.addEntry("determinePartnerID", "Header SAP_ApplicationID=" + message.headers.SAP_ApplicationID.toString()?:"n/a")
	logger.addEntry("determinePartnerID", "Header testMode=" + message.headers.testMode.toString()?:"n/a")
	logger.addEntry("determinePartnerID", "Property CustomXError_Enabled=" + message.properties.CustomXError_Enabled.toString()?:"n/a")
	logger.addEntry("determinePartnerID", "Partner ID to read the Partner Directory entries: " + Pid?:"n/a")
	if (logger.getAuditLog()) {
		message.setHeader('auditLogHeader', logger.getAuditLog())
	}

    return message;
}