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
 * Scrapvalue needs to be determined by the car sales net amount and the residual value.
 * If the net amount of the car sales is lesser than the residual value, this needs to be considered.
 *
 * */

def String determineScrapValue (String residualValue, String carSalesNetAmount){

    Double dblResidualValue = residualValue.toDouble()
    Double dblCarSalesNetAmount = 0.0

    if (carSalesNetAmount != ''){
        dblCarSalesNetAmount = carSalesNetAmount.toDouble()

    }

    //Check what value is bigger
    if (dblCarSalesNetAmount < dblResidualValue && dblCarSalesNetAmount != 0.0){
        return dblCarSalesNetAmount
    } else {
        return dblResidualValue
    }


    }
    
