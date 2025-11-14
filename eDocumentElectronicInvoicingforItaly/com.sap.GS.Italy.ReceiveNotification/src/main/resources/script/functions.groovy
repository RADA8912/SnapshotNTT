/*
 * The integration developer needs to create the method processData 
 * This method takes Message object of package com.sap.gateway.ip.core.customdev.util
 * which includes helper methods useful for the content developer:
 * 
 * The methods available are:
    public java.lang.Object getBody()
    
    //This method helps User to retrieve message body as specific type ( InputStream , String , byte[] ) - e.g. message.getBody(java.io.InputStream)
    public java.lang.Object getBody(java.lang.String fullyQualifiedClassName)

    public void setBody(java.lang.Object exchangeBody)

    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()

    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)

    public void setHeader(java.lang.String name, java.lang.Object value)

    public java.util.Map<java.lang.String,java.lang.Object> getProperties()

    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 

	public void setProperty(java.lang.String name, java.lang.Object value)
 * 
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

def Message buildDatastoreName(Message message) {
	
	//Headers 
	headers = message.getHeaders();
	dataStoreName = headers.get("DataStoreName");
	
	//Properties  
	props = message.getProperties();
	fileName = props.get("NomeFile");
	
	String[] parts = fileName.trim().split("_");
	fiscalCode = parts[0].substring(2);

	message.setHeader("DataStoreName", dataStoreName + fiscalCode);
	
	return message;
}

def Message getCurrentDate(Message message){
    
     format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
     timeZone = "CET";
     Date date = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat(format);
     formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
     synchronized (date) {
          message.setProperty("Timestamp",formatter.format(date));
     }   
     return message;
}