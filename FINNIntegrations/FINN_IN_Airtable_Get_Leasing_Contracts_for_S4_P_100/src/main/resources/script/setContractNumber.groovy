/* This method tries to set the SAP contract IDs if present. Otherwise
   they are empty and trigger the update process */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.*
def Message processData(Message message) {
   
   def body = message.getBody(java.lang.String);
   def slurper = new JsonSlurper().parseText(body);
   
    String result = ''
    String realContract = ''
    
    //Try to retrieve the contract numbers, if present
    try {
        result = slurper.value.InternalRealEstateNumber[-1]
        realContract = slurper.value.RealEstateContract[-1]
    } catch (Exception e){
     
    }

    //If result is enpty, set empty headers to trigger the update process
    if (result == ''){
        message.setHeader("InternalRealEstateNumber", '')
        message.setHeader("ContractID", '')
    //Otherwise, set the correct IDs
    } else {
        message.setHeader("InternalRealEstateNumber", result)
        message.setHeader("ContractID", realContract)
  
    }
    
        
    //Retrieve the original payload data and set this as a message
    message.setBody(message.getProperty("msg"))

   
    return message;
}