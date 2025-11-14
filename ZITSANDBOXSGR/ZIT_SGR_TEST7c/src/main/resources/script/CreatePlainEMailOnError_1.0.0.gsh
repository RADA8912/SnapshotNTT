import com.sap.gateway.ip.core.customdev.util.Message

/**
* CreatePlainEMailOnError
* Creates plain E-Mail body and subject on error.
* Dynamic configuration for mail-adapter is also set.
*
* Groovy script parameters (exchange properties)
* - CreateEMail.IFlowName = IFlow name
* - CreateEMail.InterfaceID = Interface ID
*
* Groovy script read only header values
* - From = e-Mail from
* - Subject = e-Mail subject
* - Content-Type = e-Mail content-type
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final String DEFAULT_LINE_SEPARATOR = '\r\n'
	final String DEFAULT_SUBJECT = 'ERROR - Please check'
	final String DEFAULT_INFORMATION = 'there is a processing error. Please check this message.'

	final boolean CREATE_MESSAGE_ID_LINK = true
	final boolean CREATE_CORRELATION_ID_LINK = false

	// Get exception
	def map = message.getProperties()
	def ex = map.get("CamelExceptionCaught")

	// Check exception
	if (ex != null) {
		// Get exchange properties
		// Get IFlow name
		String iflowName = getExchangeProperty(message, "CreateEMail.IFlowName", false)
		if (!iflowName) {
			iflowName = ''
		}
		// Get Interface ID
		String interfaceID = getExchangeProperty(message, "CreateEMail.InterfaceID", false)
		if (!interfaceID) {
		    interfaceID = ''
		}
		// Get values
		// Get Timestamp
		String timestamp = getExchangeProperty(message, "CamelCreatedTimestamp", false)
    	if (!timestamp) {
            // Get formated date time
        	def now = new Date()
            timestamp = now.format('dd-MMM-yyyy HH:mm:ss.SSS', TimeZone.getTimeZone('UTC')).toUpperCase() + ' UTC'
    	}
		// Get Message ID
		String messageID = getExchangeProperty(message, "SAP_MessageProcessingLogID", false)
		if (!messageID) {
		    messageID = ''
		}
		// Get Correlation ID
		String correlationID = getHeader(message, "SAP_MplCorrelationId", false)
		if (!correlationID) {
		    correlationID = ''
		}

    	// Get tenant information
		String tenantName = System.env['TENANT_NAME']
		String systemID = System.env['IT_SYSTEM_ID']
		String tenantDomain = System.env['IT_TENANT_UX_DOMAIN']
		String tenantURL = "https://" + tenantName + "." + systemID + "." + tenantDomain

		// Use this if special exeption is available
		if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
			// Set property
			// Set HTTP status code
			message.setProperty("HTTP_StatusCode", ex.getStatusCode())
			// Set HTTP status text
			message.setProperty("HTTP_StatusText", ex.getStatusText())
			// Set HTTP response
			message.setProperty("HTTP_ResponseBody", ex.getResponseBody())			
		}

		// Create dynamic configuration for mail-adapter
		message.setHeader('From', tenantName)
		message.setHeader('Subject', "SAP Cloud Integration Tenant $tenantName | $iflowName - $DEFAULT_SUBJECT")
		message.setHeader('Content-Type', 'text/plain; charset=utf-8')

		// Create e-Mail body
		StringBuffer sb = new StringBuffer() // because thread safety
		sb << "Dear Sir or Madam," + DEFAULT_LINE_SEPARATOR + DEFAULT_LINE_SEPARATOR
		sb << DEFAULT_INFORMATION + DEFAULT_LINE_SEPARATOR + DEFAULT_LINE_SEPARATOR
		if (ex.getClass().getCanonicalName().equals("org.apache.camel.component.ahc.AhcOperationFailedException")) {
			sb << "HTTP Status code: " + ex.getStatusCode() + DEFAULT_LINE_SEPARATOR
			sb << "HTTP status text: " + ex.getStatusText() + DEFAULT_LINE_SEPARATOR
			sb << "HTTP response body: " + ex.getResponseBody() + DEFAULT_LINE_SEPARATOR + DEFAULT_LINE_SEPARATOR
		}
		sb << "Timestamp: $timestamp" + DEFAULT_LINE_SEPARATOR
		sb << "Message ID: $messageID"
		if (CREATE_MESSAGE_ID_LINK == true) {
			sb << "   $tenantURL/itspaces/shell/monitoring/Messages/%7B%22edge%22%3A%7B%22runtimeLocationId%22%3A%22cloudintegration%22%7D%2C%22identifier%22%3A%22$messageID%22%7D"
		}
		sb << DEFAULT_LINE_SEPARATOR
		sb << "Correlation ID: $correlationID"
		if (CREATE_CORRELATION_ID_LINK == true) {
			sb << "   $tenantURL/itspaces/shell/monitoring/Messages/%7B%22edge%22%3A%7B%22runtimeLocationId%22%3A%22cloudintegration%22%7D%2C%22identifier%22%3A%22$correlationID%22%7D"
		}
		sb << DEFAULT_LINE_SEPARATOR
		sb << "IFlow name: $iflowName" + DEFAULT_LINE_SEPARATOR
		if (interfaceID) {
			sb << "Interface ID: $interfaceID" + DEFAULT_LINE_SEPARATOR
		}
		sb << "Tenant name: $tenantName" + DEFAULT_LINE_SEPARATOR
		sb << "System ID: $systemID" + DEFAULT_LINE_SEPARATOR
		sb << DEFAULT_LINE_SEPARATOR + "Please note that this is an automatically generated e-mail from SAP Cloud Integration."

		// Set body
		message.setBody(sb.toString())
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
private def getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (!(propertyValue)) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * getHeader
 * @param message This is message.
 * @param headerName This is name of header.
 * @param mandatory This is parameter if header is mandatory.
 * @return headerValue Return header value.
 */
private def getHeader(Message message, String headerName, boolean mandatory) {
	String headerValue = message.getHeaders().get(headerName) as String
	if (mandatory) {
		if (!(headerValue)) {
			throw Exception("Mandatory header '$headerName' is missing.")
		}
	}
	return headerValue
}