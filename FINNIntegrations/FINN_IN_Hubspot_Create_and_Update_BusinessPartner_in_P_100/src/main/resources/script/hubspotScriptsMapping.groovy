import com.sap.it.api.mapping.*;



/**
 * This method returns the exact value that is stored for the property variable.
 * @param propertyName
 * @param context
 * @return propertyValue
 */

 def String getProperty(String propertyName,MappingContext context)  {
 String PropertyValue= context.getProperty(propertyName); 
 PropertyValue= PropertyValue.toString(); 
 return PropertyValue; 

 }


/**
 *  * This method returns the exact value that is stored for the header variable.

 * @param headerName
 * @param context
 * @return headerValue
 */
 def String getHeader(String headerName,MappingContext context) {
 String HeaderValue = context.getHeader(headerName); 
 HeaderValue= HeaderValue.toString();

 return HeaderValue; 

 }



/**
 * Remove all special characters from string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value without special characters.
 */
public def String removeAllSpecialCharacters(String value) {
	String returnValue = ""

	if (value != null) {
		// Use RegEx for remove all special characters
		//returnValue = value.replaceAll("[^a-zA-Z0-9/.,: _+-]+","")
		// Lets just replace double quotes
		returnValue = value.replaceAll("\"","")
	}

	return returnValue
}