import com.sap.it.api.mapping.*;


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