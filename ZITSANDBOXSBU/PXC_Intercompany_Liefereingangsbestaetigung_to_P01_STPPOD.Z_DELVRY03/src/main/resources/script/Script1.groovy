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