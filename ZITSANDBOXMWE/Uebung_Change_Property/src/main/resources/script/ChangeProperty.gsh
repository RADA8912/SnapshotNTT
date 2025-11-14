import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

	// Get property
	String valueIn = getExchangeProperty(message, "Input", false) as String

	// Change value
	String valueOut
	if (valueIn.length() >= 2) {
		valueOut = valueIn.substring(0,2) + "00"
	} else {
		valueOut = "0000"
	}

	// Set property
	message.setProperty("Output", valueOut)

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
		if (!propertyValue) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}