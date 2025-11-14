import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {
    
    // get headers
    def headers = message.getHeaders();
    // get properties
    def properties = message.getProperties();

    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
        if (service == null){
            throw new IllegalStateException("Partner Directory Service not found");
    }

    // Determine tenant stage based on system environment variable TENANT_NAME
    String Stage = headers.get("tenantStage");
    // Skip if header stage already exists
    if (Stage == null){
        Stage = service.getParameter(System.getenv("TENANT_NAME") , "SAP_Integration_Suite_Landscape" , String.class) ?: "PRD";
        message.setHeader("tenantStage", Stage);
    }
    
    // Fetch header partnerID
    String Pid = headers.get("partnerID");
        
    // Skip if header partnerID already exists
    if (Pid == null){

    // Use logical system name if message is an IDoc otherwise business system name
        String IdocSndPrn = headers.get("SAP_IDoc_EDIDC_SNDPRN");
        String Scheme = IdocSndPrn? 'LogicalSystemName' : 'BusinessSystemName';

    // Map staged business system name or logical system name to sender alias if maintained
        String Agency = Stage;
        String AlternativePid = IdocSndPrn ?: headers.get("SAP_Sender");
        String SenderAlias = service.getPartnerId(Agency, Scheme, AlternativePid);
    
    // For IDocs set header SAP_Sender: Map alias name to business system name otherwise keep logical system name
        if (IdocSndPrn != null){
            if (SenderAlias != null){
                Scheme = 'BusinessSystemName'
                message.setHeader("SAP_Sender", service.getAlternativePartnerId(Agency, Scheme, SenderAlias) ?: IdocSndPrn);
            } else{
                message.setHeader("SAP_Sender", IdocSndPrn);
            }
        }

    // Define Agency to determine partner ID via alternative partner
        Agency = SenderAlias ?: AlternativePid;
        message.setHeader("SAP_SenderAlias", Agency);

    // Enhance custom header properties if sender alias differs from actual sender
        if (AlternativePid != Agency) {
            String CustomHeaders = headers.get("customHeaderProperties");
            CustomHeaders = CustomHeaders ? CustomHeaders + '|' : '';
            CustomHeaders = CustomHeaders + 'SAP_SenderAlias' + ':' + Agency;
            message.setHeader("customHeaderProperties", CustomHeaders);
        }

    // Determine partner ID via alternative partner if maintained
        Scheme = 'SenderInterface';
        AlternativePid = headers.get("SAP_SenderInterface");
        Pid = service.getPartnerId(Agency, Scheme, AlternativePid);

    // Otherwise use a combination of sender component and sender interface
    // Note: not allowed chars need to be replaced
        if (Pid == null){
            Pid = Agency + '~' + headers.get("SAP_SenderInterface").toString().replaceAll("[&%/]","_");
        }    

    // create new header holding the Partner ID
        message.setHeader("partnerID", Pid);
    
    }

	// adding information to audit log using helper PipelineLogger class
    PipelineLogger logger = PipelineLogger.newLogger(message)
    def logEntry = new StringBuilder().append("Header SAP_Sender=").append(message.headers.SAP_Sender.toString() ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
	logEntry = new StringBuilder().append("Header SAP_SenderAlias=").append(message.headers.SAP_SenderAlias.toString() ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
    logEntry = new StringBuilder().append("Header SAP_SenderInterface=").append(message.headers.SAP_SenderInterface.toString() ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
	logEntry = new StringBuilder().append("Header SAP_ApplicationID=").append(message.headers.SAP_ApplicationID.toString() ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
	logEntry = new StringBuilder().append("Header testMode=").append(message.headers.testMode.toString() ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
	logEntry = new StringBuilder().append("Property CustomXError_Enabled=").append(message.properties.CustomXError_Enabled.toString() ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
	logEntry = new StringBuilder().append("Partner ID to read the Partner Directory entries: ").append(Pid ?: "n/a").toString();
	logger.addEntry("determinePartnerID", logEntry);
    if (logger.getAuditLog()) {
	    message.setHeader('auditLogHeader', logger.getAuditLog())
    }

    return message;
}