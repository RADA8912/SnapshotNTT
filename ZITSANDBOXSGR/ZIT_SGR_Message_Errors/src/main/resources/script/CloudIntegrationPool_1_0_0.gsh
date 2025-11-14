/**
* Custom Functions Cloud Integration Pool
*
* @author itelligence.de
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Gets a value from header
 * Execution mode: Context
 *
 * @param Header value
 * @param context Mapping context
 * @return Returns a header value.
 */
def String getHeader(String headerName,MappingContext context) {

	def returnValue = context.getHeader(headerName)
	return returnValue
}

/**
 * Gets a value from a property
 * Execution mode: Context
 *
 * @param Property value
 * @param context Mapping context
 * @return Returns a property value.
 */
def String getProperty(String propertyName,MappingContext context) {

	def returnValue = context.getProperty(propertyName)
	return returnValue
}