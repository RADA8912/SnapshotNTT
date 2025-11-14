import com.sap.gateway.ip.core.customdev.util.Message

// Convert a Lobster-Mapping-Export to an Excel-Mapping.
def Message processData(Message message) {
	
	// You need to configure this! :)
	boolean ediToIdoc = true // true=EDIFACT to IDoc - or - false=IDoc to EDIFACT
	def idocType = 'ORDERS05' // For XPaths, dynamic in case of IDoc on Target side
	def ediType = 'M_ORDERS' // For XPaths
	
	// Or just get Properties
	def ediToIdocTemp = message.getProperty("ediToIdoc")
	if (ediToIdocTemp) { ediToIdoc = ediToIdocTemp.toBoolean() }
	def idocTypeTemp = message.getProperty("idocType")
	if (idocTypeTemp) { idocType = idocTypeTemp }
	def ediTypeTemp = message.getProperty("ediType")
	if (ediTypeTemp) { ediType = ediTypeTemp }
	
	// Read the input
	String body = message.getBody(String)
	def rows = body.split("\\r?\\n")
	
	// String Builder
	StringBuilder sb = new StringBuilder()
	//sb.append("Source Field\tSource Structure\tTarget Field\tTarget Structure\tTarget Description\tMapping rule\tSource Method\tSource Parameter\r\n")
	
	// Fields from Lobster Excel Export
	def type
	def path
	//def description
	//def remark
	def iterationFixValue
	def mappedToSource
	def calculationField
	def functions
	
	// Helper Variables
	def isLastRowRelevant = false
	
	// Search for the IDoc Type
	if (ediToIdoc) {
		for(r in rows) {
			def row = r.split("\\t")
			if (row.length > 4 && row[1].contains("IDOCTYP")) {
				idocType = row[4]
				break
			}
		}
	}
	
	// Create first Excel-Mapping-Rows fix
	if (ediToIdoc) {
		sb.append("\t\t\t\"/${idocType}\"\t\t\"constant\"\t\"constant\"\t\r\n")
		sb.append("\t\t\t\"/${idocType}/IDOC\"\t\t\"constant\"\t\"constant\"\t\r\n")
		sb.append("\t\t\t\"/${idocType}/IDOC/EDI_DC40\"\t\t\"constant\"\t\"constant\"\t\r\n")
		sb.append("\t\t\"@BEGINN\"\t\"/${idocType}/IDOC/EDI_DC40\"\t\t\"constant\"\t\"constant\"\t\"1\"\r\n")
	}
	
	// Go through the input
	for(r in rows) {
		def row = r.split("\\t")
		if (row[0].equals("Field") || row[0].equals("Node")) {
			// Create Excel-Mapping-Row from the last Input-Row, if relevant.
			if (isLastRowRelevant) {
				sb.append(createExcelRow(type, path, iterationFixValue, mappedToSource, calculationField, functions, idocType, ediType, ediToIdoc))
				if (ediToIdoc && !(path.toLowerCase().contains("/init/")) && (type.equals("Node") || path.contains("D-Header-"))) {
					sb.append(createAttributeForExcelRow(type, path, idocType))
				}
			}
			// Reminder for the last type
			def lastType = type
			// Collect new fields
			type = row[0]
			if (row.length > 1) {path = row[1]} else { path = '' }
			if (row.length > 4) {iterationFixValue = row[4]} else { iterationFixValue = '' }
			if (row.length > 5) {mappedToSource = row[5]} else { mappedToSource = '' }
			if (row.length > 6) {calculationField = row[6]} else { calculationField = '' }
			if (row.length > 7) {functions = row[7].replace("Filter: ","")} else { functions = '' }
			// Determine, if new row is relevant. Default is true
			isLastRowRelevant = true
			// If no mapping is implemented for the field, it is not relevant.
			if (iterationFixValue.equals("") && mappedToSource.equals("") && functions.equals("")) {
				isLastRowRelevant = false
			}
			// Filter of fields for a Flat-File-IDoc only.
			if (!path.startsWith("/EDI_DC40/EDI_DC40") && (path.contains("-MANDT") || path.contains("-DOCNUM") || path.contains("-SEGNUM") || path.contains("-PSGNUM") || path.contains("-HLEVEL"))) {
				isLastRowRelevant = false
			}
			// Filter if both "Node" and "Field-Header" line are both in the source.
			if (lastType.equals("Node") && (path.contains("D-Header-"))) {
				isLastRowRelevant = false
			}
		}
		else if (row[0].equals("") && row.length > 7 && isLastRowRelevant) {
			// Collet additional information for the "Functions"-Field.
			if (row[7].startsWith("Filter: ")) {
				functions += " | " + row[7].replace("Filter: ","")
			}
			else {
				functions += ", " + row[7]
			}
		}
	}
	
	// Create Excel-Mapping-Row from the very last Input-Row, if relevant.
	if (isLastRowRelevant) {
		sb.append(createExcelRow(type, path, iterationFixValue, mappedToSource, calculationField, functions, idocType, ediType, ediToIdoc))
		if (ediToIdoc && !(path.toLowerCase().contains("/init/")) && (type.equals("Node") || path.contains("D-Header-"))) {
			sb.append(createAttributeForExcelRow(type, path, idocType))
		}
	}
	
	// Build message and return
	message.setBody(sb.toString())
	return message
}

def String createExcelRow( def type, def path, def iterationFixValue, def mappedToSource, def calculationField, def functions, def idocType, def ediType, boolean ediToIdoc ) {
	// Fields for the Excel-Mapping-File
	def sourceField = ''
	def sourceStructure = ''
	def targetField = ''
	def targetStructure = ''
	def targetDescription = ''
	def mappingRule = ''
	def sourceMethod = ''
	def sourceParameter = ''
	// type: "Node"
	if (type.equals("Node")) {
		if (ediToIdoc) {
			targetStructure = getIDocPath(path, false, idocType)
			targetDescription = getDescriptionFromIDocPath(path)
		}
		else {
			targetStructure = getEdifactPath(path, true, ediType)
		}
		// Case: 1:1 Mapping for a segment
		if (functions.equals("")) {
			mappingRule = "1:1"
			sourceMethod = "1:1"
		// Case: Complex Mapping for a segment
		} else {
			mappingRule = functions
			sourceMethod = "To Do"
		}
		if (!iterationFixValue.equals("")) {
			mappingRule += ", Source: " + iterationFixValue
		}
	// type: "Field"
	} else {
		if (ediToIdoc) {
			targetField = getIDocField(path)
			targetStructure = getIDocPath(path, true, idocType)
		}
		else {
			targetField = getEdifactField(path)
			targetStructure = getEdifactPath(path, true, ediType)
		}
		// Case: Write to a variable
		if (!functions.contains("|") && functions.startsWith("save variable a(b) type-safe,") && mappedToSource.equals("")){
			def functionsSplit = functions.split("'")
            targetField = ''
            if (functionsSplit.length > 1) { targetField = "*" + functionsSplit[1].replaceAll("[^a-zA-Z0-9 ]", "") }
			mappingRule = "variable"
			sourceMethod = "setVariable"
			sourceParameter = ''
			if (functionsSplit.length > 3) { sourceParameter = functionsSplit[3] }
		}
		// Case: Read from a variable
		else if (!functions.contains("|") && functions.startsWith("copy(field/value/variable),") && !(functions.contains("%"))){
			def functionsSplit = functions.split("'")
			mappingRule = "variable"
			sourceMethod = "getVariable"
			sourceParameter = ''
			if (functionsSplit.length > 1) { sourceParameter = functionsSplit[1].replaceAll("[^a-zA-Z0-9 ]", "") }
		}
		// Case: Read from a property
		else if (!functions.contains("|") && functions.startsWith("copy(field/value/variable),") && functions.contains("%")){
			def functionsSplit = functions.split("%")
			mappingRule = "property"
			sourceMethod = "getExchangeProperty"
			sourceParameter = ''
			if (functionsSplit.length > 1) { sourceParameter = functionsSplit[1].replaceAll("[^a-zA-Z0-9 ]", "") }
		}
		// Case: Create a segment fix
		else if (targetField.equals("")){
			if (ediToIdoc) {
				targetDescription = getDescriptionFromIDocPath(path)
			}
			mappingRule = "constant"
			sourceMethod = "constant"
		}
		// Case: Constant for a field
		else if (!iterationFixValue.equals("")) {
			mappingRule = "constant"
			sourceMethod = "constant"
			sourceParameter = iterationFixValue
		}
		// Case: 1:1 Mapping for a field
		else if (!mappedToSource.equals("") && functions.equals("")) {
			if (ediToIdoc) {
				sourceField = getEdifactField(mappedToSource)
				sourceStructure = getEdifactPath(mappedToSource, true, ediType)
			}
			else {
				sourceField = getIDocField(mappedToSource)
				sourceStructure = getIDocPath(mappedToSource, true, idocType)
			}
			mappingRule = "1:1"
			sourceMethod = "1:1"
		}
		// Case: Function without Source for a field
		else if (mappedToSource.equals("") && !(functions.equals(""))) {
			mappingRule = functions
			sourceMethod = "To Do"
		}
		// Case: Complex Mapping for a field
		else {
			if (ediToIdoc) {
				sourceField = getEdifactField(mappedToSource)
				sourceStructure = getEdifactPath(mappedToSource, true, ediType)
			}
			else {
				sourceField = getIDocField(mappedToSource)
				sourceStructure = getIDocPath(mappedToSource, true, idocType)
			}
			mappingRule = functions
			sourceMethod = "To Do"
		}
	}
	return "\"${sourceField}\"\t\"${sourceStructure}\"\t\"${targetField}\"\t\"${targetStructure}\"\t\"${targetDescription}\"\t\"${mappingRule}\"\t\"${sourceMethod}\"\t\"${sourceParameter}\"\r\n"
}

def String createAttributeForExcelRow( def type, def path, def idocType ) {
	// Fields for the Excel-Mapping-File
	def sourceField = ''
	def sourceStructure = ''
	def targetField = "@SEGMENT"
	def targetStructure = ''
	def targetDescription = ''
	def mappingRule = "constant"
	def sourceMethod = "constant"
	def sourceParameter = "1"
	if (type.equals("Node")) {
		targetStructure = getIDocPath(path, false, idocType)
	} else {
		targetStructure = getIDocPath(path, true, idocType)
	}
	return "\"${sourceField}\"\t\"${sourceStructure}\"\t\"${targetField}\"\t\"${targetStructure}\"\t\"${targetDescription}\"\t\"${mappingRule}\"\t\"${sourceMethod}\"\t\"${sourceParameter}\"\r\n"
}

def String getIDocPath(String path, boolean cutField, String idocType) {
	if (path.equals("")) {
		return ""
	} else {
		def structure = path.split("/")
		if (cutField){
			structure = structure.dropRight(1)
		}
		def result = []
		if (structure.length > 1 && structure[1].equalsIgnoreCase("init")) {
			result = structure
		}
		else if (structure.length > 2 && structure[1].equals("EDI_DC40") && structure[2].equals("EDI_DC40_FIELDS")) {
			result += ""
			result += idocType
			result += "IDOC"
			result += "EDI_DC40"
		}
		else if (structure.length > 2 && structure[1].equals("EDI_DC40")) {
			result += ""
			result += idocType
			result += "IDOC"
			for (orinialStep in structure) {
				if (!orinialStep.equals("") && !orinialStep.contains("EDI_DC40") && !orinialStep.contains("_FIELDS")){
					def modifiedStep = orinialStep
					if (modifiedStep.startsWith("E2ED")) {
						modifiedStep = modifiedStep.split("#")[0].split("-")[0].split("_")[0].replace("E2ED","E1ED").take(7)
					}
					result += modifiedStep
				}
			}
		}
		else {
			result = structure
		}
		return result.join("/")
	}
}

def String getIDocField(String path) {
	String field = path.split("/").last()
	if (field.startsWith("D-Header-")){
		field = ""
	}
	if (field.startsWith("D-")){
		field = field.substring(2)
	}
	field = field.split("#")[0].split("-")[0]
	return field
}

def String getDescriptionFromIDocPath(String path) {
	def structure = path.split("/")
	def result = ''
	if (structure.length > 2 && structure[2].contains("#")) {
		result = structure[2].split("#")[1].replace("_"," ")
	} else if (structure.length > 2 && structure[2].contains("-")) {
		result = structure[2].split("-")[1].replace("_"," ")
	} else if (structure.length > 2 && structure[2].contains("_")) {
		result = structure[2].split("_")[1]
	}
	return result
}

def String getEdifactPath(String path, boolean cutField, def ediType) {
	if (path.equals("")) {
		return ""
	} else {
		def structure = path.split("/")
		if (cutField){
			structure = structure.dropRight(1)
		}
		def result = []
		if (structure.length > 1 && structure[1].equalsIgnoreCase("init")) {
			result = structure
		}
		else {
			result += ""
			result += "Interchange"
			if (structure.length > 1 && !(structure[1].equals("UNB"))) {
				result += ediType
			}
			for (orinialStep in structure) {
				if (orinialStep.equals("")){
				} else if (orinialStep.matches("SG\\d+.*")){
					result += "G_" + orinialStep.split("#")[0].split("_")[0].split("-")[0]
				} else if (orinialStep.matches("C\\d+.*") || orinialStep.matches("S\\d+.*")){
					def modifiedStep = orinialStep.split("#")[0].split("_")[0]
					if (modifiedStep.endsWith("-1")) {
						modifiedStep = modifiedStep.replace("-1","")
					}
					modifiedStep = modifiedStep.replace("-","_")
					result += "C_" + modifiedStep
				} else {
					result += "S_" + orinialStep.split("#")[0]/*.split("_")[0]*/.split("-")[0]
				}
			}
		}
		return result.join("/")
	}
}

def String getEdifactField(String path) {
	String field = path.split("/").last().replace("F","D_")
	if (field.endsWith("-1")) {
		field = field.replace("-1","")
	}
	field = field.replace("-","_").split("#")[0]
	return field
}