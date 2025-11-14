
/**
 * Gets a date in ISO 8601 (yyyy-MM-dd) from JSON format in milli seconds or  with date labeling (/Date(...)/). First argument is date in milli seconds or  with date labeling (/Date(...)/).
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in ISO 8601 date format.
 */
public def String getDateISO8601FromJSON(String inputDate) {
	String returnValue = ""

	try {
		// Transform date
		if (inputDate != null) {
			// Remove all whitespaces
			inputDate = inputDate.replaceAll("\\s+","")
			if (inputDate.length() > 0) {
				// Get milli seconds if there is a date labeling ("/Date(...)/")
				if (inputDate.toLowerCase().indexOf('date') > -1) {
					inputDate = inputDate.substring(6, inputDate.length() - 2)
				}

				long inputDateLong = inputDate.toLong()
				Date date = new Date(inputDateLong)
				returnValue = date.format('yyyy-MM-dd')
			}
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateISO8601FromJSON: " + ex.getMessage(), ex)
	}
}
