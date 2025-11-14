import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.util.slurpersupport.GPathResult

def Message processData(Message message) {
    //String logFileContent = message.properties.get("logFileContent") as String
    String logFileContent = message.getBody(java.lang.String) as String
  //  logFileContent = logFileContent.replace("\\n", "\n")
    final String name = 'LogFile'
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        messageLog.addAttachmentAsString(name, logFileContent, 'text/plain')
     }
    return message
}
