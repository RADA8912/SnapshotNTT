import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {

    // get queue names
    def propertyMap = message.getProperties()
    def incomingQueue = propertyMap.get("incomingQueue");
    def deadLetterQueue = incomingQueue + '_DLQ';
    // get test mode
    def headers = message.getHeaders();
    def testMode = headers.get("testMode");

    def messageLog = messageLogFactory.getMessageLog(message);
    if (messageLog != null) {
        PipelineLogger logger = PipelineLogger.newLogger(message)
        if (testMode != 'true'){
		    logger.addEntry("attachAdditionallnformation", "Maximum number of retries exceeded")
            logger.addEntry("attachAdditionallnformation", "Message removed from queue " + incomingQueue + " and sent to dead letter queue " + deadLetterQueue)            
        }
        messageLog.addAttachmentAsString('Additional Information', logger.getAuditLog4Print(), 'text/plain')
    }
    return message;
}