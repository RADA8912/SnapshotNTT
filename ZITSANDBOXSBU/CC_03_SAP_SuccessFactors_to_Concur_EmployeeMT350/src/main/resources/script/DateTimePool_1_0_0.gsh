/**
* Groovy Custom Functions Date Time Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*
import java.text.*

/**
 * Use this generated constant as an input for Custom Function getDate: indicates a single date value.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return a constant.
 */
public def String constantDate(String inputNotUsed) {
	return "date"
}

/**
 * Use this generated constant as an input for Custom Function getDate: indicates the begin of a time period.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return a constant.
 */
public def String constantDateFrom(String inputNotUsed) {
	return "dateFrom"
}

/**
 * Use this generated constant as an input for Custom Function getDate: indicates a single time value.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return a constant.
 */
public def String constantDateTo(String inputNotUsed) {
	return "dateTo"
}

/**
 * Use this generated constant as an input for Custom Function getDate: indicates the end of a time period.
 * Execution mode: Single value
 *
 * @param inputNotUsed Input not used
 * @return a constant.
 */
public def String constantTime(String inputNotUsed) {
	return "time"
}

/**
 * Creates a YYYYMMDD date from the first argument using the second argument (an EDIFACT-F2379-conform qualifier). 
 * The third argument has one of the values constantDate, constantDateFrom, constantDateTo or constantTime.
 * Execution mode: All values of context
 *
 * @param dateValues Date values
 * @param dateFormatValues Date format values
 * @param mode Mode
 * @param output Output
 * @param context Context
 * @return formatted date values (yyyyMMdd) if a date-mode is set or time if time-mode is set.
 */
public def void getDate(String[] dateValues, String[] dateFormatValues, String[] mode, String[] language, String[] country, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"
	String languageStr = language[0]
	String countryStr = country[0]
	
	// Set locale
	try {
		locale = new Locale(languageStr, countryStr)
		
		if (locale == null || languageStr.length() == 0 || countryStr.length() == 0) {
			throw new Exception("Custom Function getDate: cannot get locale by language '" + languageStr + "' and country '" + countryStr + "'.")
		}
	} catch (Exception ex) {
		throw new Exception("Custom Function getDate: cannot get locale by language '" + languageStr + "' and country '" + countryStr + "'.")
	}
	
	if (dateValues != null && dateValues.length > 0) {
		if (dateFormatValues != null && dateValues.length != dateFormatValues.length) {
			throw new IllegalStateException("Custom Function getDate: dateValues context and dateFormatValues context have different length.")
		}
		
		if (!"date".equalsIgnoreCase(mode[0]) && !"dateto".equalsIgnoreCase(mode[0]) && !"datefrom".equalsIgnoreCase(mode[0]) && !"time".equalsIgnoreCase(mode[0])) {
			throw new IllegalArgumentException("Custom Function getTime: unvalid mode '" + mode + "'. Please use Custom Function constantDate, constantDateFrom, constantDateTo or constantTime.")
		}
		try {
			for (int i = 0; i < dateValues.length; i++) {
				String dateValue = dateValues[i].trim()
				int dateLength = dateValue.length()
				String dateFormat = dateFormatValues[i]
				int dtmFormat = Integer.parseInt(dateFormat)
				boolean unexpectedLength = false
				boolean modeIsDateTo = "dateto".equalsIgnoreCase(mode[0])
				boolean modeIsTime = "time".equalsIgnoreCase(mode[0])
				String year = ""
				String month = ""
				String week = ""
				String date = null
				
				if (dateValue == null || output.isSuppress(dateValue) || dateValue.trim().length() == 0 || dateValue.replaceAll("^0*", "").length() == 0) {
					output.addSuppress()
				} else {
					try {
						switch (dtmFormat) {
							case 2: // DDMMYY
								if (dateLength != 6) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = "20" + dateValue.substring(4, 6) + dateValue.substring(2, 4) + dateValue.substring(0, 2)
								}
								break
							case 3: // MMDDYY
								if (dateLength != 6) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = "20" + dateValue.substring(4, 6) + dateValue.substring(0, 2) + dateValue.substring(2, 4)								
								}
								break
							case 101: // YYMMDD
								if (dateLength != 6) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = "20" + dateValue
								}
								break
							case 102: // CCYYMMDD
								if (dateLength != 8) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = dateValue
								}
								break
							case 201: // YYMMDDHHMM
								if (dateLength != 10) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = "20" + dateValue.substring(0, 6)
								} else if (modeIsTime) {
									date = dateValue.substring(6, 10)
								}
								break
							case 202: // YYMMDDHHMMSS
								if (dateLength != 12) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = "20" + dateValue.substring(0, 6)
								} else if (modeIsTime) {
									date = dateValue.substring(6, 12)
								}
								break
							case 203: // CCYYMMDDHHMM
								if (dateLength != 12) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = dateValue.substring(0, 8)
								} else if (modeIsTime) {
									date = dateValue.substring(8, 12)
								}
								break
							case 204: // CCYYMMDDHHMMSS
								if (dateLength != 14) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									date = dateValue.substring(0, 8)
								} else if (modeIsTime) {
									date = dateValue.substring(8, 14)
								}
								break
							case 401: // HHMM
								if (dateLength != 4) {
									unexpectedLength = true
								} else if (modeIsTime) {
									date = dateValue
								}
								break
							case 402: // HHMMSS
								if (dateLength != 6) {
									unexpectedLength = true
								} else if (modeIsTime) {
									date = dateValue
								}
								break
							case 609: // YYMM
								if (dateLength != 4) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									if (!modeIsDateTo) { // Start of month
										date = "20" + dateValue + "01"
									} else { // End of month
										year = dateValue.substring(0, 2)
										month = dateValue.substring(2, 4)
										date = getLastDayOfMonth(year, month, languageStr, countryStr)
									}
								}
								break
							case 610: // CCYYMM
								if (dateLength != 6) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									if (!modeIsDateTo) { // Start of month
										date = dateValue + "01"
									} else { // End of month
										year = dateValue.substring(0, 4)
										month = dateValue.substring(4, 6)
										date = getLastDayOfMonth(year, month, languageStr, countryStr)
									}
								}
								break
							case 615: // YYWW
								if (dateLength != 4) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									year = dateValue.substring(0, 2)
									week = dateValue.substring(2, 4)
									if (modeIsDateTo) {
										date = getFridayOfWeek(year, week, languageStr, countryStr)
									} else if (!modeIsTime) {
										date = getMondayOfWeek(year, week, languageStr, countryStr)
									}
								}
								break
							case 616: // CCYYWW
								if (dateLength != 6) {
									unexpectedLength = true
								} else if (!modeIsTime) {
									year = dateValue.substring(0, 4)
									week = dateValue.substring(4, 6)
									if (modeIsDateTo) {
										date = getFridayOfWeek(year, week, languageStr, countryStr)
									} else {
										date = getMondayOfWeek(year, week, languageStr, countryStr)
									}
								}
								break
							case 715: // YYWW-YYWW with/without hyphen
								if (dateLength == 9) {
									dateValue = dateValue.replaceAll("-", "")
									dateLength = dateValue.length()
								}
								if (dateLength != 8) {
									unexpectedLength = true
								} else if (modeIsDateTo) {
									year = dateValue.substring(4, 6)
									week = dateValue.substring(6, 8)
									date = getFridayOfWeek(year, week, languageStr, countryStr)
								} else if (!modeIsTime) {
									year = dateValue.substring(0, 2)
									week = dateValue.substring(2, 4)
									date = getMondayOfWeek(year, week, languageStr, countryStr)
								}
								break
							case 716: // CCYYWW-CCYYWW without hyphen
								if (dateLength == 13) {
									dateValue = dateValue.replaceAll("-", "")
									dateLength = dateValue.length()
								}
								if (dateLength != 12) {
									unexpectedLength = true
								} else if (modeIsDateTo) {
									year = dateValue.substring(6, 10)
									week = dateValue.substring(10, 12)
									date = getFridayOfWeek(year, week, languageStr, countryStr)
								} else if (!modeIsTime) {
									year = dateValue.substring(0, 4)
									week = dateValue.substring(4, 6)
									date = getMondayOfWeek(year, week, languageStr, countryStr)
								}
								break
							case 718: // CCYYMMDD-CCYYMMDD with and without hyphen
								if (dateLength == 17) {
									dateValue = dateValue.replaceAll("-", "")
									dateLength = dateValue.length()
								}
								if (dateLength != 16) {
									unexpectedLength = true
								} else if (modeIsDateTo) {
									date = dateValue.substring(8)
								} else if (!modeIsTime) {
									date = dateValue.substring(0, 8)
								}
								break
							default:
								throw new RuntimeException("Custom Function getTime: Unexpected date time EDIFACT qualifier '" + dateFormat + "'.")
						}

						if (unexpectedLength) {
							throw new NumberFormatException("Custom Function getTime: Length of date string '" + date + "' does not fit date format '" + dateFormat + "'.")
						}
						
						// Format and set date
						if (date == null) {
							date = SUPPRESS
						} else if (!modeIsTime) {
							DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
							formatter.setLenient(false)
							
							try {
								formatter.parse(date)
							} catch (Exception ex) {
								throw new NumberFormatException("Custom Function getDate: can not parse date string '" + date + "' of format '" + dateFormat + "'.")
							}
						}
						output.addValue(date)
					} catch (RuntimeException re) {
						throw new RuntimeException("Custom Function getDate: dateValue '" + dateValue + "', dateFormat '" + dateFormat + "' causes '" + re + "'.")
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function getDate: " + ex, ex)
		}
	}
}

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
 * Gets the number of days between two dates of yyyyMMdd format. The first argument marks the begin of the time intervall.
 * Execution mode: Single value
 *
 * @param dateBegin Date begin
 * @param dateEnd Date end
 * @return the number of days between two dates.
 */
public def String getDateDifference(String dateBegin, String dateEnd) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = Locale.getDefault()
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
	formatter.setLenient(false)

	Date date1 = null
	Date date2 = null
	try {
		date1 = formatter.parse(dateBegin)
	} catch (ParseException pe) {
		if (dateBegin.trim().length() == 0) {
			return SUPPRESS
		} else {
			throw new RuntimeException("Custom Function getDateDifference: cannot parse dateBegin '" + dateBegin + "'.")
		}
	}
	try {
		date2 = formatter.parse(dateEnd)
	} catch (ParseException pe) {
		if (dateEnd.trim().length() == 0) {
			return SUPPRESS
		} else {
			throw new RuntimeException("Custom Function getDateDifference: cannot parse dateBegin '" + dateEnd + "'.")
		}
	}

	long diff = date2.getTime() - date1.getTime()
	return (diff / 86400000) + ""
}

/**
 * Gets an EDIFACT-F2379-conform qualifier descriping the dates format.
 * Execution mode: Single value
 *
 * @param date Date
 * @return an EDIFACT-F2379-conform qualifier.
 */
public def String getDateFormat(String date) {
	final String SUPPRESS = "_sUpPresSeD_"
	String dtmFormat = SUPPRESS
	String dateString = date != null ? date.trim() : null
	
	if (date != null && dateString.length() > 0) {
		dtmFormat = ""
		int dateLength = date.length()
		
		switch (dateLength) {
			case 6:
				dtmFormat = "101"
				break
			case 8:
				dtmFormat = "102"
				break
			case 10:
				dtmFormat = "201"
				break
			case 12:
				String yearBegin = date.substring(0, 2)
				if ("20".equalsIgnoreCase(yearBegin) || "19".equalsIgnoreCase(yearBegin)) {
					dtmFormat = "203"
				} else {
					dtmFormat = "202"
				}
				break
			case 14:
				dtmFormat = "204"
				break
			default:
				throw new IllegalArgumentException("Custom Function getDateFormat: the date length '" + dateLength + "' cannot be processed.")
		}
	}
	
	return dtmFormat
}

/**
 * Gets the last date of one or more date strings (yyyyMMdd).
 * Execution mode: Single value
 *
 * @param dates Dates
 * @param output Output
 * @param context Context
 * @return the last date.
 */
 public def void getEndOfOneOrTwoDates(String[] dates, Output output, MappingContext context) {
	if (dates != null && dates.length > 0) {
		String endDate = null
		if (dates.length < 2) {
			endDate = dates[0]
		}
		if (endDate == null) {
			String date_0 = dates[0].trim()
			if (date_0.length() == 0 || !output.isSuppress(date_0) || "0".equals(date_0)) {
				endDate = dates[1]
			}
			if (endDate == null) {
				String date_1 = dates[1].trim()
				if (date_1.length() == 0 || !output.isSuppress(date_1) || "0".equals(date_1)) {
					endDate = dates[0]
				}
			}
			if (endDate == null) {
				Locale locale = Locale.getDefault()
				DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
				formatter.setLenient(false)
				Date date1 = null
				Date date2 = null
				try {
					date1 = formatter.parse(dates[0])
					date2 = formatter.parse(dates[1])
				} catch (ParseException e) {
					// ignore and try again
				}
				if (date1 == null || date2 == null) {
					try {
						date1 = formatter.parse(dates[0].substring(0,8))
						date2 = formatter.parse(dates[1].substring(0,8))
					} catch (ParseException e) {
						throw new RuntimeException("Custom Function getEndOfOneOrTwoDates: Could not parse Date '" + dates[0] + "' and/or '" + dates[1] + "'.")
					}
				}

				if (date1.before(date2)) {
					endDate = formatter.format(date2)
				} else {
					endDate = formatter.format(date1)
				}
			}
		}
		output.addValue(endDate)
	}
 }

/**
 * Generates the last day (Friday) of the week (Argument 2: "W") or month ("M") for a given date in Argument 1 (yyyyMMdd), returns Argument 1 when Argument 2 is "D".
 * Execution mode: Single value
 *
 * @param dateString Date string
 * @param timingQualf Timing qualf
 * @return last day (Friday) of the week or month for a date.
 */
 public def String getEndDateOfTimePeriod(String dateString, String timingQualf, String language, String country) {
	String resultString = null
	
	// Set locale
	try {
		locale = new Locale(language, country)
		
		if (locale == null || language.length() == 0 || country.length() == 0) {
			throw new Exception("Custom Function getEndDateOfTimePeriod: cannot get locale by language '" + language + "' and country '" + country + "'.")
		}
	} catch (Exception ex) {
		throw new Exception("Custom Function getEndDateOfTimePeriod: cannot get locale by language '" + language + "' and country '" + country + "'.")
	}
	
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
	formatter.setLenient(false)

	String newDateString = dateString.trim()
	int length = newDateString.length()
	if (length == 6) {
		newDateString = "20" + newDateString
	} else if (length > 8) {
		newDateString = newDateString.substring(0, 8)
	}

	Date date = null
	try {
		date = formatter.parse(newDateString)
	} catch (Exception e) {
		throw new RuntimeException("Custom Function getEndDateOfTimePeriod: Could not parse Date '" + newDateString + "'.")
	}

	if (timingQualf.equals("D")) {
		resultString = newDateString
	} else {
		Calendar calendar = Calendar.getInstance(locale)
		calendar.setTime(date)
		if (timingQualf.equals("W")) { // weekly
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
		} else if (timingQualf.equals("M")) { // monthly
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
		} else {
			throw new RuntimeException("Custom Function getEndDateOfTimePeriod: unexspected time period qualifier '" + timingQualf + "'.")
		}
		resultString = formatter.format(calendar.getTime())
	}

	return resultString
}

/**
 * Gets the first of month date after the date of the first argument (YYYYMMDD) by adding a number of months contained in the second argument.
 * Execution mode: Single value
 *
 * @param date Date
 * @param numberOfMonths Number of months
 * @return the first of month date after the date.
 */
public def String getFirstOfMonthAfterMonths(String date, String numberOfMonths) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = Locale.getDefault()
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
	formatter.setLenient(false)
	Calendar calendar = Calendar.getInstance(locale)
	Date aDate
	int monthCount
	
	try {
		aDate = formatter.parse(date)
	} catch (ParseException pe) {
		if (date.trim().length() == 0) {
			return SUPPRESS
		} else {
		throw new RuntimeException("Custom Function getFirstOfMonthAfterMonths: cannot parse dateBegin '" + date + "'.")
		}
	}
	try {
		monthCount = Integer.parseInt(numberOfMonths)
	} catch (NumberFormatException pe) {
		throw new RuntimeException("Custom Function getFirstOfMonthAfterMonths: cannot parse int '" + numberOfMonths + "'.")
	}

	calendar.setTimeInMillis(aDate.getTime())
	calendar.add(Calendar.MONTH, monthCount)
	calendar.set(Calendar.DAY_OF_MONTH, 1)

	return formatter.format(calendar.getTime())
}

/**
 * Gets friday of week. First argument is year. Second argument is week. Third argument is language code (ISO 639 alpha-2 or alpha-3) (e.g. 'de').
 * Fourth argument is country code (ISO 3166 alpha-2) (e.g. 'DE').
 * Execution mode: Single value
 *
 * @param year Year
 * @param week Week
 * @param language Language
 * @param country Country
 * @return date of friday of week.
 */
public def String getFridayOfWeek(String year, String week, String language, String country) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = null
	Calendar calendar = null
	Date date = null
	String weekOfYear = ""

	language = language.toLowerCase()
	country = country.toUpperCase()

	// Check null or empty
	if (year == null || year.length() == 0 || week == null || week.length() == 0) {
		return ""
	// Check SUPPRESS
	} else if (SUPPRESS.equals(year) || SUPPRESS.equals(week)) {
		return SUPPRESS
	} else {
		// Set locale
		try {
			locale = new Locale(language, country)
			
			if (locale == null || language.length() == 0 || country.length() == 0) {
				throw new Exception("Custom Function getFridayOfWeek: cannot get locale by language '" + language + "' and country '" + country + "'.")
			}
		} catch (Exception ex) {
			throw new Exception("Custom Function getFridayOfWeek: cannot get locale by language '" + language + "' and country '" + country + "'.")
		}
		
		// Check year and create 4 digits
		try {
			int yearInt = Integer.parseInt(year)
			yearInt = yearInt.abs()
			if (yearInt < 100) {
				yearInt = 2000 + yearInt
			}
			year = yearInt.toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getFridayOfWeek: cannot parse year '" + year + "' to integer.")
		}		

		// Check week
		try {		
			int weekInt = Integer.parseInt(week)
			week = weekInt.abs().toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getFridayOfWeek: cannot parse week '" + week + "' to integer.")
		}
		
		// Set date and calendar
		try {
			// Set week
			DateFormat formatterWeek = new SimpleDateFormat("yyyyww", locale)
			weekOfYear = year + week
			date = formatterWeek.parse(weekOfYear)

			// Set friday of calendar week
			calendar = Calendar.getInstance(locale)
			calendar.setTimeInMillis(date.getTime())
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
		} catch (Exception ex) {
			throw new Exception("Custom Function getFridayOfWeek: cannot get calendar by year '" + year + "' and week '" + week + "' pattern 'yyyyww' locale '" + 
			locale.getLanguage() + "_" + locale.getCountry() + "'.")
		}

		// Get formatted day
		DateFormat formatterDate = new SimpleDateFormat("yyyyMMdd", locale)
		formatterDate.setLenient(false)	
		return formatterDate.format(calendar.getTime())
	}
}

/**
 * Gets last day of month. First argument is year. Second argument is month. Third argument is language code (ISO 639 alpha-2 or alpha-3) (e.g. 'de').
 * Fourth argument is country code (ISO 3166 alpha-2) (e.g. 'DE').
 * Execution mode: Single value
 *
 * @param year Year
 * @param month Month
 * @param language Language
 * @param country Country
 * @return last day of month.
 */
public def String getLastDayOfMonth(String year, String month, String language, String country) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = null
	Calendar calendar = null
	Date date = null
	String weekOfYear = ""

	language = language.toLowerCase()
	country = country.toUpperCase()

	// Check null or empty
	if (year == null || year.length() == 0 || month == null || month.length() == 0) {
		return ""
	// Check SUPPRESS
	} else if (SUPPRESS.equals(year) || SUPPRESS.equals(month)) {
		return SUPPRESS
	} else {
		// Set locale
		try {
			locale = new Locale(language, country)
			
			if (locale == null || language.length() == 0 || country.length() == 0) {
				throw new Exception("Custom Function getLastDayOfMonth: cannot get locale by language '" + language + "' and country '" + country + "'.")
			}
		} catch (Exception ex) {
			throw new Exception("Custom Function getLastDayOfMonth: cannot get locale by language '" + language + "' and country '" + country + "'.")
		}
		
		// Check year and create 4 digits
		try {
			int yearInt = Integer.parseInt(year)
			yearInt = yearInt.abs()
			if (yearInt < 100) {
				yearInt = 2000 + yearInt
			}
			year = yearInt.toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getLastDayOfMonth: cannot parse year '" + year + "' to integer.")
		}		

		// Check month
		try {		
			int monthInt = Integer.parseInt(month)
			month = monthInt.abs().toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getLastDayOfMonth: cannot parse month '" + month + "' to integer.")
		}
		
		// Set date and calendar
		try {
			// Set month
			DateFormat formatterWeek = new SimpleDateFormat("yyyyMM", locale)
			formatterWeek.setLenient(false)
			monthOfYear = year + month
			date = formatterWeek.parse(monthOfYear)

			// Set last day of month
			calendar = Calendar.getInstance(locale)
			calendar.setTimeInMillis(date.getTime())
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
		} catch (Exception ex) {
			throw new Exception("Custom Function getLastDayOfMonth: cannot get calendar by year '" + year + "' and month '" + month + "' pattern 'yyyyww' locale '" + 
			locale.getLanguage() + "_" + locale.getCountry() + "'.")
		}

		// Get formatted day
		DateFormat formatterDate = new SimpleDateFormat("yyyyMMdd", locale)
		formatterDate.setLenient(false)	
		return formatterDate.format(calendar.getTime())
	}
}

/**
 * Gets monday of week. First argument is year. Second argument is week. Default locale is used.
 * Execution mode: Single value
 *
 * @param year Year
 * @param week Week
 * @return date of monday of week.
 */
public def String getMondayOfWeek(String year, String week) {
	final String SUPPRESS = "_sUpPresSeD_"

	Locale locale = Locale.getDefault()
	Calendar calendar = null
	Date date = null
	String weekOfYear = ""

	// Check null or empty
	if (year == null || year.length() == 0 || week == null || week.length() == 0) {
		return ""
	// Check SUPPRESS
	} else if (SUPPRESS.equals(year) || SUPPRESS.equals(week)) {
		return SUPPRESS
	} else {		
		// Check year and create 4 digits
		try {
			int yearInt = Integer.parseInt(year)
			yearInt = yearInt.abs()
			if (yearInt < 100) {
				yearInt = 2000 + yearInt
			}
			year = yearInt.toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot parse year '" + year + "' to integer.")
		}		

		// Check week
		try {		
			int weekInt = Integer.parseInt(week)
			week = weekInt.abs().toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot parse week '" + week + "' to integer.")
		}
		
		// Set date and calendar
		try {
			// Set week
			DateFormat formatterWeek = new SimpleDateFormat("yyyyww", locale)
			weekOfYear = year + week
			date = formatterWeek.parse(weekOfYear)

			// Set monday of calendar week
			calendar = Calendar.getInstance(locale)
			calendar.setTimeInMillis(date.getTime())
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot get calendar by year '" + year + "' and week '" + week + "' pattern 'yyyyww' locale '" + 
			locale.getLanguage() + "_" + locale.getCountry() + "'.")
		}

		// Get formatted day
		DateFormat formatterDate = new SimpleDateFormat("yyyyMMdd", locale)
		formatterDate.setLenient(false)	
		return formatterDate.format(calendar.getTime())
	}
}

/**
 * Gets monday of week. First argument is year. Second argument is week. Third argument is language code (ISO 639 alpha-2 or alpha-3) (e.g. 'de').
 * Fourth argument is country code (ISO 3166 alpha-2) (e.g. 'DE').
 * Execution mode: Single value
 *
 * @param year Year
 * @param week Week
 * @param language Language
 * @param country Country
 * @return date of monday of week.
 */
public def String getMondayOfWeekLocale(String year, String week, String language, String country) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = null
	Calendar calendar = null
	Date date = null
	String weekOfYear = ""

	language = language.toLowerCase()
	country = country.toUpperCase()

	// Check null or empty
	if (year == null || year.length() == 0 || week == null || week.length() == 0) {
		return ""
	// Check SUPPRESS
	} else if (SUPPRESS.equals(year) || SUPPRESS.equals(week)) {
		return SUPPRESS
	} else {
		// Set locale
		try {
			locale = new Locale(language, country)
			
			if (locale == null || language.length() == 0 || country.length() == 0) {
				throw new Exception("Custom Function getMondayOfWeek: cannot get locale by language '" + language + "' and country '" + country + "'.")
			}
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot get locale by language '" + language + "' and country '" + country + "'.")
		}
		
		// Check year and create 4 digits
		try {
			int yearInt = Integer.parseInt(year)
			yearInt = yearInt.abs()
			if (yearInt < 100) {
				yearInt = 2000 + yearInt
			}
			year = yearInt.toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot parse year '" + year + "' to integer.")
		}		

		// Check week
		try {		
			int weekInt = Integer.parseInt(week)
			week = weekInt.abs().toString()
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot parse week '" + week + "' to integer.")
		}
		
		// Set date and calendar
		try {
			// Set week
			DateFormat formatterWeek = new SimpleDateFormat("yyyyww", locale)
			weekOfYear = year + week
			date = formatterWeek.parse(weekOfYear)

			// Set monday of calendar week
			calendar = Calendar.getInstance(locale)
			calendar.setTimeInMillis(date.getTime())
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
		} catch (Exception ex) {
			throw new Exception("Custom Function getMondayOfWeek: cannot get calendar by year '" + year + "' and week '" + week + "' pattern 'yyyyww' locale '" + 
			locale.getLanguage() + "_" + locale.getCountry() + "'.")
		}

		// Get formatted day
		DateFormat formatterDate = new SimpleDateFormat("yyyyMMdd", locale)
		formatterDate.setLenient(false)	
		return formatterDate.format(calendar.getTime())
	}
}

/**
 * Gets the number of months between two dates of YYYYMMDD format. Dates wizhin a calendar month yields a 0. The first argument marks the begin of the time intervall.
 * Execution mode: Single value
 *
 * @param dateBegin Date begin
 * @param dateEnd Date end
 * @return the number of months between two dates.
 */
public def String getMonthDifference(String dateBegin, String dateEnd) {
	final String SUPPRESS = "_sUpPresSeD_"
	Locale locale = Locale.getDefault()
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
	formatter.setLenient(false)
	Date date1 = null
	Date date2 = null
	
	try {
		date1 = formatter.parse(dateBegin)
	} catch (ParseException pe) {
		if (dateBegin.trim().length() == 0) {
			return SUPPRESS
		} else {
		throw new RuntimeException("Custom Function getMonthDifference: cannot parse dateBegin '" + dateBegin + "'.")
		}
	}
	try {
		date2 = formatter.parse(dateEnd)
	} catch (java.text.ParseException pe) {
		if (dateEnd.trim().length() == 0) {
			return SUPPRESS
		} else {
		throw new RuntimeException("Custom Function getMonthDifference: cannot parse dateBegin '" + dateEnd + "'.")
		}
	}

	Calendar calendar1 = Calendar.getInstance(locale)
	calendar1.setTimeInMillis(date1.getTime())
	Calendar calendar2 = Calendar.getInstance(locale)
	calendar2.setTimeInMillis(date2.getTime())

	int yearDiff = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR)
	int monthEnd = calendar2.get(Calendar.MONTH)
	int monthBegin = calendar1.get(Calendar.MONTH)
	if (yearDiff != 0) {
		monthEnd = monthEnd + 12 * yearDiff
	}

	return (monthEnd - monthBegin) + ""
}

/**
 * Generates the first day (Monday) of the week (Argument 2: "W") or month ("M") for a given date in Argument 1 (yyyyMMdd), returns Argument 1 when Argument 2 is "D".
 * Execution mode: Single value
 *
 * @param dateString Date string
 * @param timingQualf Timing qualf
 * @return first day of the week or month for a date.
 */
public def String getStartDateOfTimePeriod(String dateString, String timingQualf) {
	String resultString = null
	
	// Please check local if you use calendar week
	Locale locale = Locale.getDefault()
	
	DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
	formatter.setLenient(false)

	String newDateString = dateString.trim()
	int length = newDateString.length()
	if (length == 6) {
		newDateString = "20" + newDateString
	} else if (length > 8) {
		newDateString = newDateString.substring(0, 8)
	}

	Date date = null
	try {
		date = formatter.parse(newDateString)
	} catch (Exception e) {
		throw new RuntimeException("Custom Function getStartDateOfTimePeriod: Could not parse Date '" + newDateString + "'.")
	}

	if (timingQualf.equals("D")) {
		resultString = newDateString
	} else {
		Calendar calendar = Calendar.getInstance(locale)
		calendar.setTime(date)
		if (timingQualf.equals("W")) { // weekly
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
		} else if (timingQualf.equals("M")) { // monthly
			calendar.set(Calendar.DAY_OF_MONTH, 1)
		} else {
			throw new RuntimeException("Custom Function getEndDateOfTimePeriod: unexspected time period qualifier '" + timingQualf + "'.")
		}
		resultString = formatter.format(calendar.getTime())
	}

	return resultString
}

/**
 * Gets the oldest date of one or more date strings (yyyyMMdd).
 * Execution mode: Single value
 *
 * @param dates Dates
 * @param output Output
 * @param context Context
 * @return the oldest date.
 */
public def void getStartOfOneOrTwoDates(String[] dates, Output output, MappingContext context) {
	if (dates != null && dates.length > 0) {
		String startDate = null
		if (dates.length < 2) {
			startDate = dates[0]
		}
		if (startDate == null) {
			String date_0 = dates[0].trim()
			if (date_0.length() == 0 || !output.isSuppress(date_0) || "0".equals(date_0)) {
				startDate = dates[0]
			}
			if (startDate == null) {
				String date_1 = dates[1].trim()
				if (date_1.length() == 0 || !output.isSuppress(date_1) || "0".equals(date_1)) {
					startDate = dates[1]
				}
			}
			if (startDate == null) {
				Locale locale = Locale.getDefault()
				DateFormat formatter = new SimpleDateFormat("yyyyMMdd", locale)
				formatter.setLenient(false)
				Date date1 = null
				Date date2 = null
				try {
					date1 = formatter.parse(dates[0])
					date2 = formatter.parse(dates[1])
				} catch (ParseException e) {
					// ignore and try again
				}
				if (date1 == null || date2 == null) {
					try {
						date1 = formatter.parse(dates[0].substring(0,8))
						date2 = formatter.parse(dates[1].substring(0,8))
					} catch (ParseException e) {
						throw new RuntimeException("Custom Function getStartOfOneOrTwoDates: Could not parse Date '" + dates[0] + "' and/or '" + dates[1] + "'.")
					}
				}

				if (date1.before(date2)) {
					startDate = formatter.format(date1)
				} else {
					startDate = formatter.format(date2)
				}
			}
		}
		output.addValue(startDate)
	}
}

/**
 * Creates a time value from the first argument using the second argument (an EDIFACT-F2379-conform qualifier).
 * If the third argument has value 'true' the time format is (hhmmss) otherwise (hhmm). An empty value returns, if EDIFACT qualifier has no time component.
 * Execution mode: All values of context
 *
 * @param dateValues Date values
 * @param dateFormatValues Date format values
 * @param longFormatValues Long format values
 * @param output Output
 * @param context Context
 * @return formatted time values.
 */
public def void getTime(String[] dateValues, String[] dateFormatValues, String[] longFormatValues, Output output, MappingContext context) {
	final String SUPPRESS = "_sUpPresSeD_"
	if (dateValues != null && dateValues.length > 0) {
		if (dateFormatValues != null && dateValues.length != dateFormatValues.length) {
			throw new IllegalStateException("Custom Function getTime: dateValues array and dateFormatValues array have different lenght.")
		}

		if (!"true".equalsIgnoreCase(longFormatValues[0]) && !"false".equalsIgnoreCase(longFormatValues[0])) {
			throw new IllegalArgumentException("Custom Function getTime: unvalid longFormatValues '" + longFormatValues[0] + "'. Please use constant 'true' or 'false'.")
		}

		// long format: seconds inclusive
		boolean longFormat = Boolean.parseBoolean(longFormatValues[0])
		try {
			for (int i = 0; i < dateValues.length; i++) {
				String dateValue = dateValues[i].trim()
				int dateLength = dateValue.length()
				String dateFormat = dateFormatValues[i]
				int dtmFormat = Integer.parseInt(dateFormat)
				boolean unexpectedLength = false
				String date = null
				
				if (dateValue == null || output.isSuppress(dateValue) || dateValue.trim().length() == 0 || "0".equals(dateValue)) {
					output.addSuppress()
				} else {
					try {
						switch (dtmFormat) {
							case 2: // DDMMYY
								break
							case 3: // MMDDYY
								break
							case 101: // YYMMDD
								break
							case 102: // CCYYMMDD
								break
							case 201: // YYMMDDHHMM
								if (dateLength != 10) {
									unexpectedLength = true
								} else {
									date = dateValue.substring(6)
								}
								break
							case 202: // YYMMDDHHMMSS
								if (dateLength != 12) {
									unexpectedLength = true
								} else {
									date = dateValue.substring(6)
								}
								break
							case 203: // CCYYMMDDHHMM
								if (dateLength != 12) {
									unexpectedLength = true
								} else {
									date = dateValue.substring(8)
								}
								break
							case 204: // CCYYMMDDHHMMSS
								if (dateLength != 14) {
									unexpectedLength = true
								} else {
									date = dateValue.substring(8)
								}
								break
							case 401: // HHMM
								if (dateLength != 4) {
									unexpectedLength = true
								} else {
									date = dateValue
								}
								break
							case 402: // HHMMSS
								if (dateLength != 6) {
									unexpectedLength = true
								} else {
									date = dateValue
								}
								break
							case 609: // YYMM
								break
							case 610: // CCYYMM
								break
							case 615: // YYWW
								break
							case 616: // CCYYWW
								break
							case 715: // YYWW-YYWW
								break
							case 716: // CCYYWW-CCYYWW
								break
							case 718: // CCYYMMDD-CCYYMMDD
								break
							default:
								throw new RuntimeException("Custom Function getTime: Unexpected date time EDIFACT qualifier '" + dateFormat + "'.")
						}

						if (unexpectedLength) {
							throw new NumberFormatException("Custom Function getTime: Length of date string '" + date + "' does not fit date format '" + dateFormat + "'.")
						}

						// Format and set time
						if (date == null) {
							date = SUPPRESS
						} else {
							int dateLengthDate = date.length()
							if (!longFormat && dateLengthDate > 4) {
								date = date.substring(0, 4)
							} else if (!longFormat && dateLengthDate < 4) {
								date = date + "0000"
								date = date.substring(0, 4)
							} else if (longFormat && dateLengthDate < 6) {
								date = date + "000000"
								date = date.substring(0, 6)
							} else if (longFormat && dateLengthDate > 6) {
								date = date.substring(0, 6)
							}
						}
						output.addValue(date)
					} catch (RuntimeException re) {
						//date = SUPPRESS
						throw new RuntimeException("Custom Function getTime: dateValue '" + dateValue + "', dateFormat '" + dateFormat + "causes '" + re + "'.")
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function getTime: " + ex, ex)
		}
	}
}