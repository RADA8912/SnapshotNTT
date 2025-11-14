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
package src.main.resources.script


import com.sap.gateway.ip.core.customdev.util.Message

static void addMplHeader(messageLog, String headerName, String headerValue) {
    try {
        if (headerName && headerValue) {
            messageLog.addCustomHeaderProperty(headerName, headerValue)
        }
    } catch (Exception e) {
        // ignored
    }
}

static void addMplHeaderForProperties(message, messageLog, Map<String, String> props) {
    try {
        props.each { String headerName, String propertyName ->
            addMplHeader(messageLog, headerName, message.getProperty(propertyName))
        }
    } catch (Exception e) {
        // ignored
    }
}

def Message addOverallStatusAndStatusDetail(Message message) {
    try {
        def messageLog = messageLogFactory.getMessageLog(message)
        addMplHeaderForProperties(message, messageLog, [
                "Status": "overallStatus",
                "Status_Detail": "overallStatusDetails",
        ])
    } catch (Exception e) {
        // ignored
    }
    return message
}



