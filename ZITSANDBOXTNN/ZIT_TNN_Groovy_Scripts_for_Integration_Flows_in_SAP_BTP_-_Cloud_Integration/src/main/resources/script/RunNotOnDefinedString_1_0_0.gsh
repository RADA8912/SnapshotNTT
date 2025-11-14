import com.sap.gateway.ip.core.customdev.util.Message

/**
 * RunNotOnEmergency
 * This Groovy script is an extended start timer to stop running on emergency.
 *
 * Groovy script exchange property
 * - StartTimerExtended.RunNotOnDefinedString  = false
 *
 * Groovy script read only properties
 * - StartTimerExtended.Type = 'RunNotOnDefinedString '
 * - StartTimerExtended.Run = 'true' or 'false' use it in router condition ${property.StartTimerExtended.Run} = 'true'
 * - StartTimerExtended.Debug = Debug info
 *
 * Custom Header Properties
 * - StartTimerExtended.Run = 'true' or 'false'
 * - StartTimerExtended.Message = Message
 * - StartTimerExtended.Debug = Debug info
 *
 * @author nttdata-solutions.com
 * @version 1.0.0
 */

def Message processData(Message message) {
	final String DEFAULT_String = "<Reponse>error</Reponse>"
	
	def definedString = getExchangeProperty(message,'StartTimerExtended.DefinedString', false)

	// Get Payload
	Reader reader = message.getBody(java.io.Reader)
	Object array = []

	// Set type
	message.setProperty("StartTimerExtended.Type", "DefinedString")
	def messageLog = messageLogFactory.getMessageLog(message)

	if(definedString == null || definedString.length()==0) {
		definedString = DEFAULT_String
	}
	
	// Read Payload
	reader.eachLine { String line ->
		array.add(line)
	}
	
	boolean hasDefinedString = array.contains(definedString)
	if(!hasDefinedString){

		// Set condition for router
		message.setProperty("StartTimerExtended.Run", "true")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Run", "true")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Message", "Runnig because no defined string")
		messageLog.setStringProperty("StartTimerExtended.Run", "true")
		messageLog.setStringProperty("StartTimerExtended.Message", "Runnig because no defined string")
	}
	else{
		message.setProperty("StartTimerExtended.Run", "false")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Run", "false")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Message", "No running because of defined string")
		messageLog.setStringProperty("StartTimerExtended.Run", "false")
		messageLog.setStringProperty("StartTimerExtended.Message", "No running because of defined string")
	}

	return message
}

/**
 * getExchangeProperty
 * @param message This is message.
 * @param propertyName This is name of property.
 * @param mandatory This is parameter if property is mandatory.
 * @return propertyValue Return property value.
 */
private getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (propertyValue == null || propertyValue.length() == 0) {
			throw new Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}