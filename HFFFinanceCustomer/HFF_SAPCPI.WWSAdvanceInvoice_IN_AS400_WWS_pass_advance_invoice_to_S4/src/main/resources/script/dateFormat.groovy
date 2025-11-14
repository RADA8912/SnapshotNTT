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

def String getMonthLeadingZeros(String sMonth){
    
    String sMonthLeadingZeros = "0"
    
    if(sMonth.length() == 1){
        sMonthLeadingZeros = sMonthLeadingZeros.concat(sMonth)
    }else{
        sMonthLeadingZeros = sMonth
    }
    
    return sMonthLeadingZeros
}

def String getDayLeadingZeros(String sDay){
    
    String sDayLeadingZeros = "0"
    
    if(sDay.length() == 1){
        sDayLeadingZeros = sDayLeadingZeros.concat(sDay)
    }else{
        sDayLeadingZeros = sDay
    }
    
    return sDayLeadingZeros
}
