/**
* Groovy Custom Functions Format Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Adds a space.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return a space.
 */
public def String addSpace(String inputNotUsed) {
	// Using Unicode-Escapes
	return "\u00A0"
}

/**
 * Changes algebraic sign minus and empty reverse from value.
 * Execution mode: Single value
 *
 * @param value Value
 * @return changed algebraic sign minus and empty reverse from value.
 */
public def String changeAlgebraicSign(String value) {
	String returnValue = ""

	// Change algebraic sign left '-' to '' and '' to '-'
	if(value.length() == 0) {
		returnValue = ""
	} else if (value.equals("0")) {
		returnValue = value
	} else if (value.startsWith("-")) {
		returnValue = value.substring(1, value.length())
	} else {
		returnValue = "-" + value
	}

	return returnValue
}

/**
 * Gets constant CDATA.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return constant CDATA.
 */
public def String constantCDATA(String inputNotUsed) {
	return "<![CDATA[]]>"
}

/**
 * Creates CDATA in string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return string with CDATA.
 */
public def String createCDATA(String value) {
	String returnValue = ""
	
	returnValue = "<![CDATA[" + value + "]]>"

	return returnValue
}

/**
 * Decodes an URL percent encoding in input string to reserved characters.
 * Execution mode: 
 *
 * @param url URL
 * @return decoded URL.
 */
public def String decodeURL(String url) {
	try {
		String urlDecoded = URLDecoder.decode(url, "utf-8")
		return urlDecoded
	} catch (UnsupportedEncodingException ex) {
		throw new RuntimeException("Custom Function decodeURL: " + ex.getMessage() + ".")
	}
}

/**
 * Encodes an URL reserved characters in input string with percent encoding.
 * Execution mode: 
 *
 * @param url URL
 * @return encoded URL.
 */
public def String encodeURL(String url) {
	try {
		String urlEncoded = URLEncoder.encode(url, "utf-8")
		return urlEncoded
	} catch (UnsupportedEncodingException ex) {
		throw new RuntimeException("Custom Function encodeURL: " + ex.getMessage() + ".")
	}
}

/**
 * Creates a new string from a first argument with length given by the second. Missing characters are filled with whitespaces left.
 * You can use this to create fixed length column values.
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @return a new fixed length string.
 */
public def String fixedLengthBySpaceLeft(String value, int length) {
	String output = ''

	// Check length
	if (length <= 0) {
		throw new RuntimeException("Custom Function fixedLengthBySpaceLeft length must be greater then '0'.")
	}

	int lengthValue = value.length()
	if (lengthValue > length) {
		// Cut left
		output = value.substring(lengthValue - length, lengthValue)
	} else {
		// Create spaces left
		output = value.padLeft(length, ' ')
	}

	return output
}

/**
 * Creates a new string from a first argument with length given by the second. Missing characters are filled with whitespaces right.
 * You can use this to create fixed length column values.
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @return a new fixed length string.
 */
public def String fixedLengthBySpaceRight(String value, int length) {
	String output = ''

	// Check length
	if (length <= 0) {
		throw new RuntimeException("Custom Function fixedLengthBySpaceRight length must be greater then '0'.")
	}

	if (value.length() > length) {
		// Cut rigth
		output = value.substring(0, length)
	} else {
		// Create spaces right
		output = value.padRight(length, ' ')
	}

	return output
}

/**
 * Creates a new string from a first argument with length given by the second. Missing characters are filled with zeros left.
 * You can use this to create fixed length column values.
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @return a new fixed length string.
 */
public def String fixedLengthByZeroLeft(String value, int length) {
	String output = ''

	// Check length
	if (length <= 0) {
		throw new RuntimeException("Custom Function fixedLengthByZeroLeft length must be greater then '0'.")
	}

	int lengthValue = value.length()
	if (lengthValue > length) {
		// Cut left
		output = value.substring(lengthValue - length, lengthValue)
	} else {
		// Create spaces left
		output = value.padLeft(length, '0')
	}

	return output
}

/**
 * Creates a new string from a first argument with length given by the second. Missing characters are filled with zeros right.
 * You can use this to create fixed length column values.
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @return a new fixed length string.
 */
public def String fixedLengthByZeroRight(String value, int length) {
	String output = ''

	// Check length
	if (length <= 0) {
		throw new RuntimeException("Custom Function fixedLengthByZeroRight length must be greater then '0'.")
	}

	if (value.length() > length) {
		// Cut left
		output = value.substring(0, length)
	} else {
		// Create spaces left
		output = value.padRight(length, '0')
	}

	return output
}

/**
 * Formats UU ID. First argument is UU ID.
 * Execution mode: Single value
 *
 * @param uuid UU ID
 * @return formated UU ID.
 */
public def String formatUUID(String uuid) {
	String returnValue = ""
	
	if (uuid != null && !uuid.equals("")) {
		returnValue = (uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32))
	}
	
	return returnValue
}

/**
 * Creates a new string from a non empty first argument with length given by the second as follows: cut at left or right end (the third argument) or fill (if the fourth says 'true') at left or right side (defined by the fifth).
 * Execution mode: Single value
 *
 * @param value Value
 * @param length Length
 * @param cutLengthDirection Cut length direction
 * @param fillSpace Fill space
 * @param fillSpaceDirection Fill space direction
 * @return a new string from a non empty.
 */
public def String formatValueBySpace(String value, String length, String cutLengthDirection, String fillSpace, String fillSpaceDirection) {
	String output = null
	if (value != null) {
		int lengthInt
		try {
			lengthInt = Integer.parseInt(length)
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function formatValueBySpace: length '" + length + "' is not a numeric value.")
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
					throw new RuntimeException("Custom Function formatValueBySpace: unexpected value '" + cutLengthDirection + "' for the cutDirection")
				}
			} else {
				if ("true".equalsIgnoreCase(fillSpace)) {
					// now lengthValue < lengthInt
					int offset = lengthInt - lengthValue
					String SpaceString = "\u00A0"
					for (int i = 0; i < offset - 1; i++) {
						SpaceString += "\u00A0"
					}
					if ("left".equalsIgnoreCase(fillSpaceDirection)) {
						output = SpaceString + value
					} else if ("right".equalsIgnoreCase(fillSpaceDirection)) {
						output = value + SpaceString
					} else {
						throw new RuntimeException("Custom Function formatValueBySpace: unexpected value '" + fillSpaceDirection + "' for the fillDirection")
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
 * Escapes signs in string by using unicode-escapes.
 * Execution mode: Single value
 *
 * @param value Value
 * @return escaped string.
 */
public def String getEscapeUnicode(String value) {
    def output = ""
    output = org.apache.commons.lang.StringEscapeUtils.escapeJava(value)
    
	return output
}

/**
 * Escapes signs in string by using XML-escapes.
 * Execution mode: Single value
 *
 * @param value Value
 * @return escaped string.
 */
public def String getEscapeXml(String value) {
	String returnValue = groovy.xml.XmlUtil.escapeXml(value)
	return returnValue
}

/**
 * Unescapes signs in string by using unicode-escapes.
 * Execution mode: Single value
 *
 * @param value Value
 * @return unescaped string.
 */
public def String getUnescapeUnicode(String value) {
    def output = ""
    output = org.apache.commons.lang.StringEscapeUtils.unescapeJava(value)
    
	return output
}

/**
 * Unescapes signs in string by using XML-escapes.
 * Execution mode: Single value
 *
 * @param value Value
 * @return unescaped string.
 */
public def String getUnescapeXml(String value) {
    String xml = "<foo>${value}</foo>"
    String returnValue = new XmlSlurper().parseText(xml)
	return returnValue.toString()
}

/**
 * Inserts a decimal point at given decimalCount if possible.
 * Execution mode: Single value
 *
 * @param number Number
 * @param decimalCount Decimal count
 * @return the formatted number.
 */
public def String insertDecimal(String number, String decimalCount) {
	final String SUPPRESS = "_sUpPresSeD_"
	String result = number.trim()
	if (result.length() == 0) {
		return SUPPRESS
	}

	int decimalCountInt = 0
	try {
		result = Long.parseLong(result) + ""
	} catch (NumberFormatException numberFormatExp) {
		throw new RuntimeException("Custom Function insertDecimal: could not convert '" + number + "' to long.")
	}

	try {
		decimalCountInt = Integer.parseInt(decimalCount)
	} catch (NumberFormatException numberFormatExp) {
		throw new RuntimeException("Custom Function insertDecimal: could not convert '" + decimalCount + "' to integer.")
	}
	while (result.length() <= decimalCountInt) {
		result = "0" + result
	}

	int resultLength = result.length()
	return result.substring(0, resultLength - decimalCountInt) + "." + result.substring(resultLength - decimalCountInt, resultLength)
}

/**
 * Insert double quotes ahead and behind value. First Argument is value.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value with double quotes ahead and behind.
 */
public def String insertDoubleQuotes(String value) {
	final String DOUBLE_QUOTE = "\""
	String returnValue = ""

	// Concate doubleQuote with input value and assign to returnValue
	returnValue = DOUBLE_QUOTE.concat(value).concat(DOUBLE_QUOTE)

	return returnValue
}

/**
 * Insert single quotes ahead and behind value. First Argument is value.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value with single quotes ahead and behind.
 */
public def String insertSingleQuotes(String value) {
	final String SINGLE_QUOTE = "\'"
	String returnValue = ""

	// Concate singleQuote with input value and assign to returnValue
	returnValue = SINGLE_QUOTE.concat(value).concat(SINGLE_QUOTE)

	return returnValue
}

/**
 * Removes algebraic sign plus and minus from value.
 * Execution mode: Single value
 *
 * @param value Value
 * @return string without algebraic sign plus and minus.
 */
public def String removeAlgebraicSign(String value) {
	String returnValue = ""

	// Check value
	if(value.length() == 0) {
		returnValue = ""
	} else if (value.startsWith("-")) {
		returnValue = value.substring(1, value.length())
	} else if (value.startsWith("+")) {
		returnValue = value.substring(1, value.length())
	} else if (value.endsWith("-")) {
		returnValue = value.substring(0, value.length() - 1)
	} else {
		returnValue = value
	}

	return returnValue
}

/**
 * Removes only algebraic sign plus from value.
 * Execution mode: Single value
 *
 * @param value Value
 * @return string without algebraic sign plus.
 */
public def String removeAlgebraicSignPlus(String value) {
	String returnValue = ""

	// Check value
	if(value.length() == 0){
		returnValue = ""
	} else if (value.startsWith("+")) {
		returnValue = value.substring(1, value.length())
	} else {
		returnValue = value
	}

	return returnValue
}

/**
 * Remove all characters from string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return string without characters.
 */
public def String removeAllCharacters(String value) {
	String returnValue = ""

	if (value != null) {
		// Use RegEx for remove all characters
		// Using Unicode-Escapes
		// ö = \u00f6
		// ä = \u00e4
		// ü = \u00fc
		// ß = \u00df
		// Ö = \u00d6
		// Ä = \u00c4
		// Ü = \u00dc
		returnValue = value.replaceAll("[a-zA-\u00f6\u00e4\u00fc\u00df\u00d6\u00c4\u00dc]","")
	}

	return returnValue
}

/**
 * Remove all spaces from string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value without spaces.
 */
public def String removeAllSpaces(String value) {
	// Remove all spaces from string.
	String returnValue = ""
	
	if(value != null) {
		// Use RegEx for remove all spaces
		returnValue = value.replaceAll(" ","")
	}
	
	return returnValue
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
		returnValue = value.replaceAll("[^a-zA-Z0-9/.,: _+-]+","")
	}

	return returnValue
}

/**
 * Remove carriage return line feed.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value without carriage return line feed.
 */
public def String removeCarriageReturnLineFeed(String value) {
	String returnValue = ""

	if(value != null) {
		// Use RegEx for remove carriage return line feed
		returnValue = value.replaceAll("[\\r\\n]","")
	}

	return returnValue
}

/**
 * Removes CDATA in string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return string without CDATA.
 */
public def String removeCDATA(String value) {
	String returnValue = ""

	returnValue = value.replaceAll("[&lt;!\\[CDATA\\[.*?\\]\\]&gt;]", "")
	returnValue = returnValue.replaceAll("[<!\\[CDATA\\[.*?\\]\\]>]", "")

	return returnValue
}

/**
 * Removes decimal if zero. The dot is used as decimal separator.
 * Execution mode: Single value
 *
 * @param number Number
 * @return value without decimal if zero.
 */
public def String removeDecimalIfZero(String number) {
	String firstPart = ""
	String secondPart = ""
	int indexOfDelimiter = 0
	String returnValue = ""

	if (number != null) {
		indexOfDelimiter = number.indexOf(".")

		if (indexOfDelimiter == -1) {
			returnValue = number
		} else {
			firstPart = number.substring(0, indexOfDelimiter)
			secondPart = number.substring(indexOfDelimiter + 1)

			if (new Integer(secondPart).intValue() == 0) {
				returnValue = firstPart
			} else {
				returnValue = number
			}
		}
	}

	return returnValue
}

/**
 * Replaces special characters in string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return string without special characters.
 */
public def String replaceSpecialCharacters(String value) {
	// Function using replaceAll RegEx syntacs. Please do not use RegEx computing signs in replacements.
	// In CPI need to use Using Unicode-Escapes.
	Map umlautMap = new HashMap()
	String output = value

	// Replacements
	// Using Unicode-Escapes
	umlautMap.put("\u00C0", "A")	// À
	umlautMap.put("\u00C1", "A")	// Á
	umlautMap.put("\u00C2", "A")	// Â
	umlautMap.put("\u00C3", "A")	// Ã
	umlautMap.put("\u00C4", "Ae")	// Ä
	umlautMap.put("\u00C5", "A")	// Å
	umlautMap.put("\u00C6", "A")	// Æ
	umlautMap.put("\u00C7", "C")	// Ç
	umlautMap.put("\u00D0", "D")	// Ð
	umlautMap.put("\u00C8", "E")	// È
	umlautMap.put("\u00C9", "E")	// É
	umlautMap.put("\u00CA", "E")	// Ê
	umlautMap.put("\u00CB", "E")	// Ë
	umlautMap.put("\u0191", "F")	// Ƒ
	umlautMap.put("\u00CC", "I")	// Ì
	umlautMap.put("\u00CD", "I")	// Í
	umlautMap.put("\u00CE", "I")	// Î
	umlautMap.put("\u00CF", "I")	// Ï
	umlautMap.put("\u00D1", "N")	// Ñ
	umlautMap.put("\u00D2", "O")	// Ò
	umlautMap.put("\u00D3", "O")	// Ó
	umlautMap.put("\u00D4", "O")	// Ô
	umlautMap.put("\u00D5", "O")	// Õ
	umlautMap.put("\u00D6", "Oe")	// Ö
	umlautMap.put("\u00D8", "O")	// Ø
	umlautMap.put("\u0152", "OE")	// Œ
	umlautMap.put("\u0160", "S")	// Š
	umlautMap.put("\u015E", "S")	// Ş
	umlautMap.put("\u00D9", "U")	// Ù
	umlautMap.put("\u00DA", "U")	// Ú
	umlautMap.put("\u00DB", "U")	// Û
	umlautMap.put("\u00DC", "Ue")	// Ü
	umlautMap.put("\u0178", "Y")	// Ÿ
	umlautMap.put("\u00DD", "Y")	// Ý
	umlautMap.put("\u017D", "Z")	// Ž
	umlautMap.put("\u00E0", "a")	// à
	umlautMap.put("\u00E1", "a")	// á
	umlautMap.put("\u00E2", "a")	// â
	umlautMap.put("\u00E3", "a")	// ã
	umlautMap.put("\u00e4", "ae")	// ä
	umlautMap.put("\u00E5", "a")	// å
	umlautMap.put("\u00E6", "ae")	// æ
	umlautMap.put("\u00E7", "c")	// ç
	umlautMap.put("\u00F0", "d")	// ð
	umlautMap.put("\u00E8", "e")	// è
	umlautMap.put("\u00E9", "e")	// é
	umlautMap.put("\u00EA", "e")	// ê
	umlautMap.put("\u00EB", "e")	// ë
	umlautMap.put("\u0192", "f")	// ƒ
	umlautMap.put("\u00EC", "i")	// ì
	umlautMap.put("\u00ED", "i")	// í
	umlautMap.put("\u00EE", "i")	// î
	umlautMap.put("\u00EF", "i")	// ï
	umlautMap.put("\u00F1", "n")	// ñ
	umlautMap.put("\u00F2", "o")	// ò
	umlautMap.put("\u00F3", "o")	// ó
	umlautMap.put("\u00F4", "o")	// ô
	umlautMap.put("\u00F5", "o")	// õ
	umlautMap.put("\u00F6", "oe")	// ö
	umlautMap.put("\u00F8", "o")	// ø
	umlautMap.put("\u0153", "oe")	// œ
	umlautMap.put("\u0161", "s")	// š
	umlautMap.put("\u015F", "s")	// ş
	umlautMap.put("\u00F9", "u")	// ù
	umlautMap.put("\u00FA", "u")	// ú
	umlautMap.put("\u00FB", "u")	// û
	umlautMap.put("\u00FC", "ue")	// ü
	umlautMap.put("\u00FF", "y")	// ÿ
	umlautMap.put("\u00FD", "y")	// ý
	umlautMap.put("\u017E", "z")	// ž
	umlautMap.put("\u00DF", "ss")	// ß
	umlautMap.put("\u00AE", "(R)")	// ®
	umlautMap.put("\u00A9", "(C)")	// ©
	umlautMap.put("\u00B1", "+-")	// 
	umlautMap.put("\u00B2", "^2")	// ²
	umlautMap.put("\u00B3", "^3")	// ³
	umlautMap.put("\u00B4", "'")	// ´
	umlautMap.put("\u0060", "'")	// `
	umlautMap.put("\u00B5", "^10−6")// µ
	umlautMap.put("\u00B0","grade")	// °
	umlautMap.put("\u2122","TM")	// ™

	// Replace values in map with RegEx
	Set entrySet = umlautMap.entrySet()
	for (Iterator it = entrySet.iterator(); it.hasNext();) {
		Map.Entry mapEntry = (Map.Entry) it.next()
		
		output = output.replaceAll("[" + (String) mapEntry.getKey() + "]",
				(String) mapEntry.getValue())
	}

	return output
}

/**
 * Replaces all umlauts in string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value without umlauts.
 */
public def String replaceUmlauts(String value) {
	String[][] chartable= new String[7][2]
	String returnValue = ""

	// Using Unicode-Escapes
	chartable[0][0]="\u00f6" // ö
	chartable[1][0]="\u00e4" // ä
	chartable[2][0]="\u00fc" // ü
	chartable[3][0]="\u00df" // ß
	chartable[4][0]="\u00d6" // Ö
	chartable[5][0]="\u00c4" // Ä
	chartable[6][0]="\u00dc" // Ü

	chartable[0][1]="oe"
	chartable[1][1]="ae"
	chartable[2][1]="ue"
	chartable[3][1]="ss"
	chartable[4][1]="Oe"
	chartable[5][1]="Ae"
	chartable[6][1]="Ue"

	for (int i=0;i<chartable.length;i++) {
		value = value.replaceAll(chartable[i][0],chartable[i][1])
	}
	returnValue = value
	
	return returnValue
}

/**
 * Sets decimal separator to point and remove thousands separator.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value with decimal separator point witout thousands separator.
 */
public def String setDecimalSeparatorPoint(String value) {
	// Remove whitespaces
	String returnValue = value.replaceAll("\\s","")

	// Remove multiple comma thousands separators
	if(returnValue.indexOf(",") > -1 && returnValue.lastIndexOf(",") < returnValue.lastIndexOf(".")) {
		returnValue = returnValue.replaceAll(",","")
	}

	// Remove multiple point thousands separator only if comma is decimal separator
	if(returnValue.indexOf(".") > -1 && returnValue.lastIndexOf(".") < returnValue.lastIndexOf(",")) {
		returnValue = returnValue.replaceAll("\\.","")
	}

	// Replace comma to point
	returnValue = returnValue.replaceAll(",",".")

	return returnValue
}

/**
 * Splitts street and number.
 * Execution mode: All values of context
 *
 * @param streetAndNumber Street and number
 * @param outputStreet Output street
 * @param outputNumber Output number
 * @param context Mapping Context 
 * @return street and number in separate outputs.
 */
public def void splittToStreetAndNumber(String[] streetAndNumber, Output outputStreet, Output outputNumber, MappingContext context) {
	String streetAndNumberEntry = ""
	char nCharacter
	String substringStreet = ""
	String substringNumber = ""
	int firstNumber = 0
	int endStreet = 0

	try {
		// Check all entries
		for (int i = 0; i < streetAndNumber.length; i++) {
			streetAndNumberEntry = streetAndNumber[i]
			firstNumber = 0
		
			//Check length of input
			if (streetAndNumberEntry.length() > 0 && !outputStreet.isSuppress(streetAndNumberEntry)) {
				// Find first numeric sign in string
				for (int n = 0; n < streetAndNumberEntry.length(); n++) {
					nCharacter = streetAndNumberEntry.charAt(n)

					if (nCharacter == '1') {
						firstNumber = n
						break
					} else if (nCharacter == '2') {
						firstNumber = n
						break
					} else if (nCharacter == '3') {
						firstNumber = n
						break
					} else if (nCharacter == '4') {
						firstNumber = n
						break
					} else if (nCharacter == '5') {
						firstNumber = n
						break
					} else if (nCharacter == '6') {
						firstNumber = n
						break
					} else if (nCharacter == '7') {
						firstNumber = n
						break
					} else if (nCharacter == '8') {
						firstNumber = n
						break
					} else if (nCharacter == '9') {
						firstNumber = n
						break
					} else if (nCharacter == '0') {
						firstNumber = n
						break
					}
				}

				// If there is no number in value (firstNumber = 0)
				if (firstNumber < 1) {
					firstNumber = streetAndNumberEntry.length()
				}
				
				// Get street
				if (streetAndNumberEntry.charAt(firstNumber - 1) == ' ') {
					endStreet = firstNumber - 1
				} else {
					endStreet = firstNumber
				}
				substringStreet = "" + streetAndNumberEntry.substring(0, endStreet)

				// Get number
				int startNumber = firstNumber
				int endNumber = streetAndNumberEntry.length()	
				substringNumber = "" + streetAndNumberEntry.substring(startNumber, endNumber)
				
				// Set output		
				outputStreet.addValue(substringStreet)
				outputNumber.addValue(substringNumber)
			} else if (outputStreet.isSuppress(streetAndNumberEntry)) {
				// Set output		
				outputStreet.addSuppress()
				outputNumber.addSuppress()
			} else {
				// Set output		
				outputStreet.addValue("")
				outputNumber.addValue("")
			}
		}
	} catch (Exception e) {
		throw new RuntimeException("Custom Function splittToStreetAndNumber: " + e.getMessage(), e)
	}
}

/**
 * Remove leading and tailing whitespaces from string.
 * Execution mode: Single value
 *
 * @param value Value
 * @return value without leading and tailing whitespaces.
 */
public def String stripSpaces(String value) {
	// Remove leading and tailing whitespaces from string.
	String returnValue = ""

	if(value != null) {
		// Use RegEx for remove whitespaces
		returnValue = value.replaceAll('^\\s+|\\s+$', '')
	}

	return returnValue
}