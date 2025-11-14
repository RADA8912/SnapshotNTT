import com.sap.it.api.mapping.*
import java.math.*
import java.text.DecimalFormat

/**
 * Format exponential number or other number to decimal. First argument is exponential value. Second argument is decimal format for example '0.00'.
 * Formats '3.5E4' to '35000.00'.
 * Execution mode: Single value
 *
 * @param exponentialValue Exponential value
 * @param decimalFormat Decimal format
 * @return decimal value.
 */
public def String formatExponential(String exponentialValue, String decimalFormat){
	String returnValue = ""

	// Set default format
	if (decimalFormat.length() == 0) {
		decimalFormat = "0.00"
	}

	if (exponentialValue.length() > 0) {
		// Set decimal format
		DecimalFormat formatter = new DecimalFormat(decimalFormat)
		
		// Format Exponential
		BigDecimal bdValue = new BigDecimal(exponentialValue)
		returnValue = formatter.format(bdValue)
	}

	return returnValue
}