import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*

/*
*
* This method extracts the contract number and company code for the reference
* from lease out to lease in.
*/


def Message processData(Message message) {
    //Get the body for the JSON Parsing
    def body = message.getBody(java.lang.String)
    //Parste the text as JSON
    def list = new XmlSlurper().parseText(body) 
   
    //Extract the contractType
    String contractType = list.el_sap_export_general_data.contract_type.toString().substring(0,4)
    //Set the real contract number
    if (contractType.equals('YI20')){
        number = message.getProperties().get("CarID")
        message.setProperty("old_contract_number", number)
    } else {
        number = message.getProperties().get("SubscriptionID")
        message.setProperty("old_contract_number", number)
    }

    message.setBody(message.getProperties().get("msg"))
    return message;
}




