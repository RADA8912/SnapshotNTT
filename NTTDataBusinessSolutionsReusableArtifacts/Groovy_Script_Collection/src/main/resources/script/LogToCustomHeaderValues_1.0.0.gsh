import com.sap.gateway.ip.core.customdev.util.Message

/**
* LogToCustomHeader
* This Groovy script logs up to 5 Exchange Properties to Custom Header Values.
* If you need less values, please set DEFAULT_PROPERTY_NAME_... and DEFAULT_CUSTOMHEADER_NAME_... to an empty value.
*
* You can set DEBUG_MODE = true to get results in simulation mode in Exchange Properties.
*
* Groovy script parameters (exchange properties)
* - LogToCustomHeader.Property1 = Name of property 1
* - LogToCustomHeader.CustomHeader1 = Name of Custom Header Value 1
* - LogToCustomHeader.Property2 = Name of property 2
* - LogToCustomHeader.CustomHeader2 = Name of Custom Header Value 2
* - LogToCustomHeader.Property3 = Name of property 3
* - LogToCustomHeader.CustomHeader3 = Name of Custom Header Value 3
* - LogToCustomHeader.Property4 = Name of property 4
* - LogToCustomHeader.CustomHeader4 = Name of Custom Header Value 4
* - LogToCustomHeader.Property5 = Name of property 5
* - LogToCustomHeader.CustomHeader5 = Name of Custom Header Value 5
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final boolean DEBUG_MODE = true

	final String DEFAULT_PROPERTY_NAME_1 = 'Property1'
	final String DEFAULT_CUSTOMHEADER_NAME_1 = 'CustomHeader1'
	final String DEFAULT_PROPERTY_NAME_2 = ''
	final String DEFAULT_CUSTOMHEADER_NAME_2 = ''
	final String DEFAULT_PROPERTY_NAME_3 = 'Property3'
	final String DEFAULT_CUSTOMHEADER_NAME_3 = 'CustomHeader3'
	final String DEFAULT_PROPERTY_NAME_4 = ''
	final String DEFAULT_CUSTOMHEADER_NAME_4 = ''
	final String DEFAULT_PROPERTY_NAME_5 = 'Property5'
	final String DEFAULT_CUSTOMHEADER_NAME_5 = 'CustomHeader5'
	
	// Get exchange properties
	// Get property 1
	propertyName1 = getExchangeProperty(message, 'LogToCustomHeader.Property1', false)
	if (!propertyName1) {
		propertyName1 = DEFAULT_PROPERTY_NAME_1
	}
	def customHeaderName1 = null
	if (propertyName1) {
		// Get property value 1
		propertyValue1 = getExchangeProperty(message, propertyName1, false)
		if (!propertyValue1) {
			propertyValue1 = ''
		}
		// Get custom header name 1
		customHeaderName1 = getExchangeProperty(message, 'LogToCustomHeader.CustomHeader1', false)
		if (!customHeaderName1) {
			customHeaderName1 = DEFAULT_CUSTOMHEADER_NAME_1
		}
	}

	// Get property 2
	propertyName2 = getExchangeProperty(message, 'LogToCustomHeader.Property2', false)
	if (!propertyName2) {
		propertyName2 = DEFAULT_PROPERTY_NAME_2
	}
	def customHeaderName2 = null
	if (propertyName2) {
		// Get property value 2
		propertyValue2 = getExchangeProperty(message, propertyName2, false)
		if (!propertyValue2) {
			propertyValue2 = ''
		}
		// Get custom header name 2
		customHeaderName2 = getExchangeProperty(message, 'LogToCustomHeader.CustomHeader2', false)
		if (!customHeaderName2) {
			customHeaderName2 = DEFAULT_CUSTOMHEADER_NAME_2
		}
	}

	// Get property 3
	propertyName3 = getExchangeProperty(message, 'LogToCustomHeader.Property3', false)
	if (!propertyName3) {
		propertyName3 = DEFAULT_PROPERTY_NAME_3
	}
	def customHeaderName3 = null
	if (propertyName3) {
		// Get property value 3
		propertyValue3 = getExchangeProperty(message, propertyName3, false)
		if (!propertyValue3) {
			propertyValue3 = ''
		}
		// Get custom header name 3
		customHeaderName3 = getExchangeProperty(message, 'LogToCustomHeader.CustomHeader3', false)
		if (!customHeaderName3) {
			customHeaderName3 = DEFAULT_CUSTOMHEADER_NAME_3
		}
	}

	// Get property 4
	propertyName4 = getExchangeProperty(message, 'LogToCustomHeader.Property4', false)
	if (!propertyName4) {
		propertyName4 = DEFAULT_PROPERTY_NAME_4
	}
	def customHeaderName4 = null
	if (propertyName4) {
		// Get property value 4
		propertyValue4 = getExchangeProperty(message, propertyName4, false)
		if (!propertyValue4) {
			propertyValue4 = ''
		}
		// Get custom header name 4
		customHeaderName4 = getExchangeProperty(message, 'LogToCustomHeader.CustomHeader4', false)
		if (!customHeaderName4) {
			customHeaderName4 = DEFAULT_CUSTOMHEADER_NAME_4
		}
	}

	// Get property 5
	propertyName5 = getExchangeProperty(message, 'LogToCustomHeader.Property5', false)
	if (!propertyName5) {
		propertyName5 = DEFAULT_PROPERTY_NAME_5
	}
	def customHeaderName5 = null
	if (propertyName5) {
		// Get property value 5
		propertyValue5 = getExchangeProperty(message, propertyName5, false)
		if (!propertyValue5) {
			propertyValue5 = ''
		}
		// Get custom header name 5
		customHeaderName5 = getExchangeProperty(message, 'LogToCustomHeader.CustomHeader5', false)
		if (!customHeaderName5) {
			customHeaderName5 = DEFAULT_CUSTOMHEADER_NAME_5
		}
	}

	// Set custom header properties
	def messageLog = messageLogFactory.getMessageLog(message)
	if (messageLog != null) {
		if(customHeaderName1) {
			messageLog.addCustomHeaderProperty(customHeaderName1, propertyValue1)
		}
		if(customHeaderName2) {
			messageLog.addCustomHeaderProperty(customHeaderName2, propertyValue2)
		}
		if(customHeaderName3) {
			messageLog.addCustomHeaderProperty(customHeaderName3, propertyValue3)
		}
		if(customHeaderName4) {
			messageLog.addCustomHeaderProperty(customHeaderName4, propertyValue4)
		}
		if(customHeaderName5) {
			messageLog.addCustomHeaderProperty(customHeaderName5, propertyValue5)
		}
	}

	// Debug Mode for simulation
	if (DEBUG_MODE == true) {
		// Set property value to debug property
		if(customHeaderName1) {
			message.setProperty('DEBUG_' + customHeaderName1, propertyValue1)
		}
		if(customHeaderName2) {
			message.setProperty('DEBUG_' + customHeaderName2, propertyValue2)
		}
		if(customHeaderName3) {
			message.setProperty('DEBUG_' + customHeaderName3, propertyValue3)
		}
		if(customHeaderName4) {
			message.setProperty('DEBUG_' + customHeaderName4, propertyValue4)
		}
		if(customHeaderName5) {
			message.setProperty('DEBUG_' + customHeaderName5, propertyValue5)
		}
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
		if (!propertyValue) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}