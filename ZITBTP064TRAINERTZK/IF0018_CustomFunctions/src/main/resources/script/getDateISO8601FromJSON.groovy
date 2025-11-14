import com.sap.it.api.mapping.*;

/*Add MappingContext parameter to read or set headers and properties
def String customFunc1(String P1,String P2,MappingContext context) {
         String value1 = context.getHeader(P1);
         String value2 = context.getProperty(P2);
         return value1+value2;
}

Add Output parameter to assign the output value.
def void custFunc2(String[] is,String[] ps, Output output, MappingContext context) {
        String value1 = context.getHeader(is[0]);
        String value2 = context.getProperty(ps[0]);
        output.addValue(value1);
        output.addValue(value2);
}*/

public def String getDateISO8601FromJSON(String inputDate) {
    String returnValue = ""

    try {
        // Transform date
        if (inputDate != null) {
            // Remove all whitespaces
            inputDate = inputDate.replaceAll("\\s+", "")
            if (inputDate.length() > 0) {
                // Get milliseconds if there is a date labeling ("/Date(...)/")
                if (inputDate.toLowerCase().indexOf('date') > -1) {
                    inputDate = inputDate.substring(6, inputDate.length() -2)
                }

                long inputDateLong = inputDate.toLong()
                Date date = new Date(inputDateLong)
                returnValue = date.format('yyyy-MM-dd')
            }
        }
        return returnValue

    } catch (Exception ex) {
        throw new RuntimeException("Custom Function getDateISO8601FromJSON: " + ex.getMessage(), ex)
    }
}
