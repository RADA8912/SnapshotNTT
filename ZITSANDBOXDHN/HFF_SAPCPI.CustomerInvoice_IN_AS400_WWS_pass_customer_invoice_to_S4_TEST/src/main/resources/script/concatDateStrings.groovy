import com.sap.it.api.mapping.*;
import java.text.SimpleDateFormat;

def String concatDateStrings(String sYear, String sMonth, String sDay){
    Integer iYear = Integer.parseInt(sYear)
    Integer iMonth = Integer.parseInt(sMonth)
    Integer iDay = Integer.parseInt(sDay)
    
    if(iMonth == 0 || iDay == 0) {
        throw new Exception()
    }
    
    Calendar date = Calendar.getInstance()
    date.set(iYear, iMonth - 1, iDay)
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd")
    return format.format(date.getTime())
}