import com.sap.gateway.ip.core.customdev.util.Message

/**
* LogPayload
* This Groovy script logs payload as MPL attachment. Script Function can use to log payload in different steps.
* If parameter is not used then on log level 'ERROR', 'DEBUG' and 'TRACE' logging of payload will be activated per default.
*
* Groovy script parameters:
* - LogPayload.Activate = activate / deactivate logging ('true', 'false', 'yes', 'no')
*
* Script Function for processing steps:
* - empty -> Default only name 'Payload'
* - logPayloadIn -> Payload in
* - logPayloadXMLIn -> Payload XML in
* - logPayloadJSONIn -> Payload JSON in
* - logPayloadIDocIn -> Payload IDoc in
* - logPayloadCSVIn -> Payload CSV in
* - logPayloadPlainIn -> Payload Plain in
* - logPayloadEDIIn -> Payload EDI in
* - logPayloadIDocTypeIn -> Payload IDoc-type from IDoc-XML payload in
* - logPayloadIDocType -> Payload IDoc-type from IDoc-XML payload
* - logPayloadBI -> BI = After sender adapter processing, but before inbound schema validation
* - logPayloadVI -> VI = Before scenario look-up step
* - logPayloadMS -> MS = Before mapping step
* - logPayloadAM -> AM = After mapping step, but before outbound schema validation
* - logPayloadVO -> VO = After outbound schema validation
* - logPayloadActivityID -> Payload with Activity ID
* - logPayload1 -> Payload 1
* - logPayload2 -> Payload 2
* - logPayload3 -> Payload 3
* - logPayload4 -> Payload 4
* - logPayload5 -> Payload 5
* - logPayloadRequest -> Payload request 
* - logPayloadResponse -> Payload response
* - logPayloadOut -> Payload out
* - logPayloadXMLOut -> Payload XML out
* - logPayloadJSONOut -> Payload JSON out
* - logPayloadIDocOut -> Payload IDoc out
* - logPayloadCSVOut -> Payload CSV out
* - logPayloadPlainOut -> Payload Plain out
* - logPayloadEDIOut -> Payload EDI out
* - logPayloadIDocTypeOut -> Payload IDoc-type from IDoc-XML payload out
* - logPayloadException -> Payload exception
* - logPayloadException1 -> Payload exception 1
* - logPayloadException2 -> Payload exception 2
* - logPayloadException3 -> Payload exception 3
* - logPayloadException4 -> Payload exception 4
* - logPayloadException5 -> Payload exception 5
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Default
	logPayload(message, 'Payload', null)
}

/**
* Step: Payload in
*/
def Message logPayloadIn(Message message) {
	logPayload(message, 'Payload in', null)
}

/**
* Step: Payload XML in
*/
def Message logPayloadXMLIn(Message message) {
	logPayload(message, 'Payload XML in', null)
}

/**
* Step: Payload JSON in
*/
def Message logPayloadJSONIn(Message message) {
	logPayload(message, 'Payload JSON in', null)
}

/**
* Step: Payload IDoc in
*/
def Message logPayloadIDocIn(Message message) {
	logPayload(message, 'Payload IDoc in', null)
}

/**
* Step: Payload CSV in
*/
def Message logPayloadCSVIn(Message message) {
	logPayload(message, 'Payload CSV in', null)
}

/**
* Step: Payload Plain in
*/
def Message logPayloadPlainIn(Message message) {
	logPayload(message, 'Payload Plain in', null)
}

/**
* Step: Payload EDI in
*/
def Message logPayloadEDIIn(Message message) {
	logPayload(message, 'Payload EDI in', null)
}

/**
* Step: Payload IDoc-type from IDoc-XML payload in
*/
def Message logPayloadIDocTypeIn(Message message) {
	logPayload(message, 'IDoc Type in', null)
}

/**
* Step: Payload IDoc-type from IDoc-XML payload
*/
def Message logPayloadIDocType(Message message) {
	logPayload(message, 'IDoc Type', null)
}

/**
* Step: BI = After sender adapter processing, but before inbound schema validation
*/
def Message logPayloadBI(Message message) {
	logPayload(message, 'Payload BI', null)
}

/**
* Step: VI = Before scenario look-up step
*/
def Message logPayloadVI(Message message) {
	logPayload(message, 'Payload VI', null)
}

/**
* Step: MS = Before mapping step
*/
def Message logPayloadMS(Message message) {
	logPayload(message, 'Payload MS', null)
}

/**
* Step: AM = Before mapping step
*/
def Message logPayloadAM(Message message) {
	logPayload(message, 'Payload AM', null)
}

/**
* Step: VO = After outbound schema validation
*/
def Message logPayloadVO(Message message) {
	logPayload(message, 'Payload VO', null)
}

/**
* Step: Activity ID
*/
def Message logPayloadActivityID(Message message) {
	logPayload(message, 'Activity ID', null)
}

/**
* Step: Payload 1
*/
def Message logPayload1(Message message) {
	logPayload(message, 'Payload 1', null)
}

/**
* Step: Payload 2
*/
def Message logPayload2(Message message) {
	logPayload(message, 'Payload 2', null)
}

/**
* Step: Payload 3
*/
def Message logPayload3(Message message) {
	logPayload(message, 'Payload 3', null)
}

/**
* Step: Payload 4
*/
def Message logPayload4(Message message) {
	logPayload(message, 'Payload 4', null)
}

/**
* Step: Payload 5
*/
def Message logPayload5(Message message) {
	logPayload(message, 'Payload 5', null)
}

/**
* Step: Payload request
*/
def Message logPayloadRequest(Message message) {
	logPayload(message, 'Payload Request', null)
}

/**
* Step: Payload response
*/
def Message logPayloadResponse(Message message) {
	logPayload(message, 'Payload Response', null)
}

/**
* Step: Payload out
*/
def Message logPayloadOut(Message message) {
	logPayload(message, 'Payload out', null)
}

/**
* Step: Payload XML out
*/
def Message logPayloadXMLOut(Message message) {
	logPayload(message, 'Payload XML out', null)
}

/**
* Step: Payload JSON out
*/
def Message logPayloadJSONOut(Message message) {
	logPayload(message, 'Payload JSON out', null)
}

/**
* Step: Payload IDoc out
*/
def Message logPayloadIDocOut(Message message) {
	logPayload(message, 'Payload IDoc out', null)
}

/**
* Step: Payload CSV out
*/
def Message logPayloadCSVOut(Message message) {
	logPayload(message, 'Payload CSV out', null)
}

/**
* Step: Payload Plain out
*/
def Message logPayloadPlainOut(Message message) {
	logPayload(message, 'Payload Plain out', null)
}

/**
* Step: Payload EDI out
*/
def Message logPayloadEDIOut(Message message) {
	logPayload(message, 'Payload EDI out', null)
}

/**
* Step: Payload IDoc-type from IDoc-XML payload out
*/
def Message logPayloadIDocTypeOut(Message message) {
	logPayload(message, 'IDoc Type out', null)
}

/**
* Step: Payload exception
*/
def Message logPayloadException(Message message) {
	logPayload(message, 'Payload Exception', true)
}

/**
* Step: Payload exception 1
*/
def Message logPayloadException1(Message message) {
	logPayload(message, 'Payload Exception 1', true)
}

/**
* Step: Payload exception 2
*/
def Message logPayloadException2(Message message) {
	logPayload(message, 'Payload Exception 2', true)
}

/**
* Step: Payload exception 3
*/
def Message logPayloadException3(Message message) {
	logPayload(message, 'Payload Exception 3', true)
}

/**
* Step: Payload exception 4
*/
def Message logPayloadException4(Message message) {
	logPayload(message, 'Payload Exception 4', true)
}

/**
* Step: Payload exception 5
*/
def Message logPayloadException5(Message message) {
	logPayload(message, 'Payload Exception 5', true)
}

/**
 * Log payload as attachment.
 * @param message Message.
 * @param name Name.
 * @param activate Activate (null = default computing, true = always activated, false = deactivated).
 * @return message Message.
 */
private def logPayload(Message message, String name, Boolean activate) {
	final Boolean DEFAULT_ACTIVATE_LOG = false
	final String DEFAULT_NAME = 'Payload'
	final String ATTENTION_EMPTY = 'ATTENTION: Payload is empty. (This was automatically created from Groovy Script.)' // If legth = 0 then no payload will created

	// Get exchange properties
	def activateLog = getExchangeProperty(message, 'LogPayload.Activate', false)
	def logLevel = getExchangeProperty(message, 'SAP_MPL_LogLevel_Internal', false)

	// Activation computing
	if (activate != null && activate) {
		// use input 'activate' from methode
	} else if (activateLog != null ) {
		// Check exchange property
		if ('true'.equalsIgnoreCase(activateLog) || 'yes'.equalsIgnoreCase(activateLog)) {
			activate = true
		} else {
			activate = false
		}
	} else if (logLevel != null ) {
		if ('error'.equalsIgnoreCase(logLevel) || 'debug'.equalsIgnoreCase(logLevel) || 'trace'.equalsIgnoreCase(logLevel)) {
			// use exchange log level
			activate = true
		} else {
			activate = DEFAULT_ACTIVATE_LOG
		}
	} else {
		activate = DEFAULT_ACTIVATE_LOG
	}

	// Log payload only if logging is activated
	if (activate == true) {
		// Get body
		def body = message.getBody(java.lang.String) as String

		// Set attachment name based on IDoc type
		if ('idoc type'.equalsIgnoreCase(name) || 'idoc type in'.equalsIgnoreCase(name) || 'idoc type out'.equalsIgnoreCase(name)) {
			String nameIn = name
			String iDocType = getIDocType(body)
			if (iDocType != null && iDocType.length() != 0) {
				name = iDocType
			} else {
				name = DEFAULT_NAME
			}
			// Add 'in' or 'out'
			if ('idoc type in'.equalsIgnoreCase(nameIn)) {
				name += ' in'
			} else if ('idoc type out'.equalsIgnoreCase(nameIn)) {
				name += ' out'
			}
		} else if ('activity id'.equalsIgnoreCase(name)) {
			String activityID = getActivityID(message)
			if (activityID.length() > 0) {
				name = 'Payload ' + activityID
			} else {
				name = DEFAULT_NAME
			}
		}

		// Log payload
		def messageLog = messageLogFactory.getMessageLog(message)
		if(messageLog != null) {
			if (body == null || body.length() == 0) {
				body = ATTENTION_EMPTY
			}
			messageLog.addAttachmentAsString(name, body, 'text/plain')
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
		if (propertyValue == null || propertyValue.length() == 0) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * getIDocType
 * @param message This is message.
 * @return getIDocType Return IDoc Type.
 */
private String getIDocType(String body) {
	String iDocType = ''
	
	if (body.length() > 0) {
		try {
			// Get values from IDoc-XML
			def idoc = new XmlSlurper(false, false).parseText(body)
			String mestyp = idoc.IDOC.EDI_DC40.MESTYP
			String idoctyp = idoc.IDOC.EDI_DC40.IDOCTYP
			String cimtyp = idoc.IDOC.EDI_DC40.CIMTYP
			// Compute iDocType
			if (mestyp.length() > 0 && idoctyp.length() > 0) {
				iDocType = 'IDoc ' + mestyp + '_' + idoctyp
				if (cimtyp.length() > 0) {
					iDocType += '_' + cimtyp
				}
			}
		} catch (Exception e) {
			iDocType = ''
		}
	}
	return iDocType
}

/**
 * getActivityID
 * @param message This is message.
 * @return getActivityID Return Activity ID.
 */
private String getActivityID(Message message) {
	String messageHistory = message.properties.get('CamelMessageHistory') as String
	String lastActivity = messageHistory.substring(messageHistory.lastIndexOf('CallActivity'), messageHistory.length())
	String activityIDComplete = lastActivity.substring(0, lastActivity.indexOf(']'))
	String activityID = activityIDComplete.substring(0, activityIDComplete.lastIndexOf('_'))
	return activityID
}