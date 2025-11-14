import com.sap.gateway.ip.core.customdev.util.Message

/**
* GetExcelMappingStructureFromXML
* This Groovy script gets Excel mapping structure from XML.
*
* Groovy script parameters (exchange properties)
* - GetExcelMappingStructureFromXML.XMLType = XML type ('Target' or 'Source')
* - GetExcelMappingStructureFromXML.AddXMLValue = Add the XML value ('true' or 'false')
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
Message processData(Message message) {
	final String XML_TYPE = 'Target'
	final boolean ADD_XML_VALUE = false
	final String XPATH_SEPARATOR = '/'
	final String COLUMN_SEPARATOR = '\t'
	final String LINE_BREAK = '\r\n'
	final String ENCLOSURE = ''

	boolean addSourceParameter = false
	String xPath = ''

	// Get exchange properties
	// XML type
	xmlType = getExchangeProperty(message, "GetExcelMappingStructureFromXML.XMLType", false)
	if (!xmlType) {
		xmlType = XML_TYPE
	}
	// Check XML type
	if (!'Target'.equals(xmlType) && !'Source'.equals(xmlType)) {
		throw Exception("XML type can only be 'Target' or 'Source'.")
	}

	// Add source parameter
	addXMLValuerIn = getExchangeProperty(message, "GetExcelMappingStructureFromXML.AddXMLValue", false)
	if (addXMLValuerIn && 'true'.equalsIgnoreCase(addXMLValuerIn)) {
		addXMLValuer = true
	} else if (!addXMLValuerIn) {
		addXMLValuer = ADD_XML_VALUE
	}

	if (message.getBodySize() > 0) {
		// Get body
		Reader reader = message.getBody(Reader)
		Node root = new XmlParser().parse(reader)

		// Create String Builder
		StringBuilder sb = new StringBuilder()

		//Create XML structure
		def createExcelMappingSheet
		createExcelMappingSheet = { Node node ->
			node.children().each { child ->
				switch (child){
					case Node:
						def grandchildren = child.children()
						boolean newSegment = true
						if (grandchildren.size() == 0) {
							newSegment = false
						}
						else {
							for (grandchild in grandchildren) {
								switch (grandchild){
									case String:
										newSegment = false
										break
								}
							}
						}
						// Append segment
						if (newSegment) {
							sb.append(COLUMN_SEPARATOR + XPATH_SEPARATOR + createPath(XPATH_SEPARATOR, ENCLOSURE, child, null))
							if (addXMLValuer) {
								sb.append(COLUMN_SEPARATOR)
							}
							sb.append(LINE_BREAK)
						}
						def attributes = child.attributes()
						// Append attribute
						if (attributes) {
							for (attribute in attributes) {
								sb.append("@${attribute.key}" + COLUMN_SEPARATOR + XPATH_SEPARATOR + createPath(XPATH_SEPARATOR, ENCLOSURE, child, null))
								if (addXMLValuer) {
									sb.append(COLUMN_SEPARATOR + "${attribute.value}")
								}
								sb.append(LINE_BREAK)
							}
						}
						if (grandchildren) {
							createExcelMappingSheet.trampoline(child).call()
						} else {
							xPath = createPath(XPATH_SEPARATOR, ENCLOSURE, child, null)
							if (xPath) {
								xPath = xPath.substring(0, (xPath.length() - XPATH_SEPARATOR.length() - child.name().length()))
							}
							sb.append(child.name() + COLUMN_SEPARATOR + XPATH_SEPARATOR + xPath)
							if (addXMLValuer) {
								sb.append(COLUMN_SEPARATOR)
							}
							sb.append(LINE_BREAK)
						}
						break
					case String:
						// Append node
						xPath = createPath(XPATH_SEPARATOR, ENCLOSURE, node, null)
						if (xPath) {
							xPath = xPath.substring(0, (xPath.length() - XPATH_SEPARATOR.length() - node.name().length()))
						}
						sb.append(node.name() + COLUMN_SEPARATOR + XPATH_SEPARATOR + xPath)
						if (addXMLValuer) {
							sb.append(COLUMN_SEPARATOR + child)
						}
						sb.append(LINE_BREAK)
						break
				}
			}
		}.trampoline()

		// Create header line
		sb.append(xmlType + " Field" + COLUMN_SEPARATOR + xmlType + " Structure")
		if (addXMLValuer) {
			sb.append(COLUMN_SEPARATOR + "Source Parameter")
		}
		sb.append(LINE_BREAK)
		// Append root node
		sb.append(COLUMN_SEPARATOR + XPATH_SEPARATOR + root.name())
		if (addXMLValuer) {
			sb.append(COLUMN_SEPARATOR)
		}
		sb.append(LINE_BREAK)
		// Append XML structure
		createExcelMappingSheet(root)

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
		if (!propertyValue) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * createPath
 * @param separator This is the separator.
 * @param enclosure This is the enclosure.
 * @param node This is the node.
 * @param root This is root.
 * @return path Return path.
 */
private def String createPath(String separator, String enclosure, Node node, String root) {
	Node parentNode = node.parent()
	if (parentNode) {
		return createPath(separator, enclosure, parentNode, root) + separator + enclosure + node.name() + enclosure
	}
	else {
		if (root){
			return root
		}
		else {
			return node.name()
		}
	}
}