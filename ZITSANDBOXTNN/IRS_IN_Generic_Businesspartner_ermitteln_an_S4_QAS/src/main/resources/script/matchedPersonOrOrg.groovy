import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {


    // Get Properties from Payload
    def properties = message.getProperties();
    def findAddress_fullName = properties.get("findPersonAndFullName_fullName");
    def findAddress_person = properties.get("findPersonAndFullName_person");
    def findName_person = properties.get("matchPersonID_person");
    def findName_businessPartnerFullName = properties.get("matchPersonID_businessPartnerFullName");
    def findName_businessPartnerCategory = properties.get("matchPersonID_businessPartnerCategory");


    // Check if BP is person or organization and check if properties are equal
    if(findName_businessPartnerCategory == "1"){
        message.setProperty("matchPerson", findAddress_person.equals(findName_person))
    } else if (findName_businessPartnerCategory == "2"){
         message.setProperty("matchBusinessParterFullName", findAddress_fullName.equals(findName_businessPartnerFullName))
    }
    return message;
}