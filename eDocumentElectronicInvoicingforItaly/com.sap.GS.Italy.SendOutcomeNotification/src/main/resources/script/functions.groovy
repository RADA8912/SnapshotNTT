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
import java.util.Iterator;
import javax.activation.DataHandler;

def Message getSOAPAttachment(Message message) {
	
	def byte[] body_bytes = null;           
    def attachments = message.getAttachments();
    
if ((attachments!=null)&&(!attachments.isEmpty())) { 
  	Iterator it = attachments.values().iterator();
	DataHandler attachment = it.next();
	message.setProperty("AttachmentBody", attachment.getContent());    
}
	return message;
}

def Message getIdentificativoSdI(Message message) {
  
	props = message.getProperties();
	idSdIAccept = props.get("IdentificativoAccept");
	idSdIReject = props.get("IdentificativoReject");
	
	if (idSdIAccept != '') {
	  message.setProperty("IdentificativoSdI", idSdIAccept);        
	} else if (idSdIReject != '') {
	  message.setProperty("IdentificativoSdI", idSdIReject);                              
	}

	return message;
}

