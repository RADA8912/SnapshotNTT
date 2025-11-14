import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*

/*
*
* This method extracts the contract number and company code for the reference
* from lease out to lease in.
*/


def Message processData(Message message) {
    //Get the body for the JSON Parsing
    def body = message.getBody(java.lang.String)
    //Parste the text as JSON
    def list = new JsonSlurper().parseText(body) 
   

    //Extract the needed values
    if(list.value[0] != null){
        String companyCode = list.value.CompanyCode[-1]
        String realContract = list.value.RealEstateContract[-1]

        //Store them as property
        message.setProperty("CompanyCodeLease", companyCode)
        message.setProperty("RealContractLease", realContract)

    }


  

    message.setBody(message.getProperties().get("msg"))
    return message;
}




