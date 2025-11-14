import com.sap.it.api.mapping.*;
import java.text.SimpleDateFormat;

def String concatDateStringsWithAlternative(String sYear, String sMonth, String sDay, String sYearAlt, String sMonthAlt, String sDayAlt) {
    Integer iYear
    Integer iMonth
    Integer iDay

    try {
        iYear = Integer.parseInt(sYear)
        iMonth = Integer.parseInt(sMonth)
        iDay = Integer.parseInt(sDay)

        if(iMonth == 0 || iDay == 0) {
            throw new Exception()
        }
    } catch (Exception e) {
        iYear = Integer.parseInt(sYearAlt)
        iMonth = Integer.parseInt(sMonthAlt)
        iDay = Integer.parseInt(sDayAlt)
    }

    if(iMonth == 0 || iDay == 0) {
        throw new Exception()
    }

    Calendar date = Calendar.getInstance()
    date.set(iYear, iMonth - 1, iDay)
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd")
    return format.format(date.getTime())
}
