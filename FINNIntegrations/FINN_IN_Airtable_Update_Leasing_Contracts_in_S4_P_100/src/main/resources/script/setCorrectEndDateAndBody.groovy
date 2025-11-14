/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*



def Message processData(Message message) {
    /*
    Parse the start date of the next leasing rate and set the end date 
    of the old one to the previous day
    */
    def xml = new XmlSlurper().parseText(message.getBody(String));
    def pattern = "yyyy-MM-dd"
  
    //Retrieve the valid_from date from the current rate
    def endDateCurrentRate = message.getProperties().get("ValidFrom")
    def valid_from = xml.valid_from.text()
    def change_date = xml.change_date.text()
    def value = message.getProperties().get("ConditionValue")
    def condPurp = message.getProperties().get("REExtConditionPurpose")
    
    
    //Define the end body
    String patchBody = ""
    
    
    //If both valid_froms are equal, just update the price
    def final_end_date = ''
    if (valid_from.equals(endDateCurrentRate)){
        final_end_date = ''
        message.setProperty("OnlyValueChange", "true") 
        patchBody = '{\n"REExtConditionPurpose":"' + condPurp + '",\n"REUnitPrice":' + value + '\n}'
        message.setBody(patchBody)
    } else {
       final_end_date = change_date
       patchBody = '{\n"REExtConditionPurpose":"' + condPurp + '",\n"REUnitPrice":' + value + ',\n"ValidityEndDate":"' + final_end_date + '"\n}'
    message.setBody(patchBody)

    }


    //Set the end date as property
    message.setProperty("EndDate", final_end_date)
    
    
    //Set the body for the correct update


    return message
}