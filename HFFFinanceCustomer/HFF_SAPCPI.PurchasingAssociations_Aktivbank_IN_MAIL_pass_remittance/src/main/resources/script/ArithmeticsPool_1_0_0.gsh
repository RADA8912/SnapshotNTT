/**
* Groovy Custom Functions Arithmetics Pool
*
* @author nttdata-solutions.com
* @version 1.0.0
*/

import com.sap.it.api.mapping.*
import java.math.*
import java.text.DecimalFormat

/**
 * Adds two values.
 * Execution mode: Single value
 *
 * @param summand1 Summand 1
 * @param summand2 Summand 2
 * @return the result of the addition.
 */
public def String bigDecimalAdd(String summand1, String summand2) {
	try {
		BigDecimal summand1D = new BigDecimal(summand1)
		BigDecimal summand2D = new BigDecimal(summand2)
		return summand1D.add(summand2D).toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalAdd: the values '" + summand1 + "', '" + summand2 + "' cannot be transformed into a addition.")
	}
}

/**
 * Divides the first value by the second value, rounding to the number of decimal places given by the third value.
 * Execution mode: Single value
 *
 * @param dividend Dividend
 * @param divisor Divisor
 * @param precision Precision
 * @return the result of the division.
 */
public def String bigDecimalDivide(String dividend, String divisor, String precision) {
	try {
		BigDecimal dividentD = new BigDecimal(dividend)
		BigDecimal divisorD = new BigDecimal(divisor)

		return dividentD.divide(divisorD, Integer.parseInt(precision), BigDecimal.ROUND_HALF_UP).toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalDivide: the values '" + dividend + "', '" + divisor + "' cannot be transformed into a division.")
	}
}

/**
 * Inserts a decimal point at given index if possible (if index is out of range, it simply returns the number).
 * Execution mode: Single value
 *
 * @param number Number
 * @param splitIndex Split index
 * @return the formatted number.
 */
public def String bigDecimalFormatNum(String number, String splitIndex) {
	// Try to split or if value is shorter than position simply returns it
	if(number != null && splitIndex != null) {
		try {
			BigInteger splitIndexBi = new BigDecimal(splitIndex).toBigInteger()
			
			if(number.length() > splitIndexBi.intValue() ) {
				return number.substring(0, number.length() - splitIndexBi.intValue()) + "." + number.substring(number.length() - splitIndexBi.intValue(), number.length())
			} else {
				return number
			}
		} catch (NumberFormatException nfe) {
			throw new RuntimeException("Custom Function bigDecimalFormatNum: " + nfe, nfe)
		}
	}
	return null
}

/**
 * Multiplies two values.
 * Execution mode: Single value
 *
 * @param factor1 Factor 1
 * @param factor2 Factor 2
 * @return the result of the multiplication.
 */
public def String bigDecimalMultiply(String factor1, String factor2) {
	try {
		BigDecimal factor1D = new BigDecimal(factor1)
		BigDecimal factor2D = new BigDecimal(factor2)
		return factor1D.multiply(factor2D).toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalMultiply: the values '" + factor1 + "', '" + factor2 + "' cannot be transformed into a multiplication.")
	}
}

/**
 * Gets the remainder of dividing the first value by the second value.
 * Execution mode: Single value
 *
 * @param value Value
 * @param modValue Mod value
 * @return the remainder.
 */
public def String bigDecimalMod(String value, String modValue) {
	try {
			BigInteger valueBi = new BigDecimal(value).toBigInteger()
			BigInteger modValueBi = new BigDecimal(modValue).toBigInteger()
			return valueBi.mod(modValueBi).toString()
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function bigDecimalMod: the values '" + value + "', '" + modValue + "' cannot be transformed into a mod value.")
		}
}


/**
 * Gets reciprocal value (german 'Kehrwert') of first value with same number of decimal places. 
 * Execution mode: Single value
 *
 * @param value Value
 * @return the reciprocal value.
 */
public def String bigDecimalReciprocalValue(String value) {
	final String SUPPRESS = "_sUpPresSeD_"
	String returnValue = ""
	
	try {
		if (value.equals(SUPPRESS)) {
			returnValue = SUPPRESS
		} else if (value != null && value.length() != 0) {
			BigDecimal bigValue = new BigDecimal(value)
			bigValue = BigDecimal.ONE.divide(bigValue, bigValue.scale(), RoundingMode.HALF_UP)
			returnValue = bigValue.toString()
		}
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalReciprocalValue: the value '" + value + "' cannot be transformed.")
	}
	
	return returnValue
}

/**
 * Rounds the first value to the number of decimal places given by the second value.
 * Execution mode: Single value
 *
 * @param value Value
 * @param precision Precision
 * @return the rounded value.
 */
public def String bigDecimalRound(String value, String precision) {
	try {
		int precisionInt = Integer.parseInt(precision)
		BigDecimal d = new BigDecimal(value).setScale(precisionInt, BigDecimal.ROUND_HALF_UP)

		return d.toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalRound: the value '" + value + "', '" + precision + "' cannot be transformed into a rounding.")
	}
}

/**
 * Rounds up the first value to the number of decimal places given by the second value.
 * Execution mode: Single value
 *
 * @param value Value
 * @param precision Precision
 * @return the rounded value.
 */
public def String bigDecimalRoundUp(String value, String precision) {
	try {
		int precisionInt = Integer.parseInt(precision)
		BigDecimal d = new BigDecimal(value).setScale(precisionInt, BigDecimal.ROUND_UP)

		return d.toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalRound: the value '" + value + "', '" + precision + "' cannot be transformed into a rounding.")
	}
}

/**
 * Subtracts the second value from the first value.
 * Execution mode: Single value
 *
 * @param minuend Minuend
 * @param subtrahend Subtrahend
 * @return the result of the subtraction.
 */
public def String bigDecimalSubtract(String minuend, String subtrahend) {
	try {
		BigDecimal minuendD = new BigDecimal(minuend)
		BigDecimal subtrahendD = new BigDecimal(subtrahend)
		return minuendD.subtract(subtrahendD).toString()
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalSubtract: the values '" + minuend + "', '" + subtrahend + "' cannot be transformed into a subtraction.")
	}
}

/**
 * Calulates the sum of all context values.
 * Execution mode: All values of context
 *
 * @param values Values
 * @param output Output
 * @param context Context
 * @return the sum of the context values.
 */
public def void bigDecimalSum(String[] values, Output output, MappingContext context) {
	try {
		BigDecimal sum = new BigDecimal("0")
		for (int i = 0; i < values.length; i++) {
			if (output.isSuppress(values[i])) {
				continue
			}
			BigDecimal valueD = new BigDecimal(values[i])
			sum = sum.add(valueD)
		}
			output.addValue(sum.toString())
	} catch (Exception ex) {
		throw new RuntimeException("Custom Function bigDecimalSum: the values '" + Arrays.asList(values) + "' cannot be transformed into a sum.")
	}
}

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