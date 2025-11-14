/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.xml.*

/*
Extract the correct BP ID for retrieveing the business partner
Lease In - Spendesk
Lease Out - Hubspot



*/

def Message processData(Message message) {
    def xmlRoot = new XmlSlurper().parseText(message.getBody(String));
    //def hubID = xmlRoot.el_sap_export_general_data.business_partner_hubspot.text()
    def spendID = xmlRoot.el_sap_export_general_data.business_partner_spendesk.text()
    //def contractType = xmlRoot.el_sap_export_general_data.leasing_conditions.condition_type.text()


    message.setProperty("PartnerID", spendID)
    message.setBody(spendID)



 /*
    if (contractType.equals('IV10')){
        message.setProperty("PartnerID", spendID)
        message.setBody(spendID)

    } 
    
   
    else {
        
        
        //message.setProperty("ParnterID", hubID)
        //message.setBody(hubID)
    }
    */
    
    
    
    return message;
}