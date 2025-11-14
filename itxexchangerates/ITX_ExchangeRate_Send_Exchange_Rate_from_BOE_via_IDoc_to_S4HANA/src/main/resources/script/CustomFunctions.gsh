import com.sap.it.api.mapping.*
import java.text.*

/**
 * Gets the date after the date of the first argument (yyyyMMdd) by adding a number of days contained in the second argument.
 * Execution mode: Single value
 *
 * @param date Date
 * @param numberOfDays Number of days
 * @return the date after the specified days.
 */
public def String getDateAfterDays(String date, String numberOfDays) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = Locale.getDefault()
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
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

/**
 * Gets a value from header.
 * There is an empty response in Message Mapping 'Display Queue' or 'Simulation' because message header is not accessible.
 * Execution mode: Single value
 *
 * @param headerName header name
 * @param context Mapping context
 * @return a header value.
 */
public def String getHeader(String headerName, MappingContext context) {
	def returnValue = context.getHeader(headerName)
	return returnValue
}

/**
 * Gets the last context value (or output.isSuppress if no such value exits).
 * Execution mode: All values of context
 *
 * @param contectValues Context values
 * @param output Output
 * @param context Mapping Context
 * @return the last context value (or output.isSuppress if no such value exits).
 */
public def void getLastContextValue(String[] contextValues, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"
	
	if (contextValues != null && contextValues.length > 0) {
		String value = SUPPRESS

		for (int i = contextValues.length - 1; i >= 0; i --) {
			String str = contextValues[i]
			if (str != null && !output.isSuppress(str)) {
				value = str
				break
			}
		}
		output.addValue(value)
	}
}