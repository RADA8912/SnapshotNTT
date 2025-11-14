import com.sap.it.api.mapping.*

/**
 * Sets a constant to create field with JSON data type 'null' if value is empty otherwise return value.
 * This constant will be changed in a separate groovy script behind 'XML To JSON Converter'.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return constant for 'null' or value.
 */
public def String setEmptyToJSONnull(String value) {
	if (value.length() == 0) {
		return "_JsOnNulL_"
	} else {
		return  value
	}
}

/**
 * Sets a constant to create field with JSON data type 'null'.
 * This constant will be changed in a separate groovy script behind 'XML To JSON Converter'.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return constant for 'null'.
 */
public def String setJSONnull(String inputNotUsed) {
	return "_JsOnNulL_"
}

/**
 * Sets a constant ahead of a numeric value to create field with JSON data type 'string'.
 * This constant will be removed in a separate groovy script behind 'XML To JSON Converter'.
 * Execution mode: Single value
 *
 * @param value Value
 * @return constant ahead of a numeric value.
 */
public def String setJSONstring(String value) {
	return "_JsOnSTriNg_" + value
}

/**
 * Sets a constant to create field with JSON data type 'null' if value is SUPPRESS otherwise return value.
 * This constant will be changed in a separate groovy script behind 'XML To JSON Converter'.
 * Execution mode: Single value
 *
 * @param value Value
 * @return constant for 'null' or value.
 */
public def void setSuppressToJSONnull(String[] contextValues, Output output, MappingContext context) {
	List values = new ArrayList()
	
	for (int i = 0; i < contextValues.length; i++) {
		if (output.isSuppress(contextValues[i])) {
			output.addValue("_JsOnNulL_")
		} else {
			output.addValue(contextValues[i])
		}
	}
}