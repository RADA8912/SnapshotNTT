import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import src.main.resources.script.PipelineLogger;

def Message processData(Message message) {

    //get custom header property name and value pairs
    def map = message.getHeaders();
    def custHeaderProps = map.get("customHeaderProperties");

    if(custHeaderProps){

    //split custom header properties into list of name/value pairs
        String[] vCustHeaderProps;
        String[] vPair;
        vCustHeaderProps = custHeaderProps.split('\\|');

        def messageLog = messageLogFactory.getMessageLog(message);
        if(messageLog){
    // add custom header properties if not empty
            for (int i=0; i<vCustHeaderProps.length; i++) {
                vPair = vCustHeaderProps[i].split(':')
                if(vPair.length == 2 && vPair[0] != ''){
                    messageLog.addCustomHeaderProperty(vPair[0], vPair[1]);
                }
            }
        }
    }

	// adding information to audit log using helper PipelineLogger class
	PipelineLogger logger = PipelineLogger.newLogger(message)
	logger.addEntry("addCustomHeaderProperties", "Current retry run: header SAPJMSRetries=" + message.headers.SAPJMSRetries.toString()?:"n/a")
	logger.addEntry("addCustomHeaderProperties", "List of custom header properties: header customHeaderProperties=" + message.headers.customHeaderProperties.toString()?:"n/a")
	if (logger.getAuditLog()) {
		message.setHeader('auditLogHeader', logger.getAuditLog())
	}

    return message;
}