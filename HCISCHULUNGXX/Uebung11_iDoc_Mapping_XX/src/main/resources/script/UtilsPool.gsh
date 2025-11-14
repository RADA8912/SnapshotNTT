/**
* Custom Functions Utils Pool
*
* @author itelligence.de
* @version 1.0.0
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
 * Returns "true" if the first argument is not empty; "false" otherwise.
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns "true" if the first argument is not empty; "false" otherwise.
 */
public def String hasValue(String value) {
	String output
	
	if (value != null) {
		outputTemp = value
		if (value.trim().length() > 0) {
			output = "true"
		} else {
			output = "false"
		}
	} else {
		output = value
	}

	return output
}

/**
 * Returns "true" when the first argument has one of the values passed as a constant in the second argument (separated by a semicolon); "false" otherwise.
 * Execution mode: Single value
 *
 * @param value Value
 * @param suchValuesString Such values string
 * @return Returns "true" when the first argument has one of the values passed as a constant in the second argument (separated by a semicolon); "false" otherwise
 */
public String hasOneOfSuchValues(String value, String suchValuesString) {
	String output
	if (value != null) {
		if (suchValuesString == null) {
			throw new IllegalStateException("Custom Function hasOneOfSuchValues: there is no suchValuesString")
		}
		output = "false"
		String[] suchValues = suchValuesString.split(";")
		for (int i = 0; i < suchValues.length; i++) {
			if (suchValues[i].equalsIgnoreCase(value)) {
				output = "true"
				break
			}
		}
	} else {
		output = value
	}

	return output
}

/**
 * Removes the trailing characters of the first argument leaving the number of characters given by the second argument. Attention: white spaces are significant, no trim is in action.
 * Execution mode: Single value
 *
 * @param value Value
 * @param headLength Head length
 * @return Returns head string of input value.
 */
public def String headString(String value, String headLength){
	String output = null
	if (value != null && headLength != null && headLength.length() > 0) {
		int headLengthInt
		try {
			headLengthInt = Integer.parseInt(headLength)
		} catch (NumberFormatException numberFormatExp) {
			throw new RuntimeException("Custom function headString: could not convert headLength '" + headLength + "' to integer")
		}

		// DO NOT trim: in some cases the trailing whitespaces may be significant
		int length = value.length()
		if (length > headLengthInt) {
			output = value.substring(0, headLengthInt)
		} else {
			output = value
		}
	} else {
		output = value
	}

	return output
}

/**
 * Returns "true" if the argument is a number, "false" otherwise.
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns "true" if the argument is a number, "false" otherwise.
 */
public def String isNumber(String value) {
	String output
	if (value != null) {
		try {
			Double.parseDouble(value.trim())
			output = "true"
		} catch (Exception numExp) {
			output = "false"
		}
	} else {
		output = value
	}

	return output
}

/**
 * Returns "true" if the argument is the number zero, "false" otherwise.
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns "true" if the argument is the number zero, "false" otherwise.
 */
public def String isNumberEqualZero(String value) {
	String output = null
	if (value != null) {
		try {
			output = Boolean.toString(Double.parseDouble(value.trim()) == 0.0)
		} catch (Exception numExp) {
			output = "false"
		}
	} else {
		output = value
	}

	return output
}

/**
 * Returns "true" if the argument is a number but not zero, "false" otherwise.
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns "true" if the argument is a number but not zero, "false" otherwise.
 */
public def String isNumberNotEqualZero(String value) {
	String output = null
	if (value != null) {
		double dd = 0
		try {
			  dd = Double.parseDouble(value.trim())
		} catch (Exception numExp) {
			  output = "false"
		} finally {
			  if (output == null) {
				  output = Boolean.toString(dd != 0.0)
			  }
		}
	} else {
		output = value
	}

	return output
}

/**
 * Sets the minus sign from the beginning to the end.
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns value with the minus at the end.
 */
public def String minusFromBeginToEnd(String value) {
	String output = ""
	
	if (value != null && value.startsWith("-")) {
		output = value.substring(1, value.length()) + "-"
	} else {
	output = value
	}

	return output
}

/**
 * Sets the minus sign from the end to the beginning.
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns value with the minus at the beginning.
 */
public def String minusFromEndToBegin(String value) {
	String output

	if (value != null && value.endsWith("-")) {
		output = "-" + value.substring(0, value.length() - 1)
	} else {
		output = value
	}

	return output
}

/**
 * Splits the first argument by a delimiter (third argument) and returns the partial string requested by the second argument (or an empty string if no such partial string exists)
 * Execution mode: Single value
 *
 * @param value Value
 * @param index Index
 * @param delimiter Delimiter
 * 
 * @return Returns value with the minus at the beginning.
 */
public def String splitToIndex(String value, String index, String delimiter) {
	String output = null
	
	if (value != null) {
		int ind = 0
		try {
			ind = Integer.parseInt(index)
		} catch (Exception ex) {
			throw new RuntimeException("Custom function splitToIndex: index '" + index + "' is not a numeric value.")
		}
		String[] splits = value.split(delimiter)
		if (splits.length > ind) {
			output = splits[ind]
		} else {
			output = ""
		}
	} else {
		output = value
	}
	
	return output
}

/**
 * Removes the leading characters of the first argument leaving the number of characters given by the second argument. Leading or trailing white spaces do not count.
 * Execution mode: Single value
 *
 * @param value Value
 * @param tailLength Tail length
 * @return Returns tail string of input value.
 */
public def String tailString(String value, String tailLength) {
	String output = null
	if (value != null && tailLength != null
			&& tailLength.trim().length() > 0) {
		int tailLengthInt
		try {
			tailLengthInt = Integer.parseInt(tailLength)
		} catch (NumberFormatException numberFormatExp) {
			throw new RuntimeException("Custom function tailString: could not convert tailLength '" + tailLength + "' to integer")
		}
		
		// DO NOT trim as usual: in some cases the trailing white spaces may be significant
		String trimmedValue = ""
		int valueLength = value.length()
		for (int i = valueLength; i > 0; i--) {
			if (value.charAt(i - 1) != ' ') {
				trimmedValue = value.substring(0, i)
				break
			}
		}

		int length = trimmedValue.length()
		if (length > tailLengthInt) {
			output = trimmedValue.substring(length - tailLengthInt)
		} else {
			output = value
		}
	} else {
		output = value
	}

	return output
}

/**
 * Formats a string like a number (removes + sign and leading/trailing zeros).
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns a number.
 */
public def String toNumber(String numberString) {
	if (numberString != null && numberString.trim().length() == 0) {
		return "_sUpPresSeD_"
	}
	try {
		return new BigDecimal(numberString).toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom function toNumber: can not transform numberString '" + numberString + "' to a number")
	}
}

/**
 * Removes trailing white spaces (leading white spaces may be significant).
 * Execution mode: Single value
 *
 * @param value Value
 * @return Returns input value without trailing white spaces.
 */
public def String trimRight(String value) {
	String output = null
	int length = value.length()
	
	if (length > 0) {
		char[] chars = value.toCharArray()
		int trailing = length - 1
		while (trailing > -1 && chars[trailing] == ' ') {
			trailing--
		}
		output = value.substring(0, trailing + 1)
	} else {
		output = value
	}

	return output
}

/**
 * Removes leading zeros.
 * Execution mode: Single value 
 *
 * @param value Value
 * @return Returns input number without leading zeros.
 */
public def String trimZeroLeft(String value) {
	String output = ""

	if (value != null) {
		if (value.trim().length() == 0) {
			output = value
		} else {
			output = value.replaceAll("^0*", "")
			if (output.trim().length() == 0) {
				output = "0"
			}
		}
	} else {
		output = value
	}

	return output
}