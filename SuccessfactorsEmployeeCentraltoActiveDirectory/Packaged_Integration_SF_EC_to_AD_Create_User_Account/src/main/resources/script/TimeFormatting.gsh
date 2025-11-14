/*
 * The integration developer needs to create the method processData 
 * This method takes Message object of package com.sap.gateway.ip.core.customdev.util
 * which includes helper methods useful for the content developer:
 * 
 * The methods available are:
    public java.lang.Object getBody()

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

def Message processData(Message message) {
	
	prop = message.getProperties();
	head = message.getHeaders();
	StringBuffer debugLog = new StringBuffer();
	String modDate="";
	Date today = new Date();
	DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	String todayInString = dateFormat.format(today);
	if(!(prop.get("LastModifiedOn_Ext").trim().isEmpty()))
	{
		modDate = prop.get("LastModifiedOn_Ext");
		message.setProperty("LastExecutionPersisted_Value",modDate);
	}
	else if(!(head.get("LastModifiedOn").trim().isEmpty()))
	{
		modDate = head.get("LastModifiedOn");
		message.setProperty("LastExecutionPersisted_Value",modDate);
	}
	else
	{
		modDate=todayInString;
		message.setProperty("LastExecutionPersisted_Value",modDate);
	}	
	if (modDate.size()>5)
   		modDate=modDate.substring(0,modDate.size()-5)+"Z";
   		
	message.setProperty("TEMP_LAST_MODIFIED_ON", modDate);
	message.setBody(debugLog.toString());
	return message;
}

