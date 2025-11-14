import com.sap.it.api.mapping.MappingContext
import com.sap.it.api.mapping.Output
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.text.ParseException

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