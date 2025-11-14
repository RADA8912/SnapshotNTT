import com.sap.gateway.ip.core.customdev.util.Message;



def Message processData(Message message) {
    
    def messageLog = messageLogFactory.getMessageLog(message);
    
    
    if(messageLog != null){
        
        //Read SpendeskID
        def spendeskID = message.getProperties().get("SpendeskID");        
        //Set as Custom Header
        if(spendeskID!=null){
        messageLog.setStringProperty("SpendeskID", spendeskID);
        }
        
        //Read Spendesk_Name
        def spendeskName = message.getProperties().get("Spendesk_Name");
        //Set as Custom Header
        if(spendeskName!=null){
        messageLog.setStringProperty("Spendesk_Name", spendeskName);    
        }
        
        //Read Spendesk_Payable_Code
        def spendeskCode = message.getProperties().get("Spendesk_Payable_Code");        
        //Set as Custom Header
        if(spendeskCode!=null){
        messageLog.setStringProperty("Spendesk_Payable_Code", spendeskCode);    
        }
        
        //Read OrganizationBPName1
        def organization1 = message.getProperties().get("OrganizationBPName1");        
        //Set as Custom Header
        if(organization1!=null){
        messageLog.setStringProperty("OrganizationBPName1", organization1);
        }
        
        //Read OrganizationBPName2
        def organization2 = message.getProperties().get("OrganizationBPName2");        
        //Set as Custom Header
        if(organization2!=null){
        messageLog.setStringProperty("OrganizationBPName2", organization2);
        }
        
       
        
        
    }
    return message;
}