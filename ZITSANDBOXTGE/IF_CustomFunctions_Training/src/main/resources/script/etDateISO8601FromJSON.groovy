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

def String customFunc(String inputDate){
	String returnValue = "";
	
	try{
	    if(inputDate != null){
	        inputDate = inputDate.replaceAll("\\s+", "")
	        if(inputDate.length() > 0) {
	            if(inputDate.toLowerCase().indexOf('date') > -1) {
	                inputDate = inputDate.substring(6, inputDate.length() -2)
	            }
	            long inputDateLong = inputDate.toLong()
	            Date date = new Date(inputDateLong)
	           
                returnValue = iso8601Format.format(date)
	        }
	    }
	    return returnValue
	} catch(Exception ex)
	{
	    throw new RuntimeException("Fehler in CustomFunction getISO8601:" + ex.getMEssage(), ex)
	}
}