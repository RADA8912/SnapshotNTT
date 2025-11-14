/**
* Groovy Custom Functions Node Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*

/**
 * Passes a value from the third argument when the corresponding value from first argument has one of the value contained in the second value (these conditions are separated by a semicolon), passes SUPPRESS otherwise.
 * Execution mode: All values of context
 *
 * @param conditionContextValues Condition context values
 * @param suchValuesString Such values string
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return a value from the third argument when the corresponding value from first argument has one of the value contained.
 */
public def void assignValueByCondition(String[] conditionContextValues, String[] suchValuesString, String[] contextValues, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"

	// A short form of the standard's IfWithoutElse
	if (suchValuesString == null || suchValuesString.length == 0) {
		throw new IllegalStateException("Custom Function assignValueByCondition: there is no suchValuesString.")
	}

	// Action when one of the contexts have values
	if (conditionContextValues != null
			&& conditionContextValues.length > 0 || (contextValues != null
			&& contextValues.length > 0)) {
		// Forbidden: both contexts have content but different number of value
		if (contextValues != null && contextValues.length > 0
				&& conditionContextValues != null
				&& conditionContextValues.length > 0
				&& contextValues.length != conditionContextValues.length) {
			throw new IllegalStateException("Custom Function assignValueByCondition: conditionContextValues and contextValues have different lengths.")
		}

		if (conditionContextValues == null
				|| conditionContextValues.length == 0
				|| contextValues == null || contextValues.length == 0) {
			// Allowed one context has content the other hasn't
			output.addSuppress()
		} else {
			String[] conditions = suchValuesString[0].split(";")
			for (int i = 0; i < conditionContextValues.length; i++) {
				String value = SUPPRESS
				for (int j = 0; j < conditions.length; j++) {
					if (conditions[j].equalsIgnoreCase(conditionContextValues[i])) {
						value = contextValues[i]
						break // leave the inner loop
					}
				}
				output.addValue(value)
			}
		}
	}
}

/**
 * Splits context in first argument into blocks defined in size by third argument and returns value at index given by second argument. Returns SUPPRESS when context is empty.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param blockSize BlockSize
 * @param indexInt Index
 * @param output Output
 * @param context Context
 * @return splited context.
 */
public def void buildBlocksAndGetValueByIndex001(String[] contextValues, String[] index, String[] blockSize, Output output, MappingContext context) {
	int contextValueLength = contextValues.length
	if (contextValueLength > 0) {
		int blockSizeInt = 0
		try {
			blockSizeInt = Integer.parseInt(blockSize[0])
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function buildBlocksAndGetValueByIndex001: '" + blockSize[0] + "' isn't an int value.")
		}
		int indexInt = 0
		try {
			indexInt = Integer.parseInt(index[0])
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function buildBlocksAndGetValueByIndex001: '" + index[0] + "' isn't an index value.")
		}
		
		if (indexInt >= blockSizeInt) {
			throw new RuntimeException("Custom Function buildBlocksAndGetValueByIndex001: index '" + index[0] + "' doesn't fit the blocksize '" + blockSize[0] + "'.")
		}

		for (int i = indexInt; i < contextValueLength; i += blockSizeInt) {
			output.addValue(contextValues[i])
		}

		int mod = contextValueLength % blockSizeInt
		if (mod != 0 && indexInt >= mod) {
			output.addSuppress()
		}
	}
}

/**
 * Concatenates all values of a context seperated by second argument.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param delimiterString Delimiter string
 * @param output Output
 * @param context Mapping Context
 * @return one string value.
 */
public def void concatContextValues(String[] contextValues, String[] delimiterString, Output output, MappingContext context) {
	if (delimiterString == null || delimiterString.length == 0) {
		throw new IllegalStateException("Custom Function concatContextValues: there is no delimiterString.")
	}

	if (contextValues != null && contextValues.length > 0) {
		String delimiter = delimiterString[0]
		StringBuffer sb = new StringBuffer(contextValues[0])
		for (int i = 1; i < contextValues.length; i++) {
			sb.append(delimiter).append(contextValues[i])
		}

		output.addValue(sb.toString())
	}
}

/**
 * Puts all arguments sequentially into one queue.
 * Execution mode: All values of context
 *
 * @param queue1Values Queue 1 values
 * @param queue2Values Queue 2 values
 * @param queue3Values Queue 3 values
 * @param queue4Values Queue 4 values
 * @param queue5Values Queue 5 values
 * @param output Output
 * @param context Context
 * @return a context contains all input arguments.
 */
public def void concatToOneQueue(String[] queue1Values, String[] queue2Values, String[] queue3Values, String[] queue4Values, String[] queue5Values, Output output, MappingContext context) {
	if (queue1Values != null && queue1Values.length > 0) {
		for (int i = 0; i < queue1Values.length; i++) {
			output.addValue(queue1Values[i])
		}
	} else {
		output.addSuppress()
	}
	if (queue2Values != null && queue2Values.length > 0) {
		for (int i = 0; i < queue2Values.length; i++) {
			output.addValue(queue2Values[i])
		}
	} else {
		output.addSuppress()
	}
	if (queue3Values != null && queue3Values.length > 0) {
		for (int i = 0; i < queue3Values.length; i++) {
			output.addValue(queue3Values[i])
		}
	} else {
		output.addSuppress()
	}
	if (queue4Values != null && queue4Values.length > 0) {
		for (int i = 0; i < queue4Values.length; i++) {
			output.addValue(queue4Values[i])
		}
	} else {
		output.addSuppress()
	}
	if (queue5Values != null && queue5Values.length > 0) {
		for (int i = 0; i < queue5Values.length; i++) {
			output.addValue(queue5Values[i])
		}
	} else {
		output.addSuppress()
	}
}

/**
 * Puts all arguments sequentially into one queue.
 * Execution mode: All values of context
 *
 * @param queue1Values Queue 1 values
 * @param queue2Values Queue 2 values
 * @param output Output
 * @param context Context
 * @return a context contains both input arguments.
 */
public def void concatTwoQueuesToOne(String[] queue1Values, String[] queue2Values, Output output, MappingContext context) {
	if (queue1Values != null && queue1Values.length > 0) {
		for (int i = 0; i < queue1Values.length; i++) {
			output.addValue(queue1Values[i])
		}
	}
	if (queue2Values != null && queue2Values.length > 0) {
		for (int i = 0; i < queue2Values.length; i++) {
			output.addValue(queue2Values[i])
		}
	}
}

/**
 * Produces a single 'true' if the first argument (a context queue) contains one of the values passed as a constant in the second argument (separated by a semicolon), 'false' otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param suchValuesString Such values string
 * @param output Output
 * @param context Mapping Context
 * @return boolean value.
 */
public def void contextHasOneOfSuchValues(String[] contextValues, String[] suchValuesString, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		if (suchValuesString == null || suchValuesString.length == 0 || suchValuesString[0] == null) {
			throw new IllegalStateException("Custom Function contextHasOneOfSuchValues: there is no suchValuesString.")
		}
		String[] suchValues = suchValuesString[0].split(";")

		String oneOfSuchValues = "false"
		fcontext: for (int i = 0; i < contextValues.length; i++) {
			for (int j = 0; j < suchValues.length; j++) {
				if (suchValues[j].equalsIgnoreCase(contextValues[i])) {
					oneOfSuchValues = "true"
					break fcontext
				}
			}
		}

		output.addValue(oneOfSuchValues)
	} else {
		output.addValue("false")
	}
}

/**
 * Splits context in first argument into blocks defined in size by second argument. Returns SUPPRESS when context is empty.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param blockSize BlockSize
 * @param output Output
 * @param context Context
 * @return splited context.
 */
public def void createContextsForFixedBlockSize001(String[] contextValues, String[] blockSize, Output output, MappingContext context) {
	int contextValueLength = contextValues.length
	if (contextValueLength > 0) {
		int blockSizeInt = 0
		try {
			blockSizeInt = Integer.parseInt(blockSize[0])
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function createContextsForFixedBlockSize001: '" + blockSize[0] + "' isn't an int value.")
		}

		int ftxCount = contextValueLength / blockSizeInt
		if (contextValueLength % blockSizeInt > 0) {
			++ftxCount
		}
		for (int i = 0; i < ftxCount; i++) {
			output.addValue("")
		}
	}
}

/**
 * Produces empty value when the first argument has one of the values passed as a constant in the second argument (separated by a semicolon), SUPPRESS otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param suchValuesString Such values string
 * @param output Output
 * @param context Mapping context
 * @return context with values "" or SUPPRESS.
 */
public def void createIfExistsAndHasOneOfSuchValues(String[] contextValues, String[] suchValuesString, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"

	if (suchValuesString == null || suchValuesString.length == 0) {
		throw new IllegalStateException("Custom Function createIfExistsAndHasOneOfSuchValues: there is no suchValuesString.")
	}

	if (contextValues != null && contextValues.length > 0) {
		String[] suchValues = suchValuesString[0].split(";")

		for (int i = 0; i < contextValues.length; i++) {
			String value = SUPPRESS
			for (int j = 0; j < suchValues.length; j++) {
				if (suchValues[j].equalsIgnoreCase(contextValues[i])) {
					value = ""
					break
				}
			}
			output.addValue(value)
		}
	} else {
		output.addSuppress()
	}
}

/**
 * Produces empty value if argument is not empty and exists, SUPPRESS otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Context
 * @return context with values "" or SUPPRESS.
 */
public def void createIfExistsAndHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			String value = contextValues[i]
			if (value != null && value.trim().length() > 0 && !output.isSuppress(value)) {
				output.addValue("")
			} else {
				output.addSuppress()
			}
		}
	} else {
		output.addSuppress()
	}
}

/**
 * Produces empty value if argument is not empty. First argument are context values.
 * Execution mode: All values of context
 *
 * @param contectValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context with values "" or SUPPRESS.
 */
public def void createIfHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			String value = contextValues[i]
			if (value != null && value.trim().length() > 0
					&& !output.isSuppress(value)) {
				output.addValue("")
			} else {
				output.addSuppress()
			}
		}
	}
}

/**
 * Produces empty value when the first argument has one of the values passed as a constant in the second argument (separated by a semicolon).
 * Execution mode: All values of context
 *
 * @param contectValues Context values
 * @param suchValuesString Values to be checked, separated by semicolon (e.g. 'AG;BY')
 * @param output Output
 * @param context Mapping Context 
 * @return context with values "" or SUPPRESS.
 */
public def void createIfHasOneOfSuchValues(String[] contextValues, String[] suchValuesString, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"

	if (suchValuesString == null || suchValuesString.length == 0) {
		throw new IllegalStateException("Custom Function createIfHasOneOfSuchValues: there is no suchValuesString.")
	}
	if (contextValues != null && contextValues.length > 0) {
		String[] suchValues = suchValuesString[0].split(";")

		for (int i = 0; i < contextValues.length; i++) {
			String value = SUPPRESS
			for (int j = 0; j < suchValues.length; j++) {
				if (suchValues[j].equalsIgnoreCase(contextValues[i])) {
					value = ""
					break
				}
			}
			output.addValue(value)
		}
	}
}

/**
 * Copies the whole context of first queue as often as the corresponding value of the second queue indicates.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param copyCounts Copy counts
 * @param output Output
 * @param context Context
 * @return copies the whole context of first queue.
 */
public def void createMultipleContextCopies(String[] contextValues, String[] copyCounts, Output output, MappingContext context) {
	if (copyCounts.length == 0) {
		throw new RuntimeException("Custom Function createMultipleContextCopies: copyCounts has length 0.")
	}
	int count = 0
	try {
		count = Integer.parseInt(copyCounts[0])
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function createMultipleContextCopies: '" + copyCounts[0] + "' is not a number.")
	}
	if (count > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			output.addValue(contextValues[i])
		}
		for (int i = 1; i < count; i++) {
			output.addContextChange()
			for (int k = 0; k < contextValues.length; k++) {
				output.addValue(contextValues[k])
			}
		}
	} else {
		for (int i = 0; i < contextValues.length; i++) {
			output.addSuppress()
		}
	}
}

/**
 * Copies each value of the first queue as often as the corresponding value of the second queue indicates.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param copyCounts Copy counts
 * @param output Output
 * @param context Context
 * @return a context of values (equal or enlarged in size to first arguments size).
 */
public def void createMultipleCopies(String[] contextValues, String[] copyCounts, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		if (copyCounts == null || contextValues.length != copyCounts.length) {
			throw new IllegalStateException("Custom Function createMultipleCopies: contextValues and copyCounts have different lengths.")
		}
		if (contextValues != null) {
			for (int i = 0; i < contextValues.length; i++) {
				int count = 0
				try {
					count = Integer.parseInt(copyCounts[i])
				} catch (Exception ex) {
					throw new RuntimeException("Custom Function createMultipleCopies: " + copyCounts[i] + " is not a number.")
				}

				if (count > 0) {
					for (int j = 0; j < count; j++) {
						output.addValue(contextValues[i])
					}
				} else {
					output.addSuppress()
				}
			}
		}
	}
}

/**
 * Creates an empty string for each number of the missing Argument-to-Argument 2-intervall.
 * Execution mode: All values of a context
 *
 * @param startValues Start values
 * @param endValues End values 
 * @param output Output
 * @param context Mapping Context
 * @return context with values.
 */
public def void createNumberRange(String[] startValues, String[] endValues, Output output, MappingContext context) {
	if (startValues.length != endValues.length) {
		throw new IllegalStateException("Custom Function createNumberRange: startValues and endValues have different lengths.")
	}
	for (int i = 0; i < startValues.length; i++) {
		String startValue = startValues[i].trim()
		if (output.isSuppress(startValue)) {
			continue
		}
		long lv_from
		try {
			lv_from = Long.parseLong(startValue)
		} catch (NumberFormatException nfe) {
			throw new RuntimeException("Custom Function createNumberRange: cannot parse long startValue '" + startValue + "'.")
		}

		String endValue = endValues[i].trim()
		long lv_to
		if (endValue.length() == 0 || output.isSuppress(startValue)) {
			lv_to = lv_from
		} else {
			try {
				lv_to = Long.parseLong(endValue)
			} catch (NumberFormatException nfe) {
				throw new RuntimeException("Custom Function createNumberRange: cannot parse long endValue '" + endValue + "'.")
			}
		}

		for (long k = lv_from; k <= lv_to; k++) {
			output.addValue(k + "")
		}
	}
}

/**
 * Maps every context value to itself when not null and not SUPPRESS - maybe the context queue will get shoter.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context values without SUPPRESS.
 */
public def void deleteSuppress(String[] contextValues, Output output, MappingContext context) {
	if (contextValues.length > 0) {
		contextValues.each {s->
			if(s !=null && !output.isSuppress(s)) {
				output.addValue(s)
			}
		}
	}
}

/**
 * Gets "true" if the argument is not empty, "false" otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping context
 * @return context with boolean values.
 */
public def void existsAndHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			if (contextValues[i] != null && contextValues[i].trim().length() > 0 && !output.isSuppress(contextValues[i])) {
				output.addValue("true")
			} else {
				output.addValue("false")
			}
		}
	} else {
		output.addValue("false")
	}
}

/**
 * Formats contexts in inputQueue by values (representing contexts) in exampleQueue.
 * Execution mode: All values of context
 *
 * @param inputQueue Input queue
 * @param exampleQueue Example queue
 * @param output Output
 * @param context Context
 * @return queue (with first arguments length and formatted by second arguments format).
 */
public def void formatByContextExample(String[] inputQueue, String[] exampleQueue, Output output, MappingContext context) {
	// Formats contexts in inputQueue by values (representing contexts) in exampleQueue.
	// This means that contexts can reduce
	// Requirement: number of contexts in inputQueue = number of values in exampleQueue
	if (inputQueue != null && inputQueue.length > 0) {
		// Declaration
		int inputCCCounter = 1
		int exampleContextCounter = 0

		// Count number of contexts in inputQueue for checking
		for (int k = 0; k < inputQueue.length; k++) {
			if (output.isContextChange(inputQueue[k])) {
				inputCCCounter++
			}
		}

		// Count number of values in exampleQueue for checking
		for (int l = 0; l < exampleQueue.length; l++) {
			if (!(output.isContextChange(exampleQueue[l]))) {
				exampleContextCounter++
			}
		}

		// Start of main computing
		if (inputCCCounter == exampleContextCounter) {
			int inputIndex = 0

			// Looping on exampleQueue
			for (int i = 0; i < exampleQueue.length; i++) {
				// Check for context change in exampleQueue
				if (output.isContextChange(exampleQueue[i])) {
					// Add context change to result
					output.addContextChange()
				} else
					// Looping on inputQueue
					for (int j = inputIndex; j < inputQueue.length; j++) {
						inputIndex++
						// Check for context changes in inputQueue
						if (output.isContextChange(inputQueue[j])) {
							break
						} else {
							// Add value to result if there is no context change
							output.addValue(inputQueue[j])
						}
					}
			}
		} else {
			throw new RuntimeException("Custom Function formatByContextExample: number of contexts in inputQueue '" + inputCCCounter + "' and number of values in exampleQueue '" +
			exampleContextCounter + "' are not equal.")
		}
	}
}

/**
 * Fragments the first argument (a single context value) into a maximal number of peaces (given by the second argument) of length from the third,
 * the last one may be shorter. All arguments are treated like constants.
 * Execution mode: All values of context
 *
 * @param wholeValue Whole value
 * @param maxFragmentCount Max fragment count
 * @param eachFragmentsLength Each fragments length
 * @param output Output
 * @param context Context
 * @return context containing fragments as values.
 */
public def void fragmentSingleValue(String[] wholeValue, String[] maxFragmentCount, String[] eachFragmentsLength, Output output, MappingContext context) {
	int maxCount
	int length

	if (wholeValue.length == 0) {
		return
	}
	if (wholeValue.length > 1) {
		List contextValueList = new ArrayList()
		for (int i = 0; i < wholeValue.length; i++) {
			if (!output.isSuppress(wholeValue[i])) {
				contextValueList.add(wholeValue[i])
			}
		}
		if (contextValueList.size() > 1) {
			throw new RuntimeException("Custom Function fragmentSingleValue: only one context value is expected.")
		}
		if (contextValueList.size() == 0) {
			output.addSuppress()
			return
		}
	}	
	String singleValue = wholeValue[0]
	try {
		maxCount = Integer.parseInt(maxFragmentCount[0])
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function fragmentSingleValue: maxFragmentCount '" + maxFragmentCount[0] + "' is not a number.")
	}

	try {
		length = Integer.parseInt(eachFragmentsLength[0])
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function fragmentSingleValue: eachFragmentsLength '" + eachFragmentsLength[0] + "' is not a number.")
	}

	int completeLineCount = singleValue.length() / length
	int rest = singleValue.length() - completeLineCount * length

	if (completeLineCount == 0 && rest == 0) {
		output.addValue(singleValue)
		return
	}

	if (!output.isSuppress(singleValue)) {	
		int resultLineCount = Math.min(maxCount, completeLineCount)
		for (int i = 0; i < resultLineCount; i++) {
			String subString = singleValue.substring(i * length, (i + 1) * length)
			output.addValue(subString)
		}

		if (maxCount > completeLineCount && rest > 0) {
			String subString = singleValue.substring(completeLineCount * length)
			output.addValue(subString)
		}
	} else {
		output.addSuppress()
	}
}

/**
 * Rearranges the third argument by the corresponding key set in the second argument formatted by the first argument like sorting.
 * Execution mode: All values of context
 *
 * @param arrangedSetOfKeys Arranged set of keys
 * @param allKeys All keys
 * @param valuesToRearrange Values to rearrange
 * @param output Output
 * @param context Mapping context
 * @return a context of rearranged values.
 */
public def void getFieldValsByStr(String[] arrangedSetOfKeys, String[] allKeys, String[] valuesToRearrange, Output output, MappingContext context) {
	if (allKeys == null || valuesToRearrange == null || allKeys.length != valuesToRearrange.length) {
		throw new IllegalStateException("Custom Function getFieldValsByStr: allKeys and valuesToRearrange have different lengths.")
	}

	if (valuesToRearrange == null || valuesToRearrange.length == 0) {
		for (int i = 0; i < arrangedSetOfKeys.length; i++) {
			output.addSuppress()
		}
	} else {
		if (arrangedSetOfKeys == null || arrangedSetOfKeys.length == 0) {
			throw new IllegalStateException("Custom Function getFieldValsByStr: there is no arrangedKeySet.")
		}

		Set indexSet = new HashSet()
		for (int i = 0; i < allKeys.length; i++) {
			if (output.isSuppress(allKeys[i]) || allKeys[i].trim().length() == 0) {
				// keep suppress values
				output.addSuppress()
			} else {
				// iterate over the arranged key set
				for1 : for (int k = 0; k < arrangedSetOfKeys.length; k++) {
					for (int m = 0; m < allKeys.length; m++) {
						if (indexSet.contains(new Integer(m))) {
							// value is already processed, ignore
							continue
						}
						if (output.isSuppress(allKeys[m]) || allKeys[m].trim().length() == 0) {
							continue
						}
						if (arrangedSetOfKeys[k].equals(allKeys[m])) {
							// value is not yet already processed add to output and keep in mind by putting onto a set
							output.addValue(valuesToRearrange[m])
							indexSet.add(Integer.valueOf(m + ""))
							break for1
						}
					}
				}
			}
		}
	}
}

/**
 * Gets the first non empty context value (or SUPPRESS if no such value exits).
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return the first non empty context value (or SUPPRESS if no such value exits).
 */
public def void getFirstContextValue(String[] contextValues, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"

	if (contextValues != null && contextValues.length > 0) {
		String value = SUPPRESS
		for (int i = 0; i < contextValues.length; i++) {
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
 * Gets a value from a context (first argument) by given index (second argument) (starting at 1).
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param index Index
 * @param output Output
 * @param context Context
 * @return value at given index.
 */
public def void getValueByIndex(String[] contextValues, String[] index, Output output, MappingContext context) {
	if (contextValues.length > 0 && index.length > 0) {
		String indexStr = index[0]
		
		try {
			int indexInt = Integer.parseInt(indexStr)
			if (indexInt <= contextValues.length && indexInt > 0) {
				output.addValue(contextValues[indexInt - 1])
			}
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function getValueByIndex: could not convert index '" + indexStr + "' to number.")
		}
	}
}

/**
 * Removes all multiple values from the context, leaving only the first.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Context
 * @return values without multiple entries.
 */
public def void groupContextValues(String[] contextValues, Output output, MappingContext context) {
	List values = new ArrayList()
	
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			if (output.isSuppress(contextValues[i])) {
				continue
			}
			if (!values.contains(contextValues[i])) {
				values.add(contextValues[i])
				output.addValue(contextValues[i])
			}
		}
	}
}

/**
 * Produces 'true' if the first argument has one of the values passed as a constant in the second argument (separated by a semicolon), 'false' otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param suchValuesString Such values string
 * @param output Output
 * @param context Mapping Context
 * @return 'true' if the first argument has one of the values passed as a constant, 'false' otherwise.
 */
public def void hasOneOfSuchValues(String[] contextValues, String[] suchValuesString, Output output, MappingContext context) {
	if (suchValuesString == null || suchValuesString.length == 0) {
		throw new IllegalStateException("Custom Function hasOneOfSuchValues: there is no suchValuesString.")
	}
	
	if (contextValues != null && contextValues.length > 0) {
		String[] suchValues = suchValuesString[0].split(";")

		for (int i = 0; i < contextValues.length; i++) {
			String oneOfSuchValues = "false"
			for (int j = 0; j < suchValues.length; j++) {
				if (suchValues[j].equalsIgnoreCase(contextValues[i])) {
					oneOfSuchValues = "true"
					break
				}
			}
			output.addValue(oneOfSuchValues)
		}
	} else {
		output.addValue("false")
	}
}

/**
 * Passes all context values and empty or non exists values are replaced by SUPPRESS.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Context
 * @return input context with empty values replaced by SUPPRESS.
 */
public def void passIfExistsAndHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			String str = contextValues[i]
			if (str != null && str.trim().length() > 0) {
				output.addValue(str)
			} else {
				output.addSuppress()
			}
		}
	} else {
		output.addSuppress()
	}
}

/**
 * Passes all context values and empty values are replaced by SUPPRESS.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context value or SUPPRESS.
 */
public def void passIfHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			String str = contextValues[i]
			if (str != null && str.trim().length() > 0) {
				output.addValue(str)
			} else {
				output.addSuppress()
			}
		}
	}
}

/**
 * Uses the first argument (the context should have exactly one value) as often as the length of the second context indicates.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param secondContext Second context
 * @param output Output
 * @param context Mapping Context
 * @return Return the second context filled with the value from the first argument.
 */
public def void simpleUseOneAsMany(String[] contextValues, String[] secondContext, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0 && secondContext != null && secondContext.length > 0) {
		String value = null
		if (contextValues.length == 1) {
			value = contextValues[0]
		} else {
			for (int i = 0; i < contextValues.length; i++) {
				if (!output.isSuppress(contextValues[i])) {
					value = contextValues[i]
					break
				}
			}
			if (value == null) {
				value = contextValues[0]
			}
		}
		for (int i = 0; i < secondContext.length; i++) {
			output.addValue(value)
		}
	}
}

/**
 * Uses the first argument (the context should have exactly one value) as often as the length of the second context indicates and splits the output by each value.
 * Execution mode: All values of a context
 *
 * @param contextValues Context values
 * @param secondContext Second context
 * @param output Output
 * @param context MappingContext
 * @return a number of contexts (containing one value) defined by 2. arguments length.
 */
public def void simpleUseOneAsManyAndSplitByEachValue(String[] contextValues, String[] secondContext, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0 && secondContext != null && secondContext.length > 0) {
		String value = null
		if (contextValues.length == 1) {
			value = contextValues[0]
		} else {
			for (int i = 0; i < contextValues.length; i++) {
				if (!output.isSuppress(contextValues[i])) {
					value = contextValues[i]
					break
				}
			}
			if (value == null) {
				value = contextValues[0]
			}
		}
		output.addValue(value)
		for (int i = 1; i < secondContext.length; i++) {
			output.addContextChange()
			output.addValue(value)
		}
	}
}

/**
 * Splits input string (first Argument) at given delimiter (second Argument) and creates a new context value for each new value.
 * Execution mode: All values of context
 *
 * @param inputString Input string
 * @param delimiter Delimiter
 * @param output Output
 * @param context Mapping context
 * @return splited context values.
 */
public def void splitValueStringToContextValues(String[] inputString, String[] delimiter, Output output, MappingContext context) {
	if (inputString == null) {
		return
	}

	for (int i = 0; i < inputString.length; i++) {
		String[] splits = inputString[i].split(delimiter[0])
		for (int j = 0; j < splits.length; j++) {
			output.addValue(splits[j])
		}
	}
}

/**
 * Gets first value and SUPPRESS multiple values of a context.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return context without multiple values. Set SUPPRESS to multiple values.
 */
public def void suppressMultipleContextValues(String[] contextValues, Output output, MappingContext context) {
	List values = new ArrayList()
	
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			if (output.isSuppress(contextValues[i])) {
				output.addSuppress()
			} else if (!values.contains(contextValues[i])) {
				values.add(contextValues[i])
				output.addValue(contextValues[i])
			} else {
				output.addSuppress()
			}
		}
	}
}

/**
 * Uses the first argument as often as values exit in the second argument.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param secondContext Second context
 * @param output Output
 * @param context Context
 * @return a number of contexts defined by second arguments length.
 */
public def void useOneContextAsMany(String[] contextValues, String[] secondContext, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		if (secondContext != null && secondContext.length > 0) {
			for (int i = 0; i < secondContext.length - 1; i++) {
				for (int j=0; j < contextValues.length; j++) {
					output.addValue(contextValues[j])
				}
				output.addContextChange()
			}
			// the last context
			for (int j=0; j < contextValues.length; j++) {
				output.addValue(contextValues[j])
			}
		}
	}
}