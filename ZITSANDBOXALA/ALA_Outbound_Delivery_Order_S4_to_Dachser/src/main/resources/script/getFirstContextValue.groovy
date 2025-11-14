import com.sap.it.api.mapping.*;


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