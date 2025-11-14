/**
* Groovy Custom Functions Regex Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*
import java.util.regex.*

/**
 * Checking if any subset of the input string fulfill regular expression pattern. First argument 'value' is following by second argument 'regex'.
 * Execution mode: Single value
 *
 * @param value Value
 * @param regex Regex
 * @return 'true' or 'false'.
 */
public def String findRegex(String value, String regex) {
	Pattern pat = Pattern.compile(regex)
	Matcher m = pat.matcher(value)

	return "" + m.find()
}

/**
 * Checking if string fulfill regular expression pattern. First argument 'value' is following by second argument 'regex'.
 * Execution mode: Single value
 *
 * @param value Value
 * @param regex Regex
 * @return 'true' or 'false'.
 */
public def String isRegex(String value, String regex) {
	return "" + Pattern.matches(regex, value)
}

/**
 * Replacing all substrings which fulfill regular expression pattern. First argument 'value' is following by second argument 'regex', third argument is 'substring'.
 * Execution mode: Single value
 *
 * @param value Value
 * @param regex Regex
 * @param subString Substring
 * @return a new string with replaced substring.
 */
public def String replaceRegexString(String value, String regex, String subString) {
	Pattern pat = Pattern.compile(regex)
	Matcher m = pat.matcher(value)

	return m.replaceAll(subString)
}

/**
 * Return string from first argument 'value' following by second argument 'regex'.
 * Execution mode: Single value
 *
 * @param value Value
 * @param regex Regex
 * @return a substring.
 */
public def String returnRegex(String value, String regex) {
	final String SUPPRESS = "_sUpPresSeD_"

	Pattern pat = Pattern.compile(regex)
	Matcher m = pat.matcher(value)

	if (m.find()) {
		return m.group()
	} else {
		return SUPPRESS
	}
}

/**
 * Splits string with regex to separated values. First argument 'value' is following by second argument 'regex'.
 * Execution mode: Single value
 *
 * @param value Value
 * @param regex Regex
 * @param output Output
 * @param context Mapping Context
 * @return splited values.
 */
public def void splitRegexString(String[] values, String[] regex, Output output, MappingContext context) {
	if (values == null)
		return

	for (int i=0; i<values.length; i++) {
		String[] splits = values[i].split(regex[0])

		for (int j=0; j<splits.length; j++) {
			output.addValue(splits[j])
		}
	}
}