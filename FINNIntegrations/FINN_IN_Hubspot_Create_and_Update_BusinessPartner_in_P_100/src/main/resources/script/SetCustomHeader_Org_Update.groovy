import com.sap.gateway.ip.core.customdev.util.Message;

/**
 * This method stores important information for the monitoring of the iFlow. Business Partner, as well as names and the
 * payload is stored so that is visible within the monitor of the Cloud Integration
 * @param message
 * @return
 */

def Message processData(Message message) {

    def messageLog = messageLogFactory.getMessageLog(message);


    if(messageLog != null){

        //Read sapID
        def sapID = message.getProperties().get("BusinessPartner");
        //Set as Custom Header
        if(sapID!=null){
            messageLog.addCustomHeaderProperty("BusinessPartner", sapID);
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