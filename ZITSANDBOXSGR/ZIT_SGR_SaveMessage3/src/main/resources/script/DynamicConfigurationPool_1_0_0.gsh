/**
* Custom Functions Dynamic Configuration Pool
*
* @author itelligence.de
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Set file name to dynamic configuration. First argument is file name.
 * Execution mode: Single value
 *
 * @param fileName File name
 * @return Returns 'true'.
 */
public def String setFileNameForFileAdapter(String fileName,MappingContext context) {
	context.setHeader("CamelFileName", fileName)
	return "true"
}