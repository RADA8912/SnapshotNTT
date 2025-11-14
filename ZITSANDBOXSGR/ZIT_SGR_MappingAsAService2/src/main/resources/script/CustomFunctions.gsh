/**
* Custom Functions
*
* @author itelligence.de
* @version 0.2.0
*/

import com.sap.it.api.mapping.*

/**
 * Creates a new string from a non-empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill with "0" (if the fourth says "true") at left or right side (defined by the fifth))
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @param cutLengthDirection Cut length direction "left" or "right" end cut
 * @param fillZero Fill zero "true" = fill
 * @param fillZeroDirection Fill zero direction "left" or "right" end fill
 * @return Returns 
 */
public def String formatValueByZero(String value, String length, String cutLengthDirection, String fillZero, String fillZeroDirection) {
	String output = null
	
	if (value != null) {
		int lengthInt
		try {
			lengthInt = Integer.parseInt(length)
		} catch (Exception ex) {
			throw new RuntimeException("Custom function formatValueByZero: length '" + length + "' is not a numeric value.")
		}
		int lengthValue = value.length()

		if (lengthValue > 0 && lengthValue != lengthInt) {
			if (lengthValue > lengthInt) {
				if ("left".equalsIgnoreCase(cutLengthDirection)) {
					int offset = lengthValue - lengthInt
					output = value.substring(offset, lengthValue)
				} else if ("right".equalsIgnoreCase(cutLengthDirection)) {
					output = value.substring(0, lengthInt)
				} else {
					throw new RuntimeException("Custom function formatValueByZero: unexpected value '" + cutLengthDirection + "' for the cutDirection")
				}
			} else {
				if ("true".equalsIgnoreCase(fillZero)) {
					// now lengthValue < lengthInt
					int offset = lengthInt - lengthValue
					String zeroString = "0"
					for (int i = 0; i < offset - 1; i++) {
						zeroString += "0"
					}
					if ("left".equalsIgnoreCase(fillZeroDirection)) {
						output = zeroString + value
					} else if ("right".equalsIgnoreCase(fillZeroDirection)) {
						output = value + zeroString
					} else {
						throw new RuntimeException(
								"Custom function formatValueByZero: unexpected value '" + fillZeroDirection + "' for the fillDirection")
					}
				}
			}
		}
		if (output == null) {
			output = value
		}
	} else {
		output = value
	}

	return output
}

/**
 * Add MappingContext as an additional argument to read or set Headers and properties.
 * Execution mode: Single value
 *
 * @param header_name Header Name
 * @return Returns 
 */
def String getLocation(String header_name, MappingContext context) {
    def headervalue= context.getHeader(header_name);
    return headervalue;
}