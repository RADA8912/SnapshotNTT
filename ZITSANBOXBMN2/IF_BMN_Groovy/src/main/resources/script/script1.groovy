import com.sap.gateway.ip.core.customdev.util.Message
/**
* Get the MovmentTyp from CSV file
*/
def Message processData(Message message) {
	def body = message.getBody(java.lang.String) as String
	
	def MovmentTyp = body.substring(0,2)
	
	 message.setProperty("MovmentTyp", MovmentTyp);
	return message
}