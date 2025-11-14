import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	String payload = message.getBody(java.lang.String) as String
	XmlParser parser = new XmlParser()
	Node root = parser.parseText(payload)
	
	StringBuilder sb = new StringBuilder()
	int tabLevel = 0
	String rootName = "input"

	//Create 1:1 Mapping
	def createOneToOneMapping
	createOneToOneMapping = { Node node ->
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
							sb.append("'${attribute.key}': " + createPath(".", "'", child, rootName) + ".@'${attribute.key}'.text()")
						}
						sb.append(")")
					}
					def grandchildren = child.children()
					if (grandchildren) {
						createOneToOneMapping.trampoline(child).call()
					}
					else {
						sb.append(" " + createPath(".", "'", child, rootName) + ".text()\r\n")
					}
					break
				case String:
					sb.append(" " + createPath(".", "'", node, rootName) + ".text()\r\n")
					break
			}
		}
		if (!createNewSegment) {
			tabLevel--
			sb.append("\t".multiply(tabLevel) + "}\r\n")
		}
	}.trampoline()
	tabLevel = 1
	sb.append("//----Create 1:1 Mapping---------------------------------------------\r\n")
	sb.append("import com.sap.gateway.ip.core.customdev.util.Message\r\n")
	sb.append("import groovy.xml.MarkupBuilder\r\n")
	sb.append("\r\n")
	sb.append("def Message processData(Message message) {\r\n")
	sb.append("\tdef reader = message.getBody(Reader)\r\n")
	sb.append("\tdef " + rootName + " = new XmlSlurper().parse(reader)\r\n")
	sb.append("\tdef writer = new StringWriter()\r\n")
	sb.append("\tdef builder = new MarkupBuilder(writer)\r\n")
	sb.append("\t".multiply(tabLevel) + "builder.'" + root.name() + "'")
	createOneToOneMapping(root)
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