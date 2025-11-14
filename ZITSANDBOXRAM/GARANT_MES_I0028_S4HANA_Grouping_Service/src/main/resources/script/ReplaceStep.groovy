import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;


	
	def replaceQuotes(inputString) {
    
     def resultString = inputString.replaceAll('"', '\\\\"')
    return resultString
    }
    
    def Message processData(Message message) {	
	
	def body = message.getBody(java.lang.String) as String
    
    def modifiedString = replaceQuotes(body)
    

		
		message.setBody(modifiedString)
	

	return message
}