/*
 The integration developer needs to create the method processData 
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
       public void clearSoapHeaders()
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body 
        latestDate = new Date();
        //set to run 4:30am
        //+1 second to be in the new day -> 00:00:01
        long from_ts = (latestDate.getTime() / 1000) - (28.5 * 3600);
        //-1 seconds to be in the day range -> 23:59:59
        long to_ts = (latestDate.getTime() / 1000) - (4.5 * 3600) - 1;
        message.setProperty("from_ts", from_ts);
        message.setProperty("to_ts", to_ts);
       message.setHeader("x-hasura-admin-secret", "P4xMydyHXojEd53GVfBi0ZVrCKsL5qexlJq55dWwUjoeqhfOzg87pJDhHsK4sneO");
       message.setHeader("content-type", "application/json");
       message.setProperty("Offset", 0);
       message.setProperty("Finished", 'False');
       message.setProperty("fullPayload", "");
       message.setProperty("limit", 300);

       return message;
}