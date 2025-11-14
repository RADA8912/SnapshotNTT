import com.sap.it.api.mapping.*;

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
