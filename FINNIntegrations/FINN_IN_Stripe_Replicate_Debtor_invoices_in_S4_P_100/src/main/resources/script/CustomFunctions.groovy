import com.sap.it.api.mapping.*;

/*Add MappingContext parameter to read or set headers and properties
def String customFunc1(String P1,String P2,MappingContext context) {
         String value1 = context.getHeader(P1);
         String value2 = context.getProperty(P2);
         return value1+value2;
}

Add Output parameter to assign the output value.
def void custFunc2(String[] is,String[] ps, Output output, MappingContext context) {
        String value1 = context.getHeader(is[0]);
        String value2 = context.getProperty(ps[0]);
        output.addValue(value1);
        output.addValue(value2);
}*/

public def String bigDecimalMod(String value, String modValue) {
	try {
			BigInteger valueBi = new BigDecimal(value).toBigInteger()
			BigInteger modValueBi = new BigDecimal(modValue).toBigInteger()
			return valueBi.mod(modValueBi).toString()
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function bigDecimalMod: the values '" + value + "', '" + modValue + "' cannot be transformed into a mod value.")
		}
}

def String getProperty(String propertyName,MappingContext context)  { 
    String PropertyValue= context.getProperty(propertyName); 
    PropertyValue= PropertyValue.toString(); 
    return PropertyValue; 
    
}




//Lesen von Header Variablen 
def String getHeader(String headerName,MappingContext context) { 
    String HeaderValue = context.getHeader(headerName); 
    HeaderValue= HeaderValue.toString();

    return HeaderValue; 
    
}


/**
 * Convert decimal numbers to integer for the determination of the year in the
 * holding period. If result is 1.5 we only need the 1.
 *
 * */

def String convertDecimalToInt (String decimalNumber){

    double resultDouble = decimalNumber.toDouble()

    return resultDouble.toInteger()
}







/**
 * Check whether itemTExt contains Car Sales to determine 
 * the correct internal order ID.
 *
 * */

def String determineInternalOrderID (String itemText, String companyCode, String orderID){

    if(orderID != ''){
        if (itemText.contains("Car Sales")){
            if (companyCode.equals("1010")){
                return "SA1_"
            }
            else {
                return "SA3_"
            }

        } else {
            if (companyCode.equals("1010")){
                return "SU1_"
            }
            else {
                return "SU3_"
            }
        }
    } else {
        return " "
    }

        return " "
    
}

/**
* Uses the first argument (the context should have exactly one value) as often as the length of the second context indicates.
* Execution mode: All values of context
*
* @param contextValues Context values
* @param secondContext Second context
* @param output Output
* @param context Mapping Context
* @return Return the second context filled with the value from the first argument.
*/
public def void simpleUseOneAsMany(String[] contextValues, String[] secondContext, Output output, MappingContext context) {
    if (contextValues != null && contextValues.length > 0 && secondContext != null && secondContext.length > 0) {
        String value = null
        if (contextValues.length == 1) {
            value = contextValues[0]
        } else {
            for (int i = 0; i < contextValues.length; i++) {
                if (!output.isSuppress(contextValues[i])) {
                    value = contextValues[i]
                    break
                }
            }
            if (value == null) {
                value = contextValues[0]
            }
        }
        for (int i = 0; i < secondContext.length; i++) {
            output.addValue(value)
        }
    }
}



/**
* This method checks whether the underlying invoice is related to a deposit. If yes, 'true' is returned.
* It is determined based on the itemTextGL that is passed by the DWH.
* @param itemTextGL
* @return The boolean value as String.
*/
public def String invoiceRelatedToDeposit (String itemTextGL, String GlAccount) {
    
    if (itemTextGL == 'Deposit' || itemTextGL == 'Deposit Reclaim Lease Out' || itemTextGL == 'Deposit Credit Note' || GlAccount == '1111111111'){
        return 'true'
    } else {
        return 'false'
    }
  
}