import com.sap.gateway.ip.core.customdev.util.Message

/**
* TXTFixedLengthToCSV
* This Groovy script converts a flat txt payload with fixed length to a csv payload with separators.
* Program supports simple case if every line has same structure.
* It set enclosure sign if needed.
* It removes empty lines.
*
* Groovy script parameters
* - TXTFixedLengthToCSV.debugMode = 'true' creates property entries
* - TXTFixedLengthToCSV.columnWidths = numeric values separated by comma
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	// Debug mode 'true' creates property entries
	boolean debugMode = false

	// Constants
	final String DEFAULT_ENCLOSURE_SIGN = "\"" // CPI "CSV to XML Converter" uses only double apostrophe " as enclosure sign
	final String DEFAULT_FIELD_SEPARATOR = ";"
	final String DEFAULT_LINE_SEPARATOR = "\r\n"

	// Variables
	int lineWidth = 0
	long linesTotal = 0
	int beginIndex = 0
	int endIndex = 0
	long widthTotal = 0
	boolean lineCheck = false
	int i = 0
	StringBuffer str = new StringBuffer()
	String columnEntry = ""
	
	// Set messag log object
	def messageLog = messageLogFactory.getMessageLog(message)

	// Get message properties and set default mapping parameters
	// Set parameter DEBUG_MODE from the Message Properties
	String inDebugMode = getExchangeProperty(message, "TXTFixedLengthToCSV.debugMode", false) as String
	if (inDebugMode != null) {
		if (inDebugMode.toLowerCase() == "true") {
			debugMode = true
		} else if (inDebugMode.toLowerCase() == "yes") {
			debugMode = true
		} else if (inDebugMode.toLowerCase() == "y") {
			debugMode = true
		}
	}

	// Set parameter COLUMN_WIDTHS from the Message Properties
	String inColumnWidths = getExchangeProperty(message, "TXTFixedLengthToCSV.columnWidths", true) as String

	// Create integer array of column widths
	Integer[] columnWidths = getColumnWidths(inColumnWidths)
	lineWidth = sumIntArray(columnWidths)

	// Set messag log in debug mode
	if (debugMode == true) {
		if(messageLog != null){
			messageLog.setStringProperty("TXTFixedLengthToCSV.debugMode", debugMode.toString())
			messageLog.setStringProperty("TXTFixedLengthToCSV.columnWidths", inColumnWidths.toString())
		}
	}

	// Get body
	def body = message.getBody(java.lang.String)

	// Remove leading UTF-8 BOM
	if (body.startsWith("\uFEFF")) {
		body = body.substring(1)
	}

	// Count lines
	linesTotal = countLines(body)

	// Loop all CSV lines
	body.eachLine { line, count ->
		// Reset values
		beginIndex = 0
		endIndex = 0
		widthTotal = 0

		// Remove blank lines
		if (! line.trim().isEmpty()) {
			// Check width of line
			lineCheck = checkLineWidth(line, lineWidth)

			// Loop columns
			for (i = 0; i < columnWidths.size(); i++) {
				// Compute Index
				beginIndex = widthTotal
				endIndex = widthTotal + columnWidths[i]

				// Append column entry
				columnEntry = line.substring(beginIndex, endIndex)
				// Set enclosure sign if needed
				columnEntry = setEnclosureSign(columnEntry, DEFAULT_FIELD_SEPARATOR, DEFAULT_ENCLOSURE_SIGN)
				str.append(columnEntry)

				// Append field separator
				if (i < columnWidths.size() - 1) {
					str.append(DEFAULT_FIELD_SEPARATOR)
				}
				
				// Set total width
				widthTotal = endIndex
			}

			// Append line separator
			if (count < linesTotal - 1) {
				str.append(DEFAULT_LINE_SEPARATOR)
			}
		}
	}

	message.setBody(str.toString())
	return message
}

/**
 * countLines
 * @param str This is string.
 * @return countLines Return count lines.
 */
static long countLines(String str){
	long linesLength = 0
	if (str != null) {
		String[] lines = str.split("\r\n|\r|\n")
		linesLength = lines.length
	} 
	return linesLength
}

/**
 * getColumnWidths
 * @param columnWidths This is column widths.
 * @return getColumnWidths Return integer column widths.
 */
static Integer[] getColumnWidths(String columnWidths) {
	final String PARAMETER_SEPARATOR = ","
	String columnWidth = ""

	// Check separator comma
	if (columnWidths.indexOf(PARAMETER_SEPARATOR) < 0) {
		throw new Exception("Parameter separator comma is missing")
	}

	// Create integer array of column widths
	String[] columnWidthsStr = columnWidths.replaceAll(" ", "").split(PARAMETER_SEPARATOR)
	Integer[] columnWidthsInt = new int[columnWidthsStr.length]
	Integer c = 0
	for (c = 0; c < columnWidthsStr.length; c++) {
		// Check value if numeric
		columnWidth = columnWidthsStr[c]			
		if (!columnWidth.isNumber()) {
			throw new Exception("Parameter of column width '" + columnWidth + "' has not mandatory numeric sign.")
		}

		// Set value to integer array
		columnWidthsInt[c] = Integer.valueOf(columnWidthsStr[c]) as Integer
	}
	return columnWidthsInt
}

/**
 * sumIntArray
 * @param arr This array of numbers (integer).
 * @return sumIntArray Return sum of elements in an array.
 */
static int sumIntArray(int[] arr) {
	int sum = 0
	int i = 0
	
	// Iterate through all elements and add to sum
	for (i = 0; i < arr.length; i++) {
		sum +=  arr[i]
	}
	return sum
}

/**
 * checkLineWidth
 * @param line This is line.
 * @param lineWidth This is mandatory width of line.
 * @return propertyValue Return value.
 */
def checkLineWidth(String line, int lineWidth) {
	boolean lineCheck = true
	if (line.length() != lineWidth) {
		throw new Exception("Line '$line' has not mandatory $lineWidth signs.")
	}
	return lineCheck
}

/**
 * setEnclosureSign
 * @param value This is value.
 * @param fieldSeparator This is field separator.
 * @param enclosureSign This is enclosure sign.
 * @return setEnclosureSign Return value with enclosure sign.
 */
static String setEnclosureSign(String value, String fieldSeparator, String enclosureSign) {
	// Set enclosure sign if needed
	if (value.indexOf(fieldSeparator) > 0) {
		value = enclosureSign + value + enclosureSign
	}
	return value
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
			throw new Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}