import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
                
                // get a map of iflow properties
                def map = message.getProperties();
                def fromDate = map.get("StartDate")
                def toDate = map.get("EndDate")
                def logException = map.get("ExceptionLogging")
                
                // get an exception java class instance
                def ex = map.get("CamelExceptionCaught");
                if (ex!=null) {
                 // save the error response as a message attachment 
                def messageLog = messageLogFactory.getMessageLog(message);
                def errordetails = "The Sales Order history reception from " + fromDate + " to " + toDate + " failed because of the following error:  " + ex.toString();
                def attachID  = "Error Details for Sales Order history  from " + fromDate + " to " + toDate;
                if (logException != null && logException.equalsIgnoreCase("YES")) {
                messageLog.addAttachmentAsString(attachID, errordetails, "text/plain");
                }
                }

                return message;
}