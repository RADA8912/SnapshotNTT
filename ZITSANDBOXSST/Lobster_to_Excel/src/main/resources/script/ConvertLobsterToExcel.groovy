// TODO: EDIFACT path & Field
	
import com.sap.gateway.ip.core.customdev.util.Message

// Convert a Lobster-Mapping-Export to an Excel-Mapping.
// For EDIFACT to IDoc Mappings.
def Message processData(Message message) {
	
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
	def idocType = 'IDOCTYP'
	
	// Search for the IDoc Type
	for(r in rows) {
		def row = r.split("\\t")
		if (row.length > 4 && row[1].contains("IDOCTYP")) {
			idocType = row[4]
			break
		}
	}
	
	// Create first Excel-Mapping-Rows fix
	sb.append("\t\t\t\"/${idocType}\"\t\t\"constant\"\t\"constant\"\t\r\n")
	sb.append("\t\t\t\"/${idocType}/IDOC\"\t\t\"constant\"\t\"constant\"\t\r\n")
	sb.append("\t\t\t\"/${idocType}/IDOC/EDI_DC40\"\t\t\"constant\"\t\"constant\"\t\r\n")
	sb.append("\t\t\"@BEGINN\"\t\"/${idocType}/IDOC/EDI_DC40\"\t\t\"constant\"\t\"constant\"\t\"1\"\r\n")
	
	// Go through the input
	for(r in rows) {
		def row = r.split("\\t")
		if (row[0].equals("Field") || row[0].equals("Node")) {
			// Create Excel-Mapping-Row from the last Input-Row, if relevant.
			if (isLastRowRelevant) {
				sb.append(createExcelRow(type, path, iterationFixValue, mappedToSource, calculationField, functions, idocType))
				if (!(path.contains("/init/")) && (type.equals("Node") || path.contains("D-Header-"))) {
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
		sb.append(createExcelRow(type, path, iterationFixValue, mappedToSource, calculationField, functions, idocType))
		if (!(path.contains("/init/")) && (type.equals("Node") || path.contains("D-Header-"))) {
			sb.append(createAttributeForExcelRow(type, path, idocType))
		}
	}
	
	// Build message and return
	message.setBody(sb.toString())
	return message
}

def String createExcelRow( def type, def path, def iterationFixValue, def mappedToSource, def calculationField, def functions, def idocType ) {
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
		sourceStructure = getEdifactPath(iterationFixValue, false)
		targetStructure = getIDocPath(path, false, idocType)
		targetDescription = getDescriptionFromIDocPath(path)
		// Case: 1:1 Mapping for a segment
		if (functions.equals("")) {
			mappingRule = "1:1"
			sourceMethod = "1:1"
		// Case: Complex Mapping for a segment
		} else {
			mappingRule = functions
			sourceMethod = "To Do"
		}
	// type: "Field"
	} else {
		targetField = getIDocField(path)
		targetStructure = getIDocPath(path, true, idocType)
		// Case: Create a segment fix
		if (targetField.equals("")){
			targetDescription = getDescriptionFromIDocPath(path)
			mappingRule = "constant"
			sourceMethod = "constant"
		// Case: Constant for a field
		} else if (!iterationFixValue.equals("")) {
			mappingRule = "constant"
			sourceMethod = "constant"
			sourceParameter = iterationFixValue
		// Case: 1:1 Mapping for a field
		} else if (!mappedToSource.equals("") && functions.equals("")) {
			sourceField = getEdifactField(mappedToSource)
			sourceStructure = getEdifactPath(mappedToSource, true)
			mappingRule = "1:1"
			sourceMethod = "1:1"
		// Case: Function without Source for a field
		} else if (mappedToSource.equals("") && !(functions.equals(""))) {
			mappingRule = functions
			sourceMethod = "To Do"
		// Case: Complex Mapping for a field
		} else {
			sourceField = getEdifactField(mappedToSource)
			sourceStructure = getEdifactPath(mappedToSource, true)
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
	def structure = path.split("/")
	if (cutField){
		structure = structure.dropRight(1)
	}
	def result = []
	if (structure.length > 1 && structure[1].equals("init")) {
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
				modifiedStep = orinialStep.split("#")[0].split("-")[0].split("_")[0].replace("E2ED","E1ED").take(7)
				result += modifiedStep
			}
		}
	}
	else {
		result = structure
	}
	return result.join("/")
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

def String getEdifactPath(String path, boolean cutField) {
	def structure = path.split("/")
	if (cutField){
		structure = structure.dropRight(1)
	}
	return structure.join("/")
}

def String getEdifactField(String path) {
	String field = path.split("/").last()
	return field
}