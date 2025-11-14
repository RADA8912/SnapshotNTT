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

    //get personIDs from messageXML
    def person = root.'**'.findAll{ node -> node.name() == 'Person'}*.text()
    //get fullName from messageXML
    def fullName = root.'**'.findAll{ node -> node.name() == 'BusinessPartnerFullName'}*.text()
    //get businessPartnerCategory from messageXML
    def businessPartnerCategory = root.'**'.findAll{ node -> node.name() == 'BusinessPartnerCategory'}*.text()
    
    message.setProperty("matchPersonID_person",person.toString().replaceAll("[\\[\\](){}]",""));
    message.setProperty("matchPersonID_businessPartnerFullName", fullName.toString().replaceAll("[\\[\\](){}]",""));
    
    // matchPersonID_businessPartnerCategory == 1 -> person
    // matchPersonID_businessPartnerCategory == 2 -> organization
    message.setProperty("matchPersonID_businessPartnerCategory",businessPartnerCategory.toString().replaceAll("[\\[\\](){}]",""));
    
    return message
}