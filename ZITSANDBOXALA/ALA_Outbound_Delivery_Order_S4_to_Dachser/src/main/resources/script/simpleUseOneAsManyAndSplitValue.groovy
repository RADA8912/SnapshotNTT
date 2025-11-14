import com.sap.it.api.mapping.*;

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