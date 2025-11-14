import com.sap.gateway.ip.core.customdev.util.Message;
import java.text.SimpleDateFormat
import java.util.HashMap;

def Message setPropertyBlineDate(Message message) {
       properties = message.getProperties();
       
       Integer iYear, iMonth, iDay
       
        try {
            iYear = Integer.parseInt(properties.get("UBJA"))   
            iMonth = Integer.parseInt(properties.get("UBMO"))   
            iDay = Integer.parseInt(properties.get("UBTA"))   

            if(iYear == 0 || iMonth == 0 || iDay == 0) {
                throw new IllegalArgumentException()
            }

        } catch (NumberFormatException | IllegalArgumentException ignored) {
            iYear = Integer.parseInt(properties.get("UBLJ"))
            iMonth = Integer.parseInt(properties.get("UBLM"))
            iDay = Integer.parseInt(properties.get("UBLT"))
        }

        if(iYear == 0 || iMonth == 0 || iDay == 0) {
            throw new IllegalArgumentException()
        }

        Calendar date = Calendar.getInstance()
        date.set(iYear, iMonth - 1, iDay)
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd")
       
        message.setProperty("BLINE_DATE", format.format(date.getTime()))
        return message
}
