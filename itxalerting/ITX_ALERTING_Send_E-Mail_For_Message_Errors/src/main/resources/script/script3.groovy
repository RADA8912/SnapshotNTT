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
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.time.TimeCategory;


def Message processData(Message message) {

def headers = message.getHeaders();


int scheduledMinutes = headers.get("Minutes").toInteger();

def currentDate = new Date();






MailDate = (currentDate.format("dd-MM-yyyy"));

 

LogEnd = (currentDate.format("yyyy-MM-dd'T'HH:mm:ss"));

MailEndTime = (currentDate.format("HH:mm"));



use( TimeCategory ) {

   logStart = currentDate - scheduledMinutes.minutes

}



LogStart = (logStart.format("yyyy-MM-dd'T'HH:mm:ss"));

MailStartTime = (logStart.format("HH:mm"));



message.setProperty("logStart", LogStart);

message.setProperty("logEnd", LogEnd);

message.setProperty("mailStartTime", MailStartTime);

message.setProperty("mailEndTime", MailEndTime);

message.setProperty("mailDate", MailDate);    




return message;

}