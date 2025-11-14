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
    def fullName = root.'**'.findAll{ node -> node.name() == 'FullName'}*.text()
    
    message.setProperty("findPersonAndFullName_person",person.toString().replaceAll("[\\[\\](){}]",""));
    message.setProperty("findPersonAndFullName_fullName", fullName.toString().replaceAll("[\\[\\](){}]",""));

    return message
}