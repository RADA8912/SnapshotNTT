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
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
def Message processData(Message message) {
    
    def cetTimeZone = TimeZone.getTimeZone("Europe/Berlin"); // get CET timezone
    def calendar = Calendar.getInstance(cetTimeZone); // get current time in CET timezone
    
    calendar.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
    calendar.set(Calendar.MINUTE, 0); // set minute to 0
    calendar.set(Calendar.SECOND, 0); // set second to 0
    calendar.set(Calendar.MILLISECOND, 0); // set millisecond to 0
    

    def to_ts=calendar.getTimeInMillis() / 1000 - 1;
    calendar.add(Calendar.DATE, -1);
    def from_ts=calendar.getTimeInMillis() / 1000; // set to previous day
    
       message.setProperty("from_ts", from_ts);
       message.setProperty("to_ts", to_ts);
       message.setHeader("x-hasura-admin-secret", "P4xMydyHXojEd53GVfBi0ZVrCKsL5qexlJq55dWwUjoeqhfOzg87pJDhHsK4sneO");
       message.setHeader("content-type", "application/json");
       message.setProperty("Offset", 0);
       message.setProperty("Finished", 'False');
       message.setProperty("fullPayload", "");

       return message;
}