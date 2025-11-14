/**
* Groovy Custom Functions Format Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

/**
 * Remove all spaces from string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value without spaces.
 */
public def Message processData(Message message) {
  
   def mapProperties = message.getProperties();
   def valueProperty = mapProperties.get("FileName");
   def formattedValue = valueProperty.replaceAll(".pgp","");

		// Use RegEx for remove pgp extension
	//	valueProperty = valueProperty.replaceAll(".pgp","")
	
	 message.setProperty("FileName", formattedValue);
	
	return message;
}