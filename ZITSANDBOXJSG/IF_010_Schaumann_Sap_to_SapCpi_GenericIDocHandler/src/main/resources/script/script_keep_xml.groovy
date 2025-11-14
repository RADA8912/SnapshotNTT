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
import groovy.xml.*;
import java.util.regex.*;
import java.util.HashMap;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String;
    //def body = message.getBody(java.io.Reader)
    // Get LogLevel of the artifact
    def map = message.getProperties();
	def logConfig = map.get("SAP_MessageProcessingLogConfiguration");
	def logLevel = (String) logConfig.logLevel;
	
    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null){
        // Only log when LogLevel of iFlow == Debug || Trace
        if(logLevel.equals("DEBUG") || logLevel.equals("TRACE")) {
            def bodyNice = XmlUtil.serialize(body); // Make XML fancy
            messageLog.setStringProperty("Logging#3", "Printing Payload As Attachment");
            messageLog.addAttachmentAsString("3. Outgoing", bodyNice , "text/plain");
        } // Here it would be possible to add logging alternatives in case of other log levels
    }

    return message;
}