import com.sap.gateway.ip.core.customdev.util.Message
import javax.xml.xpath.*
import javax.xml.parsers.DocumentBuilderFactory
import org.apache.commons.codec.binary.Base64

/**
* GetPayloadFromCDATA
* This Groovy script get payload from CDATA. It can decode base64, make valide XML and add XML header line.
* Values from XML-payload can read and set to Exchange Properties.
* Exchange properties 'GetPayloadFromCDATA.messageType', 'GetPayloadFromCDATA.messageStatus', 'GetPayloadFromCDATA.documentType'.
* The xPath input is case sensitive.
* Exchange property 'GetPayloadFromCDATA.isPayloadAvailable' can be used for routing an exception handling.
* File name can set to Camel File Name in Header to save CDATA-Payload as file.
*
* Groovy script parameters (exchange properties)
* - GetPayloadFromCDATA.xPathData = xPath payload data (for example '//Record/node')
* - GetPayloadFromCDATA.xPathFileName = xPath file name (for example '//Record/node')
* - GetPayloadFromCDATA.setCamelFileName = set Camel file name to message header ('true', 'yes', 'false', 'no')
* - GetPayloadFromCDATA.xPathMessageID = xPath message id from message payload (for example '//Record/node')
* - GetPayloadFromCDATA.xPathMessageType = xPath message type from message payload (for example '//Record/node')
* - GetPayloadFromCDATA.xPathMessageStatus = xPath message status from message payload (for example '//Record/node')
* - GetPayloadFromCDATA.decodeBase64 = Decode base64 payload ('true', 'yes', 'false', 'no')
* - GetPayloadFromCDATA.addXMLHeader = Add XML header to XML payload if not available ('true', 'yes', 'false', 'no')
* - GetPayloadFromCDATA.lineSeparator = Line separator ('\n', '\r\n' or empty value) to use in add XML header
* - GetPayloadFromCDATA.xPathDocumentNumber = xPath document number from CDATA payload (for example '//Record/node')
* - GetPayloadFromCDATA.xPathDocumentType = xPath document type from CDATA payload (for example '//Record/node')
* 
* Groovy script read only exchange properties
* - GetPayloadFromCDATA.isPayloadAvailable = Is payload available payload ('true', 'false')
* - GetPayloadFromCDATA.fileName = File name from XML-Payload
* - GetPayloadFromCDATA.messageID = Message ID from XML-Payload
* - GetPayloadFromCDATA.messageType	= Message type from XML-Payload
* - GetPayloadFromCDATA.messageStatus = Message status from XML-Payload
* - GetPayloadFromCDATA.documentNumber = Document number from CDATA-Payload
* - GetPayloadFromCDATA.documentType = Document type from CDATA-Payload
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final boolean DEFAULT_DECODE_BASE64 = false
	final boolean DEFAULT_ADD_XML_HEADER = false
	final String DEFAULT_LINE_SEPARATOR = '\r\n'
	final boolean DEFAULT_SET_CAMEL_FILE_NAME = false

	// Save results to these exchange properties
	final String EXCHANGE_PROPERTY_IS_PAYLOAD_AVAILABLE = 'GetPayloadFromCDATA.isPayloadAvailable'
	final String EXCHANGE_PROPERTY_FILE_NAME = 'GetPayloadFromCDATA.fileName'
	final String EXCHANGE_PROPERTY_MESSAGE_ID = 'GetPayloadFromCDATA.messageID'
	final String EXCHANGE_PROPERTY_MESSAGE_TYPE = 'GetPayloadFromCDATA.messageType'	
	final String EXCHANGE_PROPERTY_MESSAGE_STATUS = 'GetPayloadFromCDATA.messageStatus'	
	final String EXCHANGE_PROPERTY_DOCUMENT_NUMBER = 'GetPayloadFromCDATA.documentNumber'
	final String EXCHANGE_PROPERTY_DOCUMENT_TYPE = 'GetPayloadFromCDATA.documentType'	

	// Get exchange properties
	xPathDataQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathData', true)
	xPathFileNameQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathFileName', false)
	xPathDataMessageIDQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathMessageID', false)
	xPathDataMessageTypeQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathMessageType', false)
	xPathDataMessageStatusQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathMessageStatus', false)
	xPathDataDocumentNumberQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathDocumentNumber', false)
	xPathDataDocumentTypeQuery = getExchangeProperty(message, 'GetPayloadFromCDATA.xPathDocumentType', false)

	decodeBase64Property = getExchangeProperty(message, 'GetPayloadFromCDATA.decodeBase64', false)
	boolean decodeBase64 = false
	if (decodeBase64Property == null || decodeBase64Property.length() == 0) {
		decodeBase64 = DEFAULT_DECODE_BASE64
	} else if ('true'.equalsIgnoreCase(decodeBase64Property) || 'yes'.equalsIgnoreCase(decodeBase64Property)) {
		decodeBase64 = true
	}

	addXMLHeaderProperty = getExchangeProperty(message, 'GetPayloadFromCDATA.addXMLHeader', false)
	boolean addXMLHeader = false
	if (addXMLHeaderProperty == null || addXMLHeaderProperty.length() == 0) {
		addXMLHeader = DEFAULT_DECODE_BASE64
	} else if ('true'.equalsIgnoreCase(addXMLHeaderProperty) || 'yes'.equalsIgnoreCase(addXMLHeaderProperty)) {
		addXMLHeader = true
	}

	lineSeparator = getExchangeProperty(message, 'GetPayloadFromCDATA.lineSeparator', false)
	if (lineSeparator == null) {
		lineSeparator = DEFAULT_LINE_SEPARATOR
	} else {
		if ('\\n'.equalsIgnoreCase(lineSeparator) != true && '\\r\\n'.equalsIgnoreCase(lineSeparator) != true && lineSeparator.length() != 0) {
			throw Exception("Exchange property 'GetPayloadFromCDATA.lineSeparator' allows only '\\n', '\\r\\n' or empty value.")
		}
		lineSeparator = createNewLine(lineSeparator)
	}

	setCamelFileNameProperty = getExchangeProperty(message, 'GetPayloadFromCDATA.setCamelFileName', false)
	boolean setCamelFileName = false
	if (setCamelFileNameProperty == null || setCamelFileNameProperty.length() == 0) {
		setCamelFileName = DEFAULT_SET_CAMEL_FILE_NAME
	} else if ('true'.equalsIgnoreCase(setCamelFileNameProperty) || 'yes'.equalsIgnoreCase(setCamelFileNameProperty)) {
		setCamelFileName = true
	}

	// Get body
	def body = message.getBody(java.lang.String) as String

	if (body.length() > 0) {
		// Set exchange properties (source is XML-payload)
		setXPathValueToExchangeProperty(message, body, xPathDataMessageIDQuery, EXCHANGE_PROPERTY_MESSAGE_ID)
		setXPathValueToExchangeProperty(message, body, xPathDataMessageTypeQuery, EXCHANGE_PROPERTY_MESSAGE_TYPE)
		setXPathValueToExchangeProperty(message, body, xPathDataMessageStatusQuery, EXCHANGE_PROPERTY_MESSAGE_STATUS)
		String fileName = setXPathValueToExchangeProperty(message, body, xPathFileNameQuery, EXCHANGE_PROPERTY_FILE_NAME)

		// Set file name dynamic to header for adapter
		if (setCamelFileName == true) {
			if (fileName.length() > 0) {
				message.setHeader('CamelFileName', fileName)
			}
		}

		// Get value from CDATA node. CDATA will be remove automaticly
		String nodeValue = processXml(body, xPathDataQuery)

		// Decode base64
		if (nodeValue.length() > 0) {
			if (decodeBase64 == true) {
				if (Base64.isBase64(nodeValue)) {
					byte[] decoded = nodeValue.decodeBase64()
					nodeValue = new String(decoded)
				}
			}
		}
		body = nodeValue

		// Check payload availability and set exchange property
		if (body.length() == 0) {
			message.setProperty(EXCHANGE_PROPERTY_IS_PAYLOAD_AVAILABLE,'false')
		} else {
			message.setProperty(EXCHANGE_PROPERTY_IS_PAYLOAD_AVAILABLE,'true')

			// Unescapes XML
			if ('&lt;'.equalsIgnoreCase(body.substring(0,4))) {
				body = body.replaceAll('&lt;','<')
					.replaceAll('&gt;','>')
					.replaceAll('&quot;', '"')
					.replaceAll('&apos;', "'")
					.replaceAll('&amp;', '&')
					.replaceAll('&#60;','<')
					.replaceAll('&#62;','>')
					.replaceAll('&#34;', '"')
					.replaceAll('&#39;', "'")
					.replaceAll('&#38;', '&')
			}

			// Add XML header line
			if (addXMLHeader == true) {
				if ('<?'.equalsIgnoreCase(body.substring(0,2)) == false) {
					body = '<?xml version="1.0" encoding="utf-8"?>' + lineSeparator + body
				}
			}
			
			// Set exchange properties (source is CDATA-payload)
			setXPathValueToExchangeProperty(message, body, xPathDataDocumentNumberQuery, EXCHANGE_PROPERTY_DOCUMENT_NUMBER)
			setXPathValueToExchangeProperty(message, body, xPathDataDocumentTypeQuery, EXCHANGE_PROPERTY_DOCUMENT_TYPE)
		}

		message.setBody(body)
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
 * createNewLine
 * @param value This is value.
 * @return value Return value.
 */
private createNewLine(String value) {
	value.replace("\\n", "\n")
		.replace("\\r", "\r")
}

/**
 * processXml
 * @param xml This is xml-payload.
 * @param xpathQuery This is xpath query.
 * @return nodeValue Return node value.
 */
private processXml(String xml, String xpathQuery) {
	def xpath = XPathFactory.newInstance().newXPath()
	def builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
	def inputStream = new ByteArrayInputStream(xml.bytes)
	def records = builder.parse(inputStream).documentElement
	String nodeValue = xpath.evaluate(xpathQuery, records)
	return nodeValue
}

/**
 * setXPathValueToExchangeProperty
 * @param message This is message.
 * @param body This is body.
 * @param xPathQuery This is the xPath query.
 * @param propertyName This is property name.
 * @return nodeValue Return node value.
 */
private setXPathValueToExchangeProperty(Message message, String body, String xPathQuery, String propertyName) {
	String nodeValue = ''
	if (body.length() > 0) {
		if (xPathQuery != null && xPathQuery.length() > 0) {
			// Get value from node
			nodeValue = processXml(body, xPathQuery)
			
			//Set exchange property
			message.setProperty(propertyName,nodeValue)
		}
	}
	return nodeValue
}