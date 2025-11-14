import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) {
    def body = message.getBody(java.lang.String) as String
	def messageLog = messageLogFactory.getMessageLog(message);
	def CustomerID = message.getHeaders().get("CustomerID");
	def OrderID = message.getProperties().get("OrderNumber");
	
        if(messageLog != null){
                messageLog.addCustomHeaderProperty("CustomerID", CustomerID);
                messageLog.addCustomHeaderProperty("OrderID", OrderID);  
        }
        return message;
}