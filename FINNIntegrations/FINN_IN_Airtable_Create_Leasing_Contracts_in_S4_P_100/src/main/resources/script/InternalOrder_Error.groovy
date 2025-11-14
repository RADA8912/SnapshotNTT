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
                    							messageLog.addAttachmentAsString("Error Code", ex.getStatusCode() as String, "text/plain");
                    							messageLog.addAttachmentAsString("Error Text", ex.getStatusText(), "text/plain");
                    							messageLog.addAttachmentAsString("Error Body", ex.getResponseBody(), "text/plain");
                    

												 //Read SpendeskID
												def internalID = message.getProperties().get("InternalOrderID");        
												//Set as Custom Header
												if(internalID!=null){
												messageLog.addCustomHeaderProperty("InternalOrder ID", internalID);
												}
												
                                        											       
                                                //Read Body
                                                def body = message.getBody(java.lang.String) as String;
                                        
                                                //Set as Custom Header
                                                if(body!=null){
                                                messageLog.addAttachmentAsString("Payload", body, "text/plain");
                                                }
													

																						
								}
                }



               return message;
}

        
    }
    return message;
}