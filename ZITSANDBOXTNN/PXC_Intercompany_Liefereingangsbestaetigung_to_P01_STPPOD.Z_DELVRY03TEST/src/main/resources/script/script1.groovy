import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
    def Message processData(Message message) {
    def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null){
    Reader reader = message.getBody(Reader)
    def xml = new XmlSlurper().parse(reader)
    
    def idocNo = xml.'**'.findAll{ node -> node.name() == 'DbId'}*.text().toString().replaceAll("[\\[\\](){}]","")
    messageLog.addCustomHeaderProperty("IDocNo", idocNo);
    message.setHeader("idocNo",idocNo)
    }
    return message
}