import com.sap.gateway.ip.core.customdev.util.Message
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {

    // get queue names
    def propertyMap = message.getProperties()
    def incomingQueue = propertyMap.get("incomingQueue");
    def deadLetterQueue = new StringBuilder(incomingQueue).append('_DLQ').toString();
    def customStatus = propertyMap.get("SAP_MessageProcessingLogCustomStatus")?:'NONE';
    // get test mode
    def headers = message.getHeaders();
    def testMode = headers.get("testMode");

    // adding information to audit log using helper PipelineLogger class
    def messageLog = messageLogFactory.getMessageLog(message);
    if (messageLog != null) {
        PipelineLogger logger = PipelineLogger.newLogger(message)
        if (testMode != 'true' && !customStatus.contains("OptionNotSupported") && incomingQueue != null){
		    logger.addEntry("attachAdditionallnformation", "Maximum number of retries exceeded")
            def logEntry = new StringBuilder().append("Message removed from queue ").append(incomingQueue).append(" and sent to dead letter queue ").append(deadLetterQueue).toString();
            logger.addEntry("attachAdditionallnformation", logEntry);            
        }
        messageLog.addAttachmentAsString('Additional Information', logger.getAuditLog4Print(), 'text/plain')
    }
    return message;
}