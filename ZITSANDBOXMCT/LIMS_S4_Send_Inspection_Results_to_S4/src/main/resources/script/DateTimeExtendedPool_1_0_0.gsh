/**
* Groovy Custom Functions Date Time Extended Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*
import java.text.*

/**
 * Convert input date time to target time zone. The first argument is date time. The second argument is time zone of input date time for example 'CET'.
 * The third argument is date format of input date time for example 'yyyyMMddHHmmss'. The fourth argument is time zone of output date time for example 'EST'.
 * The fifth argument is date format of output date time for example 'yyyyMMddHHmmss'.
 * Execution mode: Single value
 *
 * @param inputDateTime Input date time
 * @param inputTimeZone Input time zone
 * @param inputDateFormat Input date format
 * @param outputTimeZone Output time zone
 * @param outputDateFormat Output date format
 * @return date time in target time zone.
 */
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

/**
 * Transform date by format for example name of day or name of month in local. The first argument is date time.
 * The second argument is date format of input for example 'yyyyMMddHHmmss'. The third argument is language of input for example 'de'.
 * The fourth argument is date format of output for example 'yyyyMMddHHmmss'. The fifth argument is language of input for example 'en'.
 * Execution mode: Single value
 *
 * @param inputDateTime Input date time
 * @param inputDateFormat Input date format
 * @param inputLanguage Input language
 * @param outputDateFormat Output date format
 * @param outputLanguage Output language
 * @return date time in target time format.
 */
public def String dateTransLocale(String inputDateTime, String inputDateFormat, String inputLanguage, String outputDateFormat, String outputLanguage) {
	Locale localeIn = null
	Locale localeOut = null
	String languageIn = null
	String languageOut = null
	final String RETURN_DEFAULT = "DEFAULT"
	String returnValue = RETURN_DEFAULT

	// Check input date format
	if (inputDateFormat.equals("")) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: inputDateFormat is not set.")
	}

	// Check inputLanguage and get locale
	try {
		if (inputLanguage.equals("")) {
			throw new IllegalArgumentException("Custom Function dateTransLocale: inputLanguage is not set.")
		} else {
			localeIn = new Locale(inputLanguage)
			languageIn = localeIn.getISO3Language()
		}
	} catch (MissingResourceException e) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: inputLanguage '" + inputLanguage + "' is not supported.")
	}

	// Check output date format
	if (outputDateFormat.equals("")) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: outputDateFormat is not set.")
	}

	// Check outputLanguage and get locale
	try {
		if (outputLanguage.equals("")) {
			throw new IllegalArgumentException("Custom Function dateTransLocale: outputLanguage is not set.")
		} else {
			localeOut = new Locale(outputLanguage)
			languageOut = localeOut.getISO3Language()
		}
	} catch (MissingResourceException e) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: outputLanguage '" + outputLanguage + "' is not supported.")
	}

	// Transform date time for selected local
	try {
		// Format if there is a date time
		if(inputDateTime.length() > 0) {
			// Set input date time in input date format
			DateFormat formatterIn = new SimpleDateFormat(inputDateFormat, localeIn)
			Date DateIn = formatterIn.parse(inputDateTime)

			// Create local input calendar and set date
			Calendar calendarIn = Calendar.getInstance(localeIn)
			calendarIn.setTime(DateIn)
			calendarIn.setLenient(false)

			// Create local output calendar and set date
			Calendar calendarOut = Calendar.getInstance(localeOut)
			calendarOut.set(Calendar.YEAR, calendarIn.get(Calendar.YEAR))
			calendarOut.set(Calendar.MONTH, calendarIn.get(Calendar.MONTH))
			calendarOut.set(Calendar.DATE, calendarIn.get(Calendar.DATE))
			calendarOut.set(Calendar.HOUR, calendarIn.get(Calendar.HOUR))
			calendarOut.set(Calendar.MINUTE, calendarIn.get(Calendar.MINUTE))
			calendarOut.set(Calendar.SECOND, calendarIn.get(Calendar.SECOND))
			calendarOut.setLenient(false)

			// Get date time in ouput date format
			DateFormat formatterOut = new SimpleDateFormat(outputDateFormat, localeOut)
			formatterOut.setLenient(false)
			returnValue = formatterOut.format(calendarOut.getTime())
		} else {
			returnValue = ""
		}
	} catch (Exception ex) {
		throw new RuntimeException(ex.getMessage(), ex)
	}

	// Check if return value is available
	if(returnValue.toString().equals(RETURN_DEFAULT)) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: Can not compute return value. Please check date time and arguments.")
	} else {
		return returnValue
	}
}

/**
 * Formats date long (milliseconds since 1.1.1970).
 * Execution mode: Single value
 *
 * @param input value = date time
 * @return date in format yyyy-MM-dd.
 */
public def String formatDateLong(String input) {
	Date date = new java.util.Date(Long.parseLong(input))
	String dateNew = date.format("yyyy-MM-dd")
	
	return dateNew
}

/**
 * Get day(s) from now
 * Execution mode: Single value
 *
 * @param inputDays Input day(s)
 * @return date now add input day(s).
 */
public def String getDaysFromNow(int inputDays) {
	use(groovy.time.TimeCategory) {
		def dateNew = new Date() + inputDays.days
		date = dateNew.format("yyyy-MM-dd")
	}
	
	return date
}

/**
 * Get calendar week of input date (yyyy, MM, dd). The fourth argument is language code (ISO 639 alpha-2 or alpha-3) (e.g. 'de').
 * The fifth argument is country code (ISO 3166 alpha-2) (e.g. 'DE').
 * Execution mode: Single value
 *
 * @param year Year
 * @param month Month
 * @param day Day
 * @param language Language
 * @param country Country
 * @return calendar week of input date.
 */
public def String getCalendarWeek(int year, int month, int day, String language, String country) {
	String returnValue = ""

	// You need to set local (language and country) to compute start of calendar week
	Locale locale = new Locale(language.toLowerCase(), country.toUpperCase())
	Calendar cal = Calendar.getInstance(locale)

	// Set date values
	cal.set(Calendar.YEAR, year)
	cal.set(Calendar.MONTH, month - 1) // January = 0
	cal.set(Calendar.DAY_OF_MONTH, day)

	// Get week of year in defined locale
	int WEEK_OF_YEAR = cal.get(Calendar.WEEK_OF_YEAR)
	returnValue = String.valueOf(WEEK_OF_YEAR)

	return returnValue
}

/**
 * Gets a date from a long value (milliseconds since 1.1.1970) and returns in a new format. First argument is date in long. Second argument is ouput date format.
 * Execution mode: Single value
 *
 * @param inputDateTimeLong Input date time long
 * @param outputDateFormat Output date format
 * @return a date from a long value and returns in a new format.
 */
public def String getDateFromLong(String inputDateTimeLong, String outputDateFormat) {
	// inputDateTimeLong = milliseconds since 1.1.1970
	String returnValue = ""
	long datetimeLong = 0

	// Check output date format
	if (outputDateFormat.equals("")) {
		throw new IllegalArgumentException("Custom Function getDateFromLong: ouputDateFormat is not set.")
	}

	// Transform date time long in defined format
	try {
		if (inputDateTimeLong != null && inputDateTimeLong.length() > 0) {
			datetimeLong = Long.parseLong(inputDateTimeLong)
			Date date = new Date(datetimeLong)

			// Get date in new format
			DateFormat formatter = new SimpleDateFormat(outputDateFormat)
			returnValue = formatter.format(date)
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateFromLong: " + ex.getMessage(), ex)
	}
}

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

/**
 * Gets a date in format ISO 8601 (yyyy-MM-dd) from SAP DATS format (YYYYMMDD). First argument is date in format yyyyMMdd.
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in ISO 8601 date format.
 */
public def String getDateISO8601FromSAP(String inputDate) {
	String returnValue = ""

	// Check format of inputDate
	if(inputDate.length() != 8 || !inputDate.isNumber()) {
		throw new RuntimeException("Custom Function getDateJSONFromSAP: Please use SAP DATS format (YYYYMMDD).")
	}

	try {
		// Transform date
		if (inputDate != null && inputDate.length() > 0 && inputDate != '00000000') {
			Date date = Date.parse('yyyyMMdd', inputDate)
			returnValue = date.format('yyyy-MM-dd')
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateISO8601FromSAP: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a date in JSON format ('/Date(' + milli seconds + ')/') from ISO 8601 format (yyyy-MM-dd). First argument is date in format yyyy-MM-dd.
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in JSON format.
 */
public def String getDateJSONFromISO8601(String inputDate) {
	String returnValue = ""

	// Check format of inputDate
	if(inputDate.length() != 10 || inputDate.indexOf('-') == -1) {
		throw new RuntimeException("Custom Function getDateJSONFromSAP: Please use ISO 8601 format (yyyy-MM-dd).")
	}

	try {
		// Transform date
		if (inputDate != null && inputDate.length() > 0) {
			Date date = Date.parse('yyyy-MM-dd', inputDate)
			returnValue = "/Date(" + date.getTime() + ")/"
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateJSONFromISO8601: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a date in JSON format ('/Date(' + milli seconds + ')/') from SAP DATS format (YYYYMMDD). First argument is date in format yyyyMMdd.
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in JSON format.
 */
public def String getDateJSONFromSAP(String inputDate) {
	String returnValue = ""

	// Check format of inputDate
	if(inputDate.length() != 8 || !inputDate.isNumber()) {
		throw new RuntimeException("Custom Function getDateJSONFromSAP: Please use SAP DATS format (YYYYMMDD).")
	}

	try {
		// Transform date
		if (inputDate != null && inputDate.length() > 0 && inputDate != '00000000') {
			Date date = Date.parse('yyyyMMdd', inputDate)
			returnValue = "/Date(" + date.getTime() + ")/"
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateJSONFromSAP: " + ex.getMessage(), ex)
	}
}

/**
 * Get date by format for example name of day or name of month in local language. First argument is year. Second argument is month. Third argument is day.
 * Fourth argument is output format ('EEEE dd MMMM yyyy'). Fift argument is locale ('de').
 * Execution mode: Single value
 *
 * @param inputYear Input year
 * @param inputMonth Input month
 * @param inputDay Input day
 * @param outputDateFormat Output date format
 * @param outputLocale Output locale
 * @return date by format for example name of day or name of month in local language.
 */
public def String getDateLocal(String inputYear, String inputMonth, String inputDay, String outputDateFormat, String outputLocale) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = null
	String returnValue = SUPPRESS

	// Check output date format
	if (outputDateFormat.equals("")) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: outputDateFormat is not set.")
	}

	// Check outputLocale and set locale
	if (outputLocale.length() > 1) {
		locale = new Locale(outputLocale)
	} else if (outputLocale.equals("")) {
		throw new IllegalArgumentException("Custom Function dateTransLocale: outputLocale is not set.")
	} else {
		throw new IllegalArgumentException("Custom Function dateTransLocale: outputLocale '" + outputLocale + "' is not supported.")
	}

	// Transform date time for selected local
	try {
		// Format if there is a date time
		if (inputYear.length() > 0 && inputMonth.length() > 0 && inputDay.length() > 0) {
			// Set input date time in input date
			int yearInt = Integer.parseInt(inputYear)
			int monthInt = Integer.parseInt(inputMonth) - 1 // January = 0
			int dateInt = Integer.parseInt(inputDay)

			// Create local calendar and set date
			Calendar calendar = Calendar.getInstance(locale)
			calendar.set(Calendar.YEAR, yearInt)
			calendar.set(Calendar.MONTH, monthInt)
			calendar.set(Calendar.DATE, dateInt)
			calendar.setLenient(false)

			// Get date time in ouput date format
			DateFormat formatterOut = new SimpleDateFormat(outputDateFormat, locale)
			formatterOut.setLenient(false)
			returnValue = formatterOut.format(calendar.getTime())
		} else {
			returnValue = SUPPRESS
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateLocal: " + ex.getMessage(), ex)
	}

	return returnValue
}

/**
 * Gets a date in SAP DATS format (YYYYMMDD) from ISO 8601 format (yyyy-MM-dd). First argument is date in format yyyy-MM-dd.
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in SAP DATS format.
 */
public def String getDateSAPFromISO8601(String inputDate) {
	String returnValue = ""

	// Check format of inputDate
	if(inputDate.length() != 10 || inputDate.indexOf('-') == -1) {
		throw new RuntimeException("Custom Function getDateJSONFromSAP: Please use ISO 8601 format (yyyy-MM-dd).")
	}

	try {
		// Transform date
		if (inputDate != null && inputDate.length() > 0) {
			Date date = Date.parse('yyyy-MM-dd', inputDate)
			returnValue = date.format('yyyyMMdd')
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateSAPFromISO8601: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a date in SAP DATS format (YYYYMMDD) from JSON format in milli seconds or  with date labeling (/Date(...)/). First argument is date in milli seconds or  with date labeling (/Date(...)/).
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in SAP DATS format.
 */
public def String getDateSAPFromJSON(String inputDate) {
	String returnValue = ""

	try {
		// Transform date
		if (inputDate != null) {
			// Remove all whitespaces
			inputDate = inputDate.replaceAll("\\s+", "")
			if (inputDate.length() > 0) {
				// Get milli seconds if there is a date labeling ("/Date(...)/")
				if (inputDate.toLowerCase().indexOf('date') > -1) {
					inputDate = inputDate.substring(6, inputDate.length() - 2)
				}

				long inputDateLong = inputDate.toLong()
				Date date = new Date(inputDateLong)
				returnValue = date.format('yyyyMMdd')
			}
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateSAPFromJSON: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a date time in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss) from JSON format in milli seconds or  with date labeling (/Date(...)/). First argument is date in milli seconds or  with date labeling (/Date(...)/).
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in ISO 8601 format.
 */
public def String getDateTimeISO8601FromJSON(String inputDateTime) {
	String returnValue = ""

	try {
		// Transform date time
		if (inputDateTime != null) {
			// Remove all whitespaces
			inputDateTime = inputDateTime.replaceAll("\\s+", "")
			if (inputDateTime.length() > 0) {
				// Get milli seconds if there is a date labeling ("/Date(...)/")
				if (inputDateTime.toLowerCase().indexOf('date') > -1) {
					inputDateTime = inputDateTime.substring(6, inputDateTime.length() - 2)
				}

				long inputDateLong = inputDateTime.toLong()
				Date date = new Date(inputDateLong)
				returnValue = date.format("yyyy-MM-dd'T'HH:mm:ss")
			}
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateTimeISO8601FromJSON: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a date time in JSON format in milli seconds or  with date labeling (/Date(...)/) from ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss). First argument is date in ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss).
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in JSON format.
 */
public def String getDateTimeJSONFromISO8601(String inputDateTime) {
	String returnValue = ""

	// Check format of inputDateTime
	if(inputDateTime.length() != 19 || inputDateTime.indexOf('-') == -1 || inputDateTime.indexOf('T') == -1 || inputDateTime.indexOf(':') == -1) {
		throw new RuntimeException("Custom Function getDateJSONFromSAP: Please use ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss).")
	}

	try {
		// Transform date time
		if (inputDateTime != null) {
			Date date = Date.parse("yyyy-MM-dd'T'HH:mm:ss", inputDateTime)
			returnValue = "/Date(" + date.getTime() + ")/"
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateTimeJSONFromISO8601: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a date time in SAP DATS and TIMS format (YYYYMMDD HHMMSS) from JSON format in milli seconds or  with date labeling (/Date(...)/). First argument is date in milli seconds or  with date labeling (/Date(...)/).
 * Execution mode: Single value
 *
 * @param inputDate Input date
 * @return a date in SAP DATS and TIMS format.
 */
public def String getDateTimeSAPFromJSON(String inputDateTime) {
	String returnValue = ""

	try {
		// Transform date time
		if (inputDateTime != null) {
			// Remove all whitespaces
			inputDateTime = inputDateTime.replaceAll("\\s+", "")
			if (inputDateTime.length() > 0) {
				// Get milli seconds if there is a date labeling ("/Date(...)/")
				if (inputDateTime.toLowerCase().indexOf('date') > -1) {
					inputDateTime = inputDateTime.substring(6, inputDateTime.length() - 2)
				}

				long inputDateLong = inputDateTime.toLong()
				Date date = new Date(inputDateLong)
				returnValue = date.format('yyyyMMdd HHmmss')
			}
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getDateTimeSAPFromJSON: " + ex.getMessage(), ex)
	}
}

/**
 * Gets default locale.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return default locale.
 */
public def String getLocaleDefault(String inputNotUsed){
	Locale localeDefault = Locale.getDefault()

	return String.valueOf(localeDefault)
}

/**
 * Gets a time in ISO 8601 format (HH:mm:ss) from JSON format in milli seconds or  with date labeling (/Date(...)/). First argument is date in milli seconds or  with date labeling (/Date(...)/).
 * Execution mode: Single value
 *
 * @param inputDateTime Input date time
 * @return a time in ISO 8601 format.
 */
public def String getTimeISO8601FromJSON(String inputDateTime) {
	String returnValue = ""

	try {
		// Transform date time
		if (inputDateTime != null) {
			// Remove all whitespaces
			inputDateTime = inputDateTime.replaceAll("\\s+", "")
			if (inputDateTime.length() > 0) {
				// Get milli seconds if there is a time labeling ("/Date(...)/")
				if (inputDateTime.toLowerCase().indexOf('date') > -1) {
					inputDateTime = inputDateTime.substring(6, inputDateTime.length() - 2)
				}

				long inputDateTimeLong = inputDateTime.toLong()
				Date date = new Date(inputDateTimeLong)
				returnValue = date.format('HH:mm:ss')
			}
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getTimeISO8601FromJSON: " + ex.getMessage(), ex)
	}
}

/**
 * Gets a time in SAP TIMS format (HHMMSS) from JSON format in milli seconds or  with date labeling (/Date(...)/). First argument is date in milli seconds or  with date labeling (/Date(...)/).
 * Execution mode: Single value
 *
 * @param inputDateTime Input date time
 * @return a time in SAP TIMS format.
 */
public def String getTimeSAPFromJSON(String inputDateTime) {
	String returnValue = ""

	try {
		// Transform date time
		if (inputDateTime != null) {
			// Remove all whitespaces
			inputDateTime = inputDateTime.replaceAll("\\s+", "")
			if (inputDateTime.length() > 0) {
				// Get milli seconds if there is a time labeling ("/Date(...)/")
				if (inputDateTime.toLowerCase().indexOf('date') > -1) {
					inputDateTime = inputDateTime.substring(6, inputDateTime.length() - 2)
				}

				long inputDateTimeLong = inputDateTime.toLong()
				Date date = new Date(inputDateTimeLong)
				returnValue = date.format('HHmmss')
			}
		}
		return returnValue

	} catch (Exception ex) {
		throw new RuntimeException("Custom Function getTimeSAPFromJSON: " + ex.getMessage(), ex)
	}
}

/**
 * Gets time zone short local.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return time zone short local.
 */
public def String getTimeZoneShortLocal(String inputNotUsed) {
	String returnValue = ""

	returnValue = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)

	return returnValue
}

/**
 * Checks if input is a valid locale and returns 'true' or 'false'. First argument is language code (ISO 639 alpha-2 or alpha-3) (e.g. 'de').
 * Second argument is country code (ISO 3166 alpha-2) (e.g. 'DE').
 * Execution mode: Single value
 *
 * @param language Language
 * @param country Country
 * @return 'true' if values are valide locale otherwise 'false'.
 */
public def String isLocale(String language, String country) {
	Locale locale = null
	String returnValue = ""

	try {
		// Check if language is available
		if (language.length() > 1) {
			locale = new Locale(language, country)
		} else {
			returnValue = "false"
		}

		// Check if locale is valid
		if (locale.getISO3Language() != null && locale.getISO3Country() != null) {
			returnValue = "true"
		} else {
			returnValue = "false"
		}
	} catch (MissingResourceException e) {
		returnValue = "false"
	}

	return returnValue
}

/**
 * Checks if input is a valid language code and returns 'true' or 'false'. First argument is language for example 'de'.
 * Execution mode: Single value
 *
 * @param language Language
 * @return 'true' if values are valide language otherwise 'false'.
 */
public def String isLanguage(String language) {
	Locale locale = null
	String returnValue = ""

	try {
		// Check if language is available
		if (language.length() > 1) {
			locale = new Locale(language)
		} else {
			returnValue = "false"
		}

		// Check if language is valid
		if (locale.getISO3Language() != null && locale.getISO3Country() != null) {
			returnValue = "true"
		} else {
			returnValue = "false"
		}
	} catch (MissingResourceException e) {
		returnValue = "false"
	}

	return returnValue
}

/**
 * Check if time zone is valid (for example 'Europe/Berlin').
 * Execution mode: Single value
 *
 * @param zoneID This is time zone id.
 * @return 'true' if values are valid time zone otherwise 'false'.
 */
public def String isValidTimeZone(String zoneID) {
	for (String timeZoneID : TimeZone.getAvailableIDs()) {
		if (timeZoneID.equals(zoneID)) {
			return 'true'
		}
	}
	return 'false'
}