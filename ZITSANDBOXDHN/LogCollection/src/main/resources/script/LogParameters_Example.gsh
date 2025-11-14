import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/**
 * Log parameters
 */
def Message processData(Message message) {

	def pmap = message.getProperties();
	def messageLog = messageLogFactory.getMessageLog(message);

	String replicationTargetSystem = pmap.get("REPLICATION_TARGET_SYSTEM");
	String enableLogging = pmap.get("ENABLE_PAYLOAD_LOGGING");

	if(messageLog != null){
		messageLog.setStringProperty("REPLICATION_TARGET_SYSTEM ", replicationTargetSystem);
		messageLog.setStringProperty("ENABLE_PAYLOAD_LOGGING ", enableLogging);
	}

	return message;
}