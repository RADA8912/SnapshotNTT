// Generic Script to log exception message and attach source body in case of exception

import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    //Fetch source body stored in property and logging as attachment for further debugging
    def map  = message.getProperties()
    def sourceBody = map.get("sourceJSON")

    def messageLog = messageLogFactory.getMessageLog(message);
    if(messageLog != null)
    {
        messageLog.setStringProperty("SourcePayload","Printing Payload As Attachment")
        messageLog.addAttachmentAsString("SourcePayload",sourceBody,"text/plain");
    }
    
	
	//Capturing exceptions and logging in attachment
    def ex   = map.get("CamelExceptionCaught");
       if (ex != null)
       {
           exceptionText    = ex.getMessage();
           messageLog.addAttachmentAsString("Exception", exceptionText,"application/text");
       }


    return message
}