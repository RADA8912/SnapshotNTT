import com.sap.it.api.mapping.*
import java.text.*


public def String getDateAfterDays(String date, String numberOfDays) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = Locale.getDefault()
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", locale)
	formatter.setLenient(false)
	Calendar calendar = Calendar.getInstance(locale)
	Date aDate
	int dayCount
	
	try {
		aDate = formatter.parse(date)
	} catch (ParseException pe) {
		if (date.trim().length() == 0) {
			return SUPPRESS
		} else {
			throw new RuntimeException("Custom Function getDateAfterDays: cannot parse date '" + date + "'.")
		}
	}
	try {
		dayCount = Integer.parseInt(numberOfDays)
	} catch (NumberFormatException pe) {
		throw new RuntimeException("Custom Function getDateAfterDays: cannot parse int '" + numberOfDays + "'.")
	}

	calendar.setTimeInMillis(aDate.getTime())
	calendar.add(Calendar.DAY_OF_MONTH, dayCount)

	return formatter.format(calendar.getTime())
}