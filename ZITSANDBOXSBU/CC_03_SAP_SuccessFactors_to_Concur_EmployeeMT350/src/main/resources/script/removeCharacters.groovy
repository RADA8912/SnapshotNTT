import com.sap.it.api.mapping.*;

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
