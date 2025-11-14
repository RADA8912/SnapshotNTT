import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	String payload = message.getBody(java.lang.String) as String
	XmlParser parser = new XmlParser()
	Node root = parser.parseText(payload)
	
	StringBuilder sb = new StringBuilder()
	int tabLevel = 0
	String rootName = "input"
	
	//Addition for "Create Mapping with Constant Values": Find all nodes with the same value within second source XML
	boolean searchForValue = true
	String secondSourceProperty = "secondSourceProperty"
	String secondSourcePayload = message.getProperty(secondSourceProperty)
	XmlParser secondSourceParser = new XmlParser()
	Node secondSourceRoot = secondSourceParser.parseText(secondSourcePayload)
	
	//Create Mapping with Constant Values
	def createMappingWithConstantValues
	createMappingWithConstantValues = { Node node ->
		boolean createNewSegment = true
		node.children().each { child ->
			switch (child){
				case Node:
					if (createNewSegment) {
						sb.append(" {\r\n")
						tabLevel++
						createNewSegment = false
					}
					sb.append("\t".multiply(tabLevel) + "'" + child.name() + "'")
					def attributes = child.attributes()
					if (attributes) {
						sb.append(" (")
						boolean isFirstAttribute = true
						for (attribute in attributes) {
							if (isFirstAttribute) {
								isFirstAttribute = false
							} else {
								sb.append(", ")
							}
							sb.append("'${attribute.key}': '${attribute.value}'")
						}
						sb.append(")")
					}
					def grandchildren = child.children()
					if (grandchildren) {
						createMappingWithConstantValues.trampoline(child).call()
					}
					else {
						sb.append(" ''\r\n")
					}
					break
				case String:
					sb.append(" '" + child + "'\r\n")
					if (searchForValue) {
						def nodeListWithValue = secondSourceRoot.'**'.findAll{ it.text() == child }
						if (nodeListWithValue) {
							sb.append("\t".multiply(tabLevel) + "/* The value '" + child + "' was found in the following source nodes:\r\n")
							for (secondSourceNode in nodeListWithValue) {
								sb.append("\t".multiply(tabLevel) + "   " + createPath(".", "'", secondSourceNode, rootName) + ".text()\r\n")
							}
							sb.append("\t".multiply(tabLevel) + "*/\r\n")
						}
					}
					break
			}
		}
		if (!createNewSegment) {
			tabLevel--
			sb.append("\t".multiply(tabLevel) + "}\r\n")
		}
	}.trampoline()
	tabLevel = 1
	sb.append("//----Create Mapping with Constant Values----------------------------\r\n")
	if (searchForValue) {
		sb.append("//----Find all nodes with the same value within second source XML----\r\n")
	}
	sb.append("import com.sap.gateway.ip.core.customdev.util.Message\r\n")
	sb.append("import groovy.xml.MarkupBuilder\r\n")
	sb.append("\r\n")
	sb.append("def Message processData(Message message) {\r\n")
	if (searchForValue) {
		sb.append("\tdef reader = message.getBody(Reader)\r\n")
		sb.append("\tdef " + rootName + " = new XmlSlurper().parse(reader)\r\n")
	}
	sb.append("\tdef writer = new StringWriter()\r\n")
	sb.append("\tdef builder = new MarkupBuilder(writer)\r\n")
	sb.append("\t".multiply(tabLevel) + "builder.'" + root.name() + "'")
	createMappingWithConstantValues(root)
	sb.append("\tmessage.setBody(writer.toString())\r\n")
	sb.append("\treturn message\r\n")
	sb.append("}\r\n")

	message.setBody(sb.toString())
	return message
}

String createPath(String separator, String enclosure, Node node, String root) {
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