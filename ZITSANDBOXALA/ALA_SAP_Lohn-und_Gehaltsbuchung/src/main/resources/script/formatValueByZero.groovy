/**
* Groovy Custom Functions Utils Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Creates a new string from a non-empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill with "0" (if the fourth says "true") at left or right side (defined by the fifth))
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @param cutLengthDirection Cut length direction 'left' or 'right' end cut
 * @param fillZero Fill zero 'true' = fill
 * @param fillZeroDirection Fill zero direction 'left' or 'right' end fill
 * @return a new string with zeros.
 */
public def String formatValueByZero(String value, String length, String cutLengthDirection, String fillZero, String fillZeroDirection) {
	String output = null

	if (value != null) {
		int lengthInt
		try {
			lengthInt = Integer.parseInt(length)
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function formatValueByZero: length '" + length + "' is not a numeric value.")
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
					throw new RuntimeException("Custom Function formatValueByZero: unexpected value '" + cutLengthDirection + "' for the cutDirection.")
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
						throw new RuntimeException("Custom Function formatValueByZero: unexpected value '" + fillZeroDirection + "' for the fillDirection.")
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