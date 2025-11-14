/* The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
	public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
    public void setProperty(java.lang.String name, java.lang.Object value)
    public java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> getSoapHeaders()
    public void setSoapHeaders(java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> soapHeaders) 
    public void clearSoapHeaders() */ 
    import com.sap.gateway.ip.core.customdev.util.Message;
    import java.util.HashMap;
    import com.sap.it.api.pd.PartnerDirectoryService;
    import com.sap.it.api.ITApiFactory;

def Message processData(Message message) {

    def service = ITApiFactory.getApi(PartnerDirectoryService.class, null); 
    if (service == null){
        throw new IllegalStateException("Partner Directory Service not found");
    }       
    
    // PD Partner ID
    def properties = message.getProperties(); 
    def Pid = properties.get("pid");
    if (Pid == null){
        throw new IllegalStateException("Partner ID not found in sent message");   
    }
    
    // Partner URL
    def receiverUrl = service.getParameter("ReceiverUrl", Pid , String.class);
    if (receiverUrl == null){
        throw new IllegalStateException("Partner ID " + Pid + " not found or ReceiverUrl parameter not found in the Partner Directory for the partner ID " + Pid);      
    }
    message.setProperty("receiverUrl", receiverUrl);    
    
    // This expression can be used also directly in receiver channel or in content modifier "pd:${property.pid}:${property.credentialsName}:UserCredential"
    message.setProperty("credentialAlias", "pd:" + Pid + ":ReceiverCredentials:UserCredential");

    return message;
}


