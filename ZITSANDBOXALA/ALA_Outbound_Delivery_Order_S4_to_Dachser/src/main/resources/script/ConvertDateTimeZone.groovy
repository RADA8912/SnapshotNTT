import com.sap.it.api.mapping.*;
import java.text.*;



public def String convertDateTimeTimeZone(String inputDateTime, String inputTimeZone, String inputDateFormat, String outputTimeZone, String outputDateFormat) {
	final String SUPPRESS = "_sUpPresSeD_"
	String returnValue = null
	List<String> availableTimeZoneList = Arrays.asList(TimeZone.getAvailableIDs())

	// Create Suppress if inputDateTime is empty
	if (inputDateTime.trim().equals("")) {
		return SUPPRESS
	} else {
		// Check input time zone
		if (inputTimeZone.equals("")) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: inputTimeZone is not set.")
		}
		if (!availableTimeZoneList.contains(inputTimeZone)) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: unexpected inputTimeZone '" + inputTimeZone + "'.") 
		}

		// Check input date format
		if (inputDateFormat.equals("")) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: inputDateFormat is not set.")
		}

		if (inputDateTime.length() != inputDateFormat.replaceAll("'","").length()) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: inputDateFormat '" + inputDateFormat + "' correlates not to inputDateTime '" + inputDateTime + "'.") 
		}

		// Check output time zone
		if (outputTimeZone.equals("")) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: outputTimeZone is not set.")
		}
		if (!availableTimeZoneList.contains(outputTimeZone)) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: unexpected outputTimeZone '" + outputTimeZone + "'.") 
		}

		// Check output date format
		if (inputDateFormat.equals("")) {
			throw new IllegalArgumentException("Custom Function convertDateTimeTimeZone: outputDateFormat is not set.")
		}

		try {
			// Input simple date format
			SimpleDateFormat inputSimpleDateFormat = new SimpleDateFormat(inputDateFormat)
			inputSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(inputTimeZone))

			// Parses value and assumes it represents a date and time in input time zone
			Date outputDateTime = inputSimpleDateFormat.parse(inputDateTime)

			// Output simple date format
			SimpleDateFormat outputSimpleDateFormat = new SimpleDateFormat(outputDateFormat)
			outputSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(outputTimeZone))

			// Formats date time in output time zone
			returnValue = outputSimpleDateFormat.format(outputDateTime)
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function convertDateTimeTimeZone: " + ex.getMessage(), ex)
		}

		return returnValue
	}
}