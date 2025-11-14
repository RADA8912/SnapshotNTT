import com.sap.gateway.ip.core.customdev.util.Message
import java.time.*

/**
* RunOnStartDate
* This Groovy script is an extended start timer to start run at a given date for example 2022-12-31.
* 
* Groovy script exchange property
* - StartTimerExtended.StartDate = Start date with format yyyy-MM-dd
* - StartTimerExtended.ZoneID = Zone ID for example 'Asia/Tokyo'
*
* Groovy script read only properties
* - StartTimerExtended.Type = 'RunOnStartDate'
* - StartTimerExtended.Run = 'true' or 'false' use it in router condition ${property.StartTimerExtended.Run} = 'true'
* - StartTimerExtended.Debug = Debug info
*
* Custom Header Properties
* - StartTimerExtended.Run = 'true' or 'false'
* - StartTimerExtended.Message = Message
* - StartTimerExtended.Debug = Debug info
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) {
	// Set the default time zone
	final String DEFAULT_ZONE_ID = 'Europe/Berlin'

	// For example start run at date 2022-12-31
	String runOnStartDate = getExchangeProperty(message, "StartTimerExtended.StartDate", true)

	// Set timer type
	message.setProperty("StartTimerExtended.Type", "RunOnStartDate")
	
	// Set default value for property 'StartTimerExtended.Run' to false
	message.setProperty("StartTimerExtended.Run", "false")

	def messageLog = messageLogFactory.getMessageLog(message)

	// Get Zone ID from exchange property
	String zoneID = getExchangeProperty(message, "StartTimerExtended.ZoneID", false)
	if (zoneID == null || zoneID.length() == 0) {
		zoneID = DEFAULT_ZONE_ID
	}

	// Check if is valid time zone id configured
	if (zoneID != DEFAULT_ZONE_ID) {
		boolean validTimeZone = isValidTimeZone(zoneID)
		if (validTimeZone == false) {
			throw Exception("The zone id: '$zoneID' is not valid.")
		}
	}

	// Get the time zone for the given String for example for 'Europe/Berlin'
	ZoneId timeZone = ZoneId.of(zoneID) 

	// Get the current date time as ZonedDateTime object for the given time zone
	ZonedDateTime zonedDateTimeNow = ZonedDateTime.now(ZoneId.of(zoneID))

	// Parse the runOnStartDate String to ZonedDateTime object
	LocalDate localDate
	LocalDateTime localDateTime
	ZonedDateTime zonedDateTime
	try {
		localDate = LocalDate.parse(runOnStartDate);
		localDateTime = localDate.atStartOfDay();  
		zonedDateTime = ZonedDateTime.of(localDateTime, timeZone)
	} catch(Exception e) {
		throw Exception("The date: '$runOnStartDate' of the property StartTimerExtended.StartDate is invalid! Check if the defined date is valid and the date format is yyyy-MM-dd.")
	}

	// Set the condition for router step in the integration flow
	if (zonedDateTimeNow.isAfter(zonedDateTime)) {
		message.setProperty("StartTimerExtended.Run", "true")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Run", "true")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Message", "Runnig because now is past defined start date.")
		messageLog.setStringProperty("StartTimerExtended.Run", "true")
		messageLog.setStringProperty("StartTimerExtended.Message", "Runnig because now is past defined start date.")
	} else {
		message.setProperty("StartTimerExtended.Run", "false")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Run", "false")
		messageLog.addCustomHeaderProperty("StartTimerExtended.Message", "No runnig because now is before defined start date.")
		messageLog.setStringProperty("StartTimerExtended.Run", "false")
		messageLog.setStringProperty("StartTimerExtended.Message", "No runnig because now is before defined start date.")
	}

	// Set debug info
	String debugInfo = "runOnStartDate: " + zonedDateTime + " currentDate: " + zonedDateTimeNow + " zoneID: " + zoneID
	message.setProperty("StartTimerExtended.Debug", debugInfo)
	messageLog.addCustomHeaderProperty("StartTimerExtended.Debug", debugInfo)

	return message
}

/**
 * getExchangeProperty
 * @param message This is message.
 * @param propertyName This is name of property.
 * @param mandatory This is parameter if property is mandatory.
 * @return propertyValue Return property value.
 */

private getExchangeProperty(Message message, String propertyName, boolean mandatory) {
	String propertyValue = message.properties.get(propertyName) as String
	if (mandatory) {
		if (propertyValue == null || propertyValue.length() == 0) {
			throw Exception("Mandatory exchange property '$propertyName' is missing.")
		}
	}
	return propertyValue
}

/**
 * isValidTimeZone
 * @param zoneID This is time zone id.
 * @return validation Return 'true' for valid and 'false' for invalid.
 */

private boolean isValidTimeZone(String zoneID) {
	for (String timeZoneID : TimeZone.getAvailableIDs()) {
		if (timeZoneID.equals(zoneID)) {
			return true
		}
	}
	return false
}