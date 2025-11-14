import com.sap.gateway.ip.core.customdev.util.Message;

def Message processData(Message message) 
{
	def messageLog = messageLogFactory.getMessageLog(message);
	if(messageLog != null)
	{
		def idocNo = message.getHeaders().get("idocNo");
		if(idocNo != null)
		{
			messageLog.addCustomHeaderProperty("IDoc-Nummer", idocNo);
		}
	}
	return message;
}