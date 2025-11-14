package com.nttdata.ndbs.btp.soap

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.util.*
import groovy.xml.QName;

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
                                                messageLog.addAttachmentAsString("http.ResponseBody", ex.getResponseBody(), "text/plain");

                                                // copy the http error response to an exchange property
                                                message.setProperty("http.ResponseBody",ex.getResponseBody());

                                                // copy the http error response to the message body
                                                message.setBody(ex.getResponseBody());

                                                // copy the value of http error code (i.e. 500) to a property
                                                message.setProperty("http.StatusCode",ex.getStatusCode());

                                                // copy the value of http error text (i.e. "Internal Server Error") to a property
                                                message.setProperty("http.StatusText",ex.getStatusText());
                                                
                                }
                }
                
                // set Content-Type to application/xml, otherwise the ERP system will not process the response properly
                message.setHeader("Content-Type", "application/xml")
                // reset http response code to 200, otherwise the ERP system will not process the response properly
                message.setHeader("CamelHttpResponseCode", 200) 
                
                
                def nodeName = new QName(message.getProperty('ITX_Namespace'), 'MT_Response', 'n0')
	            def response = new Node(null, nodeName)
	            if (ex!=null) {
    	            new Node(response,'content',ex.getResponseBody().bytes.encodeBase64().toString())
    	
                	def contentTypeHeader = new Node(response,'header')
                	new Node(contentTypeHeader,'header_key','ContentType')
                	new Node(contentTypeHeader,'header_value',message.getHeader('Content-Type', String.class))
                	def statusHeader = new Node(response,'header')
                	new Node(statusHeader,'header_key','HTTP_STATUS')
                	// Get http status from exception object
                	new Node(statusHeader,'header_value',ex.getStatusCode())
                	
                	def statusText = new Node(response,'header')
                	new Node(statusText,'header_key','HTTP_STATUS_TEXT')
                	new Node(statusText,'header_value',ex.getStatusText())
            	}
            	
            	message.setBody(groovy.xml.XmlUtil.serialize(response));

                return message;
                

}