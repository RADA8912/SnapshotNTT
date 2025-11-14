/**
* Groovy Custom Functions Node Extended Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Adds a SUPPRESS.
 * Execution mode: All values of context
 *
 * @param inputNotUsed Input not used
 * @param output Output
 * @param context Mapping Context
 * @return a SUPPRESS.
 */
public def void addSuppress(String[] inputNotUsed, Output output, MappingContext context) {
	output.addSuppress()
}

/**
 * Concats all context values with a linebreak '\n'.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return one context values with linebreaks.
 */
public def void concatContextTextLines(String[] contextValues, Output result) {
	final String LINEBREAK = "\n"
	
	if (contextValues != null && contextValues.length > 0) {
		StringBuffer sb = new StringBuffer(contextValues[0])
			for (int i = 1; i < contextValues.length; i++) {
				sb.append(LINEBREAK).append(contextValues[i])
			}

			result.addValue(sb.toString())
		}
}

/**
 * Create values but suppress first values. For example CSV header lines can suppressed. First argument are context values. Second argument is number of suppressed values.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param numberOfSuppress Number of suppress
 * @param output Output
 * @param context Mapping Context
 * @return values but suppress first values.
 */
public def void createSuppressFirstValues(String[] values, int[] numberOfSuppress, Output output, MappingContext context) {
	// Check number of suppress
	if (numberOfSuppress[0] < 0) {
		throw new IllegalArgumentException("Custom Function createSuppressFirstValues: numberOfSuppress is not set to '0' or greater.")
	}

	try {
		for(int i = 0; i < values.length; i++) {
			// Set first values to SUPPRESS
			if (i <= numberOfSuppress[0] - 1) {
				output.addSuppress()
			} else {
				output.addValue("")
			}
		}
	} catch (Exception e) {
		throw new RuntimeException("Custom Function createSuppressFirstValues: " + e.getMessage(), e)
	}
}

/**
 * Maps every context value to itself when not null, not NIL and not SUPPRESS - maybe the context queue will get shoter.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context values without NIL and SUPPRESS.
 */
public def void deleteNilAndSuppress(String[] contextValues, Output output, MappingContext context) {
	if (contextValues.length > 0) {
		contextValues.each {s->
			if(s !=null && !output.isXSINIL(s) && !output.isSuppress(s)) {
				output.addValue(s)
			}
		}
	}
}

/**
 * Fragments the first argument (a single context value) into several values. The first argument is the value. The second argument is the maximum number of new values.
 * The third argument is the delimiter.
 * Execution mode: All values of context
 *
 * @param value Value
 * @param maxFragmentCount Max fragment count
 * @param delimiter Delimiter
 * @param output Output
 * @param context Mapping Context
 * @return spitted context values.
 */
public def void fragmentSingleValueByDelimiter(String[] value, int[] maxFragmentCount, String[] delimiter, Output output, MappingContext context) {
	// Only took the first value of the queue.

	// Test if there is an input-value.
	if (value!= null && value.length > 0 && value[0] != null && value[0].length() > 0) {
		// Split the string at every delimiter and put it into the ResultList.
		String[] temp = value[0].split(delimiter[0], maxFragmentCount[0])
		for (String s: temp) {
			output.addValue(s)
		}
	}
}

/**
 * Fragments the first argument (a single context value) into context values. The first argument is the value. The second argument is the number of context values.
 * The third argument is the delimiter.
 * Execution mode: All values of context
 *
 * @param value Value
 * @param numberOfContextValues Number of context values
 * @param delimiter Delimiter
 * @param output Output
 * @param context Mapping Context
 * @return spitted context values.
 */
public def void fragmentSingleValueToContextValuesByDelimiter(String[] value, int[] numberOfContextValues, String[] delimiter, Output output, MappingContext context) {
	// Only took the first value of the queue.
	
	// Test if there is an input-value.
	if (value!= null && value.length > 0 && value[0] != null && value[0].length() > 0) {
		// Split the string at every delimiter.
		String[] temp = value[0].split(delimiter[0],-1)
	
		// Took the values and rearrange them into the output.
		int i = 0
		if (numberOfContextValues[0] > 0) {
			while (i < temp.length) {
				output.addValue(temp[i])
				i++
				if (i%numberOfContextValues[0] == 0 && i != temp.length) {
					output.addContextChange()
				}
			}
		}
	}
}

/**
 * Fragments the first argument (a single context value) into context values. The first argument is the value. The second argument is the number of context values.
 * The third argument is the length of the new values.
 * Execution mode: All values of context
 *
 * @param value Value
 * @param numberOfContextValues Number of context values
 * @param length Length
 * @param output Output
 * @param context Mapping Context
 * @return spitted context values.
 */
public def void fragmentSingleValueToContextValuesByLength(String[] value, int[] numberOfContextValues, int[] length, Output output, MappingContext context) {
	// Only took the first value of the queue.

	// Test if there is an input-value.
	if (value!= null && value.length > 0 && value[0] != null && value[0].length() > 0) {

		// Took the value and rearrange it into the ResultList.
		int contextValuesCount = 0
		int inputStringIndex = 0
		if (length[0] > 0 && numberOfContextValues[0] > 0) {
			while (inputStringIndex < value[0].length()){
				if (inputStringIndex + length[0] <= value[0].length()){
					output.addValue(value[0].substring(inputStringIndex, inputStringIndex + length[0]))
				} else {
					output.addValue(value[0].substring(inputStringIndex, value[0].length()))
				}
				inputStringIndex += length[0]
				contextValuesCount++
				if (contextValuesCount%numberOfContextValues[0] == 0 && inputStringIndex < value[0].length()) {
					output.addContextChange()
				}
			}
		}
	}
}

/**
 * Gets context value of index agument. The first argument is queue. The second argument is index number. There is only one index supported.
 * Execution mode: All values of context
 *
 * @param values Context values
 * @param valueIndex Value index
 * @param output Output
 * @param context Mapping Context
 * @return context value of index agument.
 */
public def void getIndexContextValue(String[] values, String[] valueIndex, Output output, MappingContext context) {
	if (values != null && values.length>0) {
		String index = valueIndex[0]
		int indexInt = Integer.parseInt(index)
		
		if (indexInt >= values.length) {
				output.addSuppress()
		} else {
				String str = values[indexInt]
				output.addValue(str)
		}
	}
}

/**
 * Gets the last context value (or output.isSuppress if no such value exits).
 * Execution mode: All values of context
 *
 * @param contectValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return the last context value (or output.isSuppress if no such value exits).
 */
public def void getLastContextValue(String[] contextValues, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"
	
	if (contextValues != null && contextValues.length > 0) {
		String value = SUPPRESS

		for (int i = contextValues.length - 1; i >= 0; i --) {
			String str = contextValues[i]
			if (str != null && !output.isSuppress(str)) {
				value = str
				break
			}
		}
		output.addValue(value)
	}
}

/**
 * Gets multiple values of context.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return multiple values of context.
 */
public def void getMultipleContextValues(String[] contextValues, Output output, MappingContext context) {
	List values = new ArrayList()

	// Check for multiple values
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			if (output.isSuppress(contextValues[i])) {
				// Do nothing on SUPPRESS
			} else if (!values.contains(contextValues[i])) {
				values.add(contextValues[i])
			} else {
				output.addValue(contextValues[i])
			}
		}
	}
}

/**
 * Checks if has all context values 'false'. First argument are context values.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return 'true' if all context values are 'false' or 'false' if not.
 */
public def void hasAllContextValuesFalse(String[] contextValues, Output output, MappingContext context) {
	String status = "true"

	// Check if all context values are 'false'
	try {
		for (int i = 0; i < contextValues.length; i++) {
			if (!contextValues[i].equals("false")) {
				status = "false"
				break
			}
		}

		// Set output
		if (status.equals("false")) {
			output.addValue("false")
		} else {
			output.addValue("true")
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function hasAllContextValuesFalse: " + ex.getMessage(), ex)
	}
}

/**
 * Checks if has all context values same key. First argument are context values. Second argument is key value.
 * No field, empty feld and SUPPRESS gets 'false'.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param key Key
 * @param output Output
 * @param context Mapping Context
 * @return 'true' if all context values are key value or 'false' if not.
 */
public def void hasAllContextValuesKey(String[] contextValues, String[] key, Output output, MappingContext context) {
	String status = "true"

	// Check if all context values has same key
	try {
	    // 'false' if there is no field
		if (contextValues.length == 0) {
			status = "false"
		} else {
		    // Search for non existing keys (SUPPRESS and values gets also 'false')
			for(int i = 0; i < contextValues.length; i++) {
				if (!contextValues[i].equals(key[0])) {
					status = "false"
					break
				}
			}
		}

		// Set result
		if (status.equals("false")) {
			output.addValue("false")
		} else {
			output.addValue("true")
		}
	} catch (Exception ex) {
		throw new Exception("Custom Function hasAllContextValuesKey: " + ex.getMessage(), ex)
	}
}

/**
 * Checks if has all context values 'true'. First argument are context values.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return 'true' if all context values are 'true' or 'false' if not.
 */
public def void hasAllContextValuesTrue(String[] contextValues, Output output, MappingContext context) {
	String status = "true"

	// Check if all context values are 'true'
	try {
		for (int i = 0; i < contextValues.length; i++) {
			if (!contextValues[i].equals("true")) {
				status = "false"
				break
			}
		}

		// Set output
		if (status.equals("false")) {
			output.addValue("false")
		} else {
			output.addValue("true")
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function hasAllContextValuesTrue: " + ex.getMessage(), ex)
	}
}

/**
 * Checks if there are multiple values in context.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return 'true' if context has multiple values or 'false' if not.
 */
public def void hasMultipleContextValues(String[] contextValues, Output output, MappingContext context) {
	List values = new ArrayList()
	String outputValue = "false"

	// Check for multiple values
	if (contextValues != null && contextValues.length > 0 && !outputValue.equals("true")) {
		for (int i = 0; i < contextValues.length; i++) {
			if (output.isSuppress(contextValues[i])) {
				// Do nothing on SUPPRESS
			} else if (!values.contains(contextValues[i])) {
				values.add(contextValues[i])
			} else {
				outputValue = "true"
				break // Leave the loop
			}
		}
	}

	output.addValue(outputValue)
}

/**
 * Gets context value of index agument. The first argument is queue. The second argument is index number. There is only one index supported.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param initialValue Initial value
 * @param increment Increment
 * @param resetIndex Reset index
 * @param output Output
 * @param context Mapping Context
 * @return context value of index agument.
 */
public def void indexIfExists(String[] contextValues, int[] initialValue, int[] increment, String[] resetIndex, Output output, MappingContext context) {
	int index = 0
	index = index + initialValue[0]

	// Check resetIndex (only 'true' is supported)
	if (!resetIndex[0].equalsIgnoreCase("true")) {
		throw new IllegalArgumentException("Custom Function indexIfExists: resetIndex is not set to 'true'. Only 'true' is supported.")
	}

	// Compute context values
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			if (contextValues[i] != null && !output.isSuppress(contextValues[i])) {
				output.addValue(index)
				
				// Add index
				index = index + increment[0]
			} else {
				output.addSuppress()
			}
		}
	}
}

/**
 * Checks if entry is last value and return 'true' otherwise 'false'.
 * Execution mode: All values of context
 *
 * @param values Values
 * @param output Output
 * @param context Mapping Context
 * @return if entry is last value and return 'true' otherwise 'false'.
 */
public def void isLastContextValue(String[] values, Output output, MappingContext context) {
	for (int i = 0; i < values.length; i++) {
		if (output.equals(values[i])) {
			output.addContextChange()
		} else if (i == values.length-1 || output.equals(values[i+1])) {
			output.addValue("true")
		} else {
			output.addValue("false")
		}
	}
}

/**
 * Pass values but can suppress first values. For example header line can suppressed. First argument are context values. Second argument is number of suppressed values.
 * Execution mode: All values of context
 *
 * @param values Values
 * @param numberOfSuppress Number of Suppress
 * @param output Output
 * @param context Mapping Context
 * @return passed values.
 */
public def void passSuppressFirstValues(String[] values, int[] numberOfSuppress, Output output, MappingContext context) {
	// Check number of suppress
	if (numberOfSuppress[0] < 0) {
		throw new IllegalArgumentException("Custom Function passSuppressFirstValues: numberOfSuppress is not set to '0' or greater.")
	}

	try {
		for(int i = 0; i < values.length; i++) {
			// Set first values to SUPPRESS
			if (i <= numberOfSuppress[0] - 1) {
				output.addSuppress()
			} else {
				output.addValue(values[i])
			}
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function passSuppressFirstValues: " + ex.getMessage(), ex)
	}
}

/**
 * Splits context values by number. The first argument are the values in queue. The second argument is number to split.
 * Execution mode: All values of context
 *
 * @param values Values
 * @param numberOfEntries Number of entries
 * @param output Output
 * @param context Mapping Context
 * @return splited context values.
 */
public def void splitByNumber(String[] values, String[] numberOfEntries, Output output, MappingContext context) {
	int counter = 0
	int numberOfEntriesInt = 0

	// Check number
	if (numberOfEntries[0] != null && numberOfEntries[0].length() > 0) {
		try {
			numberOfEntriesInt = Integer.parseInt(numberOfEntries[0])
		} catch (NumberFormatException numberFormatExp) {
			throw new IllegalArgumentException("Custom Function splitByNumber: could not convert number '" + numberOfEntries[0] + "' to integer.")
		}
	}

	if (numberOfEntriesInt < 1) {
		throw new IllegalArgumentException("Custom Function splitByNumber: numberOfEntries must be 1 or greater.")
	}

	// Add context values and context changes
	try {
		for (int i=0; i<values.length; i++) {
			// Add value
			output.addValue(values[i])
			counter++
			
			// Append context change
			if (numberOfEntriesInt == counter && i + 1 != values.length) {
				output.addContextChange()
				// Reset counter
				counter=0
			}
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function splitByNumber: " + ex.getMessage(), ex)
	}
}

/**
 * Splits values by a defined value. The first argument are the values in queue. The second argument is value before split. The third argument is an option to not create split on first occurrence.
 * Execution mode: All values of context
 *
 * @param conditionContextValues Condition context values
 * @param valueForSplit Value for split
 * @param notFirstOccurrence Not first occurrence ('true' or 'false')
 * @param output Output
 * @param context Mapping Context
 * @return splited context values.
 */
public def void splitByValue(String[] values, String[] valueForSplit, String[] notFirstOccurrence, Output output, MappingContext context) {
	String value = ""
	int valueOccurrence = 0

	// Check notFirstOccurrence is 'true' or 'false'
	if (!notFirstOccurrence[0].equalsIgnoreCase("true") && !notFirstOccurrence[0].equalsIgnoreCase("false")) {
		throw new IllegalArgumentException("Custom Function splitByValue: notFirstOccurrence is not set to 'true' or 'false'.")
	}

	// Split values
	try {
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				value = values[i]

				if (value != null && !output.isSuppress(value)) {
					// Check if value for split is equal to value
					if (valueForSplit[0].equals(value)) {
						// Add value occurrence
						valueOccurrence = valueOccurrence + 1

						if ((notFirstOccurrence[0].equalsIgnoreCase("true") && valueOccurrence > 1) || (notFirstOccurrence[0].equalsIgnoreCase("false"))) {
							output.addContextChange()
							output.addValue(value)
						} else {
							output.addValue(value)
						}
					} else {
						output.addValue(value)
					}
				} else {
					output.addSuppress()
				}
			}
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function splitByValue: " + ex.getMessage(), ex)
	}
}