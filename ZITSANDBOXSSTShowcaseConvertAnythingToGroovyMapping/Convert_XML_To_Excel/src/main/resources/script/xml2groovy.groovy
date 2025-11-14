import com.sap.gateway.ip.core.customdev.util.Message

Message processData(Message message) {
	String payload = message.getBody(java.lang.String) as String
	XmlParser parser = new XmlParser()
	Node root = parser.parseText(payload)
	
	StringBuilder sb = new StringBuilder()
	
	//Addition for "Create Mapping with Constant Values": Find all nodes with the same value within second source XML
	boolean searchForValue = false
	String secondSourcePayload = message.getProperty("secondSource")
	Node secondSourceRoot
	if (secondSourcePayload) {
		searchForValue = true
		XmlParser secondSourceParser = new XmlParser()
		secondSourceRoot = secondSourceParser.parseText(secondSourcePayload)
	}
	
	//Create Excel Mapping Sheet
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
					if (newSegment) {
						sb.append("\t/" + createPath("/", "", child, null) + "\t/" + createPath("/", "", child, null) + "\t")
						if (searchForValue) { sb.append("\t") }
						sb.append("\r\n")
					}
					def attributes = child.attributes()
					if (attributes) {
						for (attribute in attributes) {
							sb.append("@${attribute.key}\t/" + createPath("/", "", child, null) + "\t/" + createPath("/", "", child, null) + "/@${attribute.key}\t${attribute.value}\r\n")
						}
					}
					if (grandchildren) {
						createExcelMappingSheet.trampoline(child).call()
					}
					else {
						sb.append(child.name() + "\t/" + createPath("/", "", node, null) + "\t/" + createPath("/", "", child, null))
						if (searchForValue) { sb.append("\t") }
						sb.append("\r\n")
					}
					break
				case String:
					sb.append(node.name() + "\t/" + createPath("/", "", node.parent(), null) + "\t/" + createPath("/", "", node, null) + "\t" + child)
					if (searchForValue) {
						sb.append('\t"')
						def nodeListWithValue = secondSourceRoot.'**'.findAll{ it.text() == child }
						if (nodeListWithValue) {
							sb.append("The value '" + child + "' was found within the following source nodes:\r\n")
							for (secondSourceNode in nodeListWithValue) {
								sb.append("/" + createPath("/", "", secondSourceNode, null) + "\r\n")
							}
						}
						sb.append('"')
					}
					sb.append("\r\n")
					break
			}
		}
	}.trampoline()
	sb.append("Target Field\tTarget Structure\tTarget XPath\tExample Value")
	if (searchForValue) {
		sb.append("\tPossible Source Structures")
	}
	sb.append("\r\n")
	sb.append("\t/" + root.name() + "\t/"  + root.name() + "\t")
	if (searchForValue) { sb.append("\t") }
	sb.append("\r\n")
	createExcelMappingSheet(root)
	
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