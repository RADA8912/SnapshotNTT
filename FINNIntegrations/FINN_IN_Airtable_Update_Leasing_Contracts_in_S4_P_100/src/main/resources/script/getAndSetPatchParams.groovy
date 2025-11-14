/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*

/**
 * This methods extracts paramters from the retrieved condition in SAP.
 * It is necessary to udpate the existing leasing rate. These 
 * are uniqude identifiers.
 * At the end, the body is set to the leasing condition from Airtable.
 * 
 * */



def Message processData(Message message) {
    def json = new JsonSlurper().parseText(message.getBody(String));
    
    if (!(json.value.toString() == '[]')){

       
        message.setProperty("InternalRealEstateNumber", json.value.InternalRealEstateNumber[-1].toString())
        message.setProperty("REStatusObjectCalculation", json.value.REStatusObjectCalculation[-1].toString())
        message.setProperty("REConditionType", json.value.REConditionType[-1].toString())
        message.setProperty("REExtConditionPurpose", json.value.REExtConditionPurpose[-1].toString())
        message.setProperty("ValidityStartEndDateValue", json.value.ValidityStartEndDateValue[-1].toString())
        message.setProperty("REUnitPrice", json.value.REUnitPrice[-1].toString())
    	message.setProperty("ValidFrom", json.value.ValidityStartDate[-1].toString())
    
        
    }

    
    message.setBody(message.getProperties().get("ConditionMessage"))

    return message;
}

