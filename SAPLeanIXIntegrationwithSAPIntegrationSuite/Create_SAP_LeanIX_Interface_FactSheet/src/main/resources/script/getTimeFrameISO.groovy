import com.sap.gateway.ip.core.customdev.util.Message;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

def Message processData(Message message) {
    // Retrieve time offset from iFlow property, expected in minutes
    int timeOffset = message.getProperty("timeOffsetInMinutes").toInteger();

    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
    isoFormat.setTimeZone(timeZone);
    
    Calendar now = Calendar.getInstance(timeZone);
    
    // Apply the time offset
    now.add(Calendar.MINUTE, -timeOffset);
    String timeOffsetFormatted = isoFormat.format(now.getTime());
    
    // Store the formatted time offset in the message property
    message.setProperty("timeOffset", timeOffsetFormatted);

    return message;
}