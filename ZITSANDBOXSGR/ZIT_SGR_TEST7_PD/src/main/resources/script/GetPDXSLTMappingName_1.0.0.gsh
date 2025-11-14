import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.pd.PartnerDirectoryService
import com.sap.it.api.ITApiFactory

/**
* GetPDXSLTMappingName
* Read partner ID from exchange property and use them to create binary parameter name for XSLT-mapping from CI Partner Directory.
* The value from CI Partner Directory will set to header.
* In addtional receiver address is set from parameters.
*
* Groovy script parameters (exchange properties)
* - PartnerID = Partner ID
* - PDBinaryParameterIDXSLTMapping = PD Binary Parameter ID XSLT-Mapping
*
* Groovy script read only header value
* - XSLTMappingName = XSLT-Mapping Name
*
* Groovy script read only exchange properties
* - Receiver.Address = Receiver Address
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	// Configuration
	// Exchange Property In
	final String PROPERTY_PARTNER_ID = 'PartnerID'
	final String BINARY_PARAMETER_ID = 'PDBinaryParameterIDXSLTMapping'
	// Partner Directory Parameter Names
	final String PARAMETER_RECEIVER_ADDRESS = 'ReceiverAddress'
	// Exchange Property Out
	final String PROPERTY_RECEIVER_ADDRESS = 'Receiver.Address'	
	// Header Out
	final String HEADER_MAPPING = 'XSLTMappingName'
	// Custom Header Out
	final String CUSTOM_HEADER_PARTNER_ID = 'PD Partner ID'
	final String CUSTOM_HEADER_RECEIVER_ADDRESS = 'PD Receiver Address'
	final String CUSTOM_HEADER_BINARY_PARAMETER_ID = 'PD Binary Parameter ID XSLT-Mapping'
	final String CUSTOM_HEADER_MAPPING = 'PD XSLT Mapping Name'

	// Get exchange properties
	// Get partner ID from exchange property
	def partnerID = getExchangeProperty(message, PROPERTY_PARTNER_ID, true)
	// Get XSLT Mapping Binary Parameter ID from exchange property
	def binaryParameterID = getExchangeProperty(message, BINARY_PARAMETER_ID, true)

	// Get CI Partner Directory
	def service = ITApiFactory.getApi(PartnerDirectoryService.class, null)
	if (service == null){
		throw new IllegalStateException("Partner Directory Service not found.")
	}

	// Get parameter value from CI Partner Directory
	def receiverAddress = getPDParameter(service, partnerID, PARAMETER_RECEIVER_ADDRESS, true)

	// Set property
	message.setProperty(PROPERTY_RECEIVER_ADDRESS, receiverAddress)

	// Set binary paramter
	String mappingName = "pd:$partnerID:$binaryParameterID:Binary"
	message.setHeader(HEADER_MAPPING, mappingName)

	// Set custom header properties
	def messageLog = messageLogFactory.getMessageLog(message)
	if(messageLog == null) {
	    // Do nothing
	} else {
		messageLog.addCustomHeaderProperty(CUSTOM_HEADER_PARTNER_ID, partnerID)
		messageLog.addCustomHeaderProperty(CCUSTOM_HEADER_RECEIVER_ADDRESS, receiverAddress)
		messageLog.addCustomHeaderProperty(CUSTOM_HEADER_BINARY_PARAMETER_ID, binaryParameterID)
		messageLog.addCustomHeaderProperty(CUSTOM_HEADER_MAPPING, mappingName)
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
 * getPDParameter
 * Get string value from CI Partner Directory
 * @param service Partner Directory Service.
 * @param partnerID This is partner ID.
 * @param valueOut This is value out.
 * @param parameterName This is name of parameter.
 * @param mandatory This is parameter if parameter is mandatory.
 * @return parameterValue Return parameter value.
 */
private def getPDParameter(service, String partnerID, String parameterName, boolean mandatory) {
	def valueOut = service.getParameter(parameterName, partnerID, String.class)
	if (mandatory) {
		if (valueOut == null || valueOut.length() == 0) {
			throw Exception("Mandatory value of parameter '$parameterName' for partner '$partnerID' is missing in CI Partner Directory.")
		}
	}
	return valueOut
}