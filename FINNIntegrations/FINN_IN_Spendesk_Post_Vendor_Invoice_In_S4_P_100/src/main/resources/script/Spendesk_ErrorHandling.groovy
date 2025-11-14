import com.sap.gateway.ip.core.customdev.util.Message;



def Message processData(Message message) {
                
                // get a map of properties
                def map = message.getProperties();
                
                // get an exception java class instance
                def ex = map.get("CamelExceptionCaught");
                if (ex!=null) {
                                
                                // an http adapter throws an instance of org.apache.camel.component.ahc.AhcOperationFailedException
                                if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
                                                
                                                // save the http error response as a message attachment
                                                def messageLog = messageLogFactory.getMessageLog(message);
												messageLog.addAttachmentAsString("Error Code", ex.getStatusCode() as String, "text/plain")
												messageLog.addAttachmentAsString("Error Text", ex.getStatusText(), "text/plain")
												messageLog.addAttachmentAsString("Error Body", ex.getResponseBody(), "text/plain");

												 //Read SpendeskID
												def spendeskID = message.getProperties().get("SpendeskID");        
												//Set as Custom Header
												if(spendeskID!=null){
												messageLog.addCustomHeaderProperty("SpendeskID", spendeskID);
												}
												
												//Read Spendesk_Name
												def spendeskName = message.getProperties().get("Spendesk_Name");
												//Set as Custom Header
												if(spendeskName!=null){
												messageLog.addCustomHeaderProperty("Spendesk_Name", spendeskName);    
												}
												
												//Read Spendesk_Payable_Code
												def spendeskCode = message.getProperties().get("Spendesk_Payable_Code");        
												//Set as Custom Header
												if(spendeskCode!=null){
												messageLog.addCustomHeaderProperty("Spendesk_Payable_Code", spendeskCode);    
												}
												
												//Read OrganizationBPName1
												def organization1 = message.getProperties().get("OrganizationBPName1");        
												//Set as Custom Header
												if(organization1!=null){
												messageLog.addCustomHeaderProperty("OrganizationBPName1", organization1);
												}
												
												//Read OrganizationBPName2
												def organization2 = message.getProperties().get("OrganizationBPName2");        
												//Set as Custom Header
												if(organization2!=null){
												messageLog.addCustomHeaderProperty("OrganizationBPName2", organization2);
												}
												
											   
													

																						
								}
                }



               return message;
}

