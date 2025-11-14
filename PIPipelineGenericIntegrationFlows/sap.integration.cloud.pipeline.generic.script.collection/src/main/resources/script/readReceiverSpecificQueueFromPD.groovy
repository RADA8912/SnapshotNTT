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
    
    // get pid (pid equals SAP_Receiver)
    def headers = message.getHeaders();
    def Pid = headers.get("SAP_Receiver");
    // throw exception if pid is missing
    if (Pid == null){
        throw new IllegalStateException("Partner ID not found in sent message");   
    }
    
    // read the receiver-specific JMS queue from the Partner Directory
    def receiverSpecificQueue = service.getParameter("ReceiverSpecificQueue", Pid , String.class);
    
    // if the receiver-specific queue exists, create a new exchange property containing the queue name
    // otherwise, the exchange property is not created
    message.setProperty("receiverSpecificQueueName", receiverSpecificQueue ?: null);
    
    // create a new exchange property with value true or false depending on whether the queue exists
    message.setProperty("receiverSpecificQueueBoolean", receiverSpecificQueue ? 'true' : 'false');

	// adding information to audit log using helper PipelineLogger class
	PipelineLogger logger = PipelineLogger.newLogger(message)
	logger.addEntry("readReceiverSpecificQueueFromPD", "Header SAP_Receiver=" +  message.headers.SAP_Receiver.toString()?:"n/a")
	logger.addEntry("readReceiverSpecificQueueFromPD", "PID to read the receiver-specific JMS queue from the Partner Directory: " + Pid?:"n/a")
	logger.addEntry("readReceiverSpecificQueueFromPD", "Receiver specific queue from the Partner Directory: property receiverSpecificQueueName=" + message.properties.receiverSpecificQueueName.toString()?:"n/a")
	logger.addEntry("readReceiverSpecificQueueFromPD", "Receiver specific queue existence from the Partner Directory: property receiverSpecificQueueBoolean=" + message.properties.receiverSpecificQueueBoolean.toString()?:"n/a")	
	if (logger.getAuditLog()) {
		message.setHeader('auditLogHeader', logger.getAuditLog())
	}

    return message;
}