import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/*
*
* function to get person and fullName from BPAddress entity
* @message Message
*
*/
def Message processData(Message message) {
    
    //parse XML from message
    def xml = message.getBody(String.class)
    def root = new XmlSlurper().parseText(xml)

    //get BusinessPartner IDs from messageXML
    def businessPartner = root.'**'.findAll{ node -> node.name() == 'BusinessPartner'}*.text()
    def lastBpId = businessPartner.get(businessPartner.size()-1)
    message.setProperty("lastBpId", lastBpId);
    //message.setBody("<BusinessPartner>"+lastBpId+"</BusinessPartner>")


    return message
}