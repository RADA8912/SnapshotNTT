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

    def name1 = root.A_BusinessPartner.Name1
    def name2 = root.A_BusinessPartner.Name2
    def name3 = root.A_BusinessPartner.Name3

    if(name1 !='' || name2 !='' || name3 !=''){
        message.setProperty("nameExists", true)
    }
    return message
}