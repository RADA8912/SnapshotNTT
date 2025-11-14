import com.sap.gateway.ip.core.customdev.util.Message;

/**
 * This script logs monitor relevant properties.
 * @param message
 * @return message
 */

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;


def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
        //Read HubspotID
        def hubspotID = message.getProperties().get("BPIdentificationNumberHub");        
        //Set as Custom Header
        if(hubspotID!=null){
        messageLog.addCustomHeaderProperty("HubspotID", hubspotID);
        }
        
      
        
        //Read OrganizationBPName1
        def organization1 = message.getProperties().get("OrganizationBPName1");
        //Set as Custom Header
        if(organization1!=null){
        messageLog.addCustomHeaderProperty("OrganizationBPName1", organization1);    
        
        
        }
        
          //Read OrganizationBPName1
        def organization2 = message.getProperties().get("OrganizationBPName2");
        //Set as Custom Header
        if(organization2!=null){
        messageLog.addCustomHeaderProperty("OrganizationBPName2", organization2);    
        
        
        }
        
     
        //Read Body
        def body = message.getBody(java.lang.String) as String;

        //Set as Custom Header
        if(body!=null){
        messageLog.addAttachmentAsString("Payload:", body, "text/plain");
        }
       
       
        
        
    }
    return message;
}