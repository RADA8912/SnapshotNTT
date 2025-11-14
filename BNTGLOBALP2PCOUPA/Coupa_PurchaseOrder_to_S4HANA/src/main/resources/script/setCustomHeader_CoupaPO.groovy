import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
	def reader = message.getBody(Reader)
    def input = new XmlSlurper().parse(reader)
	def poNumber = input.'order-header'.'po-number'.toString()
    def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog != null){
		if(poNumber != null){
			messageLog.addCustomHeaderProperty("Coupa PO Number", poNumber)	
        }
	}
	
	//Return
	return message
}