import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.it.api.pd.PartnerDirectoryService;
import com.sap.it.api.ITApiFactory;
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {
    
    // get headers
    def headers = message.getHeaders();
    
    // Fetch receiver alias
    String ReceiverAlias = headers.get("SAP_ReceiverAlias");

    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
        if (service == null){
            throw new IllegalStateException("Partner Directory Service not found");
    }

    // Map receiver alias to business name if maintained otherwise keep alias    
    String Agency = headers.get("tenantStage") ?: "PRD";
    String Scheme = 'BusinessSystemName';
    String Pid = ReceiverAlias;

    String Receiver = service.getAlternativePartnerId(Agency, Scheme, Pid) ?: Pid;
    message.setHeader("SAP_Receiver", Receiver);
    
    // enhance custom header properties if receiver alias differs from actual receiver
    if (Receiver != ReceiverAlias) {
        String CustomHeaders = headers.get("customHeaderProperties");
        CustomHeaders = CustomHeaders ? CustomHeaders + '|' : '';
        CustomHeaders = CustomHeaders + 'SAP_ReceiverAlias' + ':' + ReceiverAlias;
        message.setHeader("customHeaderProperties", CustomHeaders);
    }

	// adding information to audit log using helper PipelineLogger class
    PipelineLogger logger = PipelineLogger.newLogger(message)
    def logEntry = new StringBuilder().append("Tenant Stage=").append(Agency ?: "n/a").toString();
	logger.addEntry("mapReceiverToBusinessName", logEntry);
    logEntry = new StringBuilder().append("Receiver Alias=").append(Pid ?: "n/a").toString();
	logger.addEntry("mapReceiverToBusinessName", logEntry);
    logEntry = new StringBuilder().append("Business System Name=").append(headers.get("SAP_Receiver") ?: "n/a").toString();
	logger.addEntry("mapReceiverToBusinessName", logEntry);
    if (logger.getAuditLog()) {
	    message.setHeader('auditLogHeader', logger.getAuditLog())
    }

    return message;
}