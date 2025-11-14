import com.sap.it.api.mapping.*;
//import java.util.GregorianCalendar;
//import java.util.Calendar;
import java.util.Date;
//import java.util.DateTime;
import java.text.SimpleDateFormat;

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

def String subtractDaysFromDate(String date){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date parseDate = sdf.parse(date);

    //Calendar c = Calendar.getInstance();
    //c.setTime(parseDate);
	//c.add(Calendar.DATE, -30);
	
	//Date result = sdf.format(c.getTime());
	//return result.format(result); 
	
	//Date zcurDate = new Date();
    Date newDate = parseDate - 30;
    //return newDate.format("yyyy-MM-dd");
    return newDate.format("yyyy-MM-dd'T'00:00:00.000");
    //return new SimpleDateFormat("yyyy-MM-dd'T'00:00:00").parse(newDate);
}