import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.io.*

/**
 * Set IDoc header properties
 */
def Message processData(Message message) {
    def body = message.getBody(java.io.Reader)
    def prop = message.getProperties();
    def xml = new XmlSlurper().parse(body)
	def messageLog = messageLogFactory.getMessageLog(message)
    
	String customtext1 = ""
	String customtext2 = ""
    String customtext3 = prop.get("header_uuid")

	//Get values from xml-body (payload)
	customtext1 = xml.WorkforcePersonMasterData.PersonUUID.text()

	
	//Set values for search in header field
	message.setHeader("customtext1", customtext1)
	message.setHeader("customtext2", "Hallo")

	
	//Set log entries
	messageLog.setStringProperty("customtext1", customtext1)
	messageLog.setStringProperty("customtext2", "Hallo Test")
	

	//Set custom header properties for message log API
	messageLog.addCustomHeaderProperty("customtext1", customtext1)
	messageLog.addCustomHeaderProperty("customtext2", "Hallo Test 2")
	messageLog.addCustomHeaderProperty("from Header", customtext3)


	return message
}


