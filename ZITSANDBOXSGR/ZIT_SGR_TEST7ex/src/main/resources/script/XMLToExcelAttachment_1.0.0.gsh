import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlSlurper
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource
import org.apache.camel.impl.DefaultAttachment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Row

/**
* XMLToExcelAttachment
* This Groovy script converts a plain XML from body to a binary Excel XSLX and add this as attachment.
* You can use this to send an Excel XSLX and add this as attachment via e-mail.
* With the configuration of 'header names' and 'node names' you can select needed values and define the sequence of the columns in Excel workbook.
*
* Groovy script parameters (exchange properties)
* - XMLToExcelAttachment.FileName = File name
* - XMLToExcelAttachment.SheetName = Sheet name
* - XMLToExcelAttachment.AddHeaderLine = Add header line ('true' or 'false')
* - XMLToExcelAttachment.HeaderNames = Header names in Excel
* - XMLToExcelAttachment.RecordName = Record node name in XML
* - XMLToExcelAttachment.NodeNames = Node names in XML
*
* @author nttdata-solutions.com
* @version 1.0.0
*/
def Message processData(Message message) {
	final String FILE_NAME = "Data.xlsx"
	final String SHEET_NAME = "Data"
	final boolean ADD_HEADER_LINE = true
	final String HEADER_NAMES = "Column A,Column B,Column C"
	final String RECORD_NAME = "record"
	final String NODE_NAMES = "nodeA,nodeB,nodeC"
	final String NAMES_SEPARATOR = ","

	boolean addHeaderLine = false
	int headerLine = 0

	// Get exchange properties
	// File name
	fileName = getExchangeProperty(message, "XMLToExcelAttachment.FileName", false)
	if (!fileName) {
		fileName = FILE_NAME
	}

	// Sheet name
	sheetName = getExchangeProperty(message, "XMLToExcelAttachment.SheetName", false)
	if (!sheetName) {
		sheetName = SHEET_NAME
	}

	// Add header line
	addHeaderLineIn = getExchangeProperty(message, "XMLToExcelAttachment.AddHeaderLine", false)
	if (addHeaderLineIn) {
		if ('true'.equalsIgnoreCase(addHeaderLineIn)) {
			addHeaderLine = true
		}
	} else {
		addHeaderLine = ADD_HEADER_LINE
	}

	// Header name
	headerNamesIn = getExchangeProperty(message, "XMLToExcelAttachment.HeaderNames", false)
	if (!headerNamesIn) {
		headerNamesIn = HEADER_NAMES
	}

	// Split header names
	String[] headerNames = headerNamesIn.split(NAMES_SEPARATOR, -1)

	// Record name in XML
	recordName = getExchangeProperty(message, "XMLToExcel.RecordName", false)
	if (!recordName) {
		recordName = RECORD_NAME
	}

	// Check record name
	if(!recordName) {
		throw Exception("'record name' must contain a value to find any values in XML.")
	}

	// Node name
	nodeNamesIn = getExchangeProperty(message, "XMLToExcelAttachment.NodeNames", false)
	if (!nodeNamesIn) {
		nodeNamesIn = NODE_NAMES
	}
	nodeNamesIn = nodeNamesIn.replace(' ','')

	// Check node names
	if(!nodeNamesIn) {
		throw Exception("'node names' must contain one or more values to find value(s) in XML.")
	}

	// Split node names
	String[] nodeNames = nodeNamesIn.split(NAMES_SEPARATOR, -1)

	// Check entries
	if (addHeaderLine == true) {
		if(headerNames.size() != nodeNames.size()) {
			throw Exception("Number of 'header names' and number of 'node names' must be equals.")
		}
	}

	if (message.getBodySize() > 0) {
		// Create a new workbook
		Workbook workbook = new XSSFWorkbook()

		// Create a sheet in the workbook with valid sheet name
		sheetName = setValidSheetName(sheetName)
		Sheet sheet = workbook.createSheet(sheetName)

		// Add header line
		if (addHeaderLine == true) {
			// Create a header row with column names
			Row headerRow = sheet.createRow(0)
			for(int h = 0; h < headerNames.size(); h++) {
				headerRow.createCell(h).setCellValue(headerNames[h])
			}
			headerLine = 1
		}

		// Get body
		Reader reader = message.getBody(Reader)
		XmlSlurper slurper = new XmlSlurper()
		// Keep whitespaces
		slurper.keepIgnorableWhitespace = true
		def root = slurper.parse(reader)

		// Get records
		def records = root.'**'.findAll{it.name() == recordName}

		// Only creation of Excel if record nodes are found XML
		if (message.getBodySize() > 0) {
			// Create data in rows
			for(int r = 0; r < records.size(); r++) {
				def record = records[r]
				Row row = sheet.createRow(r + headerLine)

				// Create data in cell for node names
				// This ensures that all defined node names are set to the correct position, even if no node is contained in the XML
				for(int n = 0; n < nodeNames.size(); n++) {
					String nodeName = nodeNames[n]
					row.createCell(n).setCellValue(record."${nodeName}".text())
				}
			}

			// Convert workbook to byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
			workbook.write(byteArrayOutputStream)
			byte[] bytes = byteArrayOutputStream.toByteArray()

			// Create a ByteArrayDataSource object with the byte array and the content's MIME type
			ByteArrayDataSource dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel")

			// Construct a DefaultAttachment object
			DataHandler dataHandler = new DataHandler(dataSource)
			DefaultAttachment attachment = new DefaultAttachment(dataHandler)

			// Add attachment to message
			fileName = setValidFileName(fileName)
			message.addAttachmentObject(fileName, attachment)
			
			message.setBody(bytes)
		} else {
			message.setBody('')
		}
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
		if (!propertyValue) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * setValidSheetName
 * @param sheetName Sheet name
 * @return sheetName Return valid sheet name.
 */
private String setValidSheetName(String sheetName) {
	if (sheetName) {
		// Create valid sheet name with RegEx. Remove invalide character.
		// Supported characters for sheet name are letters, numbers, spaces, and '_-'.
		sheetName = sheetName.replaceAll('[^a-zA-Z0-9 _ -]', '')

		// Remove leading and tailing whitespace with RegEx
		sheetName = sheetName.replaceAll('^\\s+|\\s+$','')

		if (sheetName.length() > 31) {
			// Set max length to 31 charachter
			sheetName = sheetName.substring(0,31)
		}
	}
	return sheetName
}

/**
 * setValidFileName
 * @param fileName File name
 * @return fileName Return valid file name.
 */
private String setValidFileName(String fileName) {
	if (fileName) {
		// Create valid file name with RegEx. Remove invalide character.
		// Supported characters for file name are letters, numbers, spaces, and '()_-,.'.
		fileName = fileName.replaceAll('[^a-zA-Z0-9 ( ) _ - , .]', '')

		// Remove leading and tailing whitespace with RegEx
		fileName = fileName.replaceAll('^\\s+|\\s+$','')
	}
	return fileName
}