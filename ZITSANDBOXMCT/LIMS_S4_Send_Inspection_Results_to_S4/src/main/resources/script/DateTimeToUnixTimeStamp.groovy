import com.sap.it.api.mapping.*
import java.text.*
import java.util.*
import java.util.Calendar

// Function to get a date string formatted as /Date(<UnixTimestamp>)/
public def String convertToUnixDateFormat(String inputDateTime) {
    // Parse the input date-time
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    Date date = inputFormat.parse(inputDateTime)

    // Get Unix timestamp (seconds since 1970-01-01 00:00:00 UTC)
    long unixTimestamp = date.getTime()

    // Create the desired output format
    return "/Date(${unixTimestamp})/"
}

// Function to get a time string formatted as PT<Hours>H<Minutes>M<Seconds>S
public def String convertToIsoDurationFormat(String inputDateTime) {
    // Parse the input date-time
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
    Date date = inputFormat.parse(inputDateTime)

    // Use Calendar to extract hours, minutes, and seconds
    Calendar calendar = Calendar.getInstance()
    calendar.setTime(date)

    int hours = calendar.get(Calendar.HOUR_OF_DAY)
    int minutes = calendar.get(Calendar.MINUTE)
    int seconds = calendar.get(Calendar.SECOND)

    // Create the ISO 8601 duration format
    return "PT${hours}H${minutes}M${seconds}S"
}

// Existing function to get days from now
public def String getDaysFromNow(int inputDays) {
    use(groovy.time.TimeCategory) {
        def dateNew = new Date() + inputDays.days
        date = dateNew.format("yyyy-MM-dd")
    }
    return date
}