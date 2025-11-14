/**
* Custom Functions Runtime Pool
*
* @author itelligence.de
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Gets business system SAP ERP (SID).
 * Execution mode: Single value
 *
 * @param input value (not used)
 * @return Returns SAP ERP SID.
 */
public def String getBusinessSystemSAPERP(String input) {
	// Use tenat ID to return SAP ERP SID
	// You need to insert project specific tenant ID and SAP ERP SID in this method
	String returnValue = ""

    //Get tenant ID
	String appUrl = System.getenv("HC_APPLICATION_URL")
	appId = appUrl.substring(8, appUrl.indexOf("ifl"))

	// Get SAP ERP SID
	if (appId.equals("p0293")) {
		// SAP ERP SID of DEV system
		returnValue = "ER3"
	} else if (appId.equals("cpi_QAS")) {
		// SAP ERP SID of QAS system
		returnValue = "ER2"
	} else if (appId.equals("cpi_PRD")) {
		// SAP ERP SID of PRD system
		returnValue = "ER1"
	}

	return returnValue
}

/**
 * Throw exception if 'true'.
 * Execution mode: Single value
 *
 * @param active Active (true or false)
 * @param message Exceptin message
 * @return Returns exception if 'true'.
 */
public def String throwExceptionIfTrue(String active, String message) {
	if (active.equalsIgnoreCase("true")) {
		throw new RuntimeException(message)
	}

	return ""
}

/**
 * Get file name from message header.
 * Execution mode: Context
 *
 * @param fileName File name
 * @param context Mapping context
 * @return Returns file name.
 */
public def String getFileNameFromFileAdapter(String value,MappingContext context) {
	def returnValue = context.getHeader("CamelFileName")
	return returnValue
}