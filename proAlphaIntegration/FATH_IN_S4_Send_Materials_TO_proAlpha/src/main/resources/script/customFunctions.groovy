import com.sap.it.api.mapping.*;
import groovy.util.*;
import groovy.json.*;


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


def String getProperty(String propertyName,MappingContext context) 
{ 
    String PropertyValue= context.getProperty(propertyName); 
    PropertyValue= PropertyValue.toString(); 
    return PropertyValue; 
    
}

//Lesen von Header Variablen 
def String getHeader(String headerName,MappingContext context) 
{
    String HeaderValue = context.getHeader(headerName); 
    HeaderValue= HeaderValue.toString();
    return HeaderValue; 
    
}



/**
 * 
 * Convert ATFLV from floating to correct data type representation for the PIM system.
 * Therefore get all relevant values and build up a dictionary to 
 * dynamically access the relevant data.
 **/
 def String convertCharateristics(String charsBody, String atnam, String atflv, String atwrt) {
	
    //Parse Characteristics with its data types. Therefore, build up a HashMap
    def jsonBody = new JsonSlurper().parseText(charsBody)
    
    //Iterate through Charastics and DataTypes
    Map<String,String> dataTypeMap  = new HashMap<>();

    for (node in jsonBody.d.results){
        dataTypeMap.put(node.Characteristic,node.CharcDataType)
    }

    //Now convert the Body accordingly
    //Get the data type for given atnam. See SAP NOTE 1568641 for conversions.
    def dataTypeForSpecificValue = dataTypeMap.get(atnam)
    
    switch (dataTypeForSpecificValue){

        case "NUM":
            BigDecimal res_atflv = (Double.parseDouble(atflv))
            return res_atflv

        case "DATE":
            //Remove "." to have the year in correct format
            def year = atflv.substring(0, 5).replace(".", "")
            def month = atflv.substring(5, 7)
            def day = atflv.substring(7, 9)

            return year + "-" + month + "-" + day

        
        //Return CHAR value which is awtwrt
        case "CHAR":
            return atwrt





        case "TIME":
        //Remove "." to have the correct format
        def hour = atflv.substring(0, 3).replace(".", "")
        def mins = atflv.substring(3, 5)
        def seconds = atflv.substring(5, 7)

        return hour + ":" + mins + ":" + seconds

    }



	return "0"
}




/**
 * 
 * This script converts the SAP MSTAE to the corresponding proAlpha status.
 * For the MSTAE 99 the MMSTA from MARC must be considered additionally for the mapping. 
 * Furthermore, there is a special handling for status 02.
 **/
 
 def String convertMaterialStatus(String mstae, String mmsta) {
	
    //If status is x (coming from the value mapping for mstae 02), set the value to 9.
    if (mstae == 'x'){
        return "9"
    }
    //If status is 997 (coming from the value mapping for mstae 99) and plant specific status is 40, return 6
    else if (mstae == '99' & mmsta == '40'){
        return "6"
    }
    //Otherwise return the given status
    else {
        return mstae
    }
  
    return mstae
  
  
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
 * Removes leading zeros.
 * Execution mode: Single value 
 *
 * @param value Value
 * @return input number without leading zeros.
 */
public def String trimZeroLeft(String value) {
	String output = ""

	if (value != null) {
		if (value.trim().length() == 0) {
			output = value
		} else {
			output = value.replaceAll("^0*", "")
			if (output.trim().length() == 0) {
				output = "0"
			}
		}
	} else {
		output = value
	}

	return output
}




def String convertWeight(String weight, String weight_unit, String base_unit, MappingContext context) {
    if (!weight || !weight_unit || !base_unit) {
        return ""
    }

    // Normalize to uppercase for SAP unit codes
    weight_unit = weight_unit.toUpperCase()
    base_unit = base_unit.toUpperCase()

    BigDecimal value
    try {
        value = new BigDecimal(weight)
    } catch (Exception e) {
        return ""
    }

    // SAP standard unit conversion factors to KG (KGM)
    def toKGM = [
        "KGM": 1.0,          // Kilogram
        "GRM": 0.001,        // Gram
        "MGM": 0.000001,     // Milligram
        "LBR": 0.453592,     // Pound
        "ONZ": 0.0283495     // Ounce
    ]

    if (!toKGM.containsKey(weight_unit) || !toKGM.containsKey(base_unit)) {
        return ""
    }

    // Convert input to kilograms
    BigDecimal inKGM = value * new BigDecimal(toKGM[weight_unit].toString())

    // Convert kilograms to target unit
    BigDecimal result = inKGM / new BigDecimal(toKGM[base_unit].toString())

    // Return rounded result (3 decimal places)
    return result.setScale(3, BigDecimal.ROUND_HALF_UP).toPlainString()
}


def String convertVolume(String volume, String volume_unit, String base_unit, MappingContext context) {
    if (!volume || !volume_unit || !base_unit) {
        return ""
    }

    // Normalize to uppercase for SAP unit codes
    volume_unit = volume_unit.toUpperCase()
    base_unit = base_unit.toUpperCase()

    BigDecimal value
    try {
        value = new BigDecimal(volume)
    } catch (Exception e) {
        return ""
    }

    // SAP standard volume unit conversion factors to LTR (liter)
    def toLTR = [
        "LTR": 1.0,           // Liter
        "MLT": 0.001,         // Milliliter
        "MTQ": 1000.0,        // Cubic Meter = 1000 Liters
        "CMQ": 0.001,         // Cubic Centimeter = 0.001 Liters
        "GLL": 3.78541,       // US Gallon = ~3.785 Liters
        "DMQ": 1.0,           // Cubic Decimeter = 1.0 Liters
        "MMQ": 0.000001       // Cubic Milimeter = 0.000001 Liters

    ]

    if (!toLTR.containsKey(volume_unit) || !toLTR.containsKey(base_unit)) {
        return ""
    }

    // Convert input to liters
    BigDecimal inLTR = value * new BigDecimal(toLTR[volume_unit].toString())

    // Convert liters to target unit
    BigDecimal result = inLTR / new BigDecimal(toLTR[base_unit].toString())

    // Return rounded result (3 decimal places)
    return result.setScale(3, BigDecimal.ROUND_HALF_UP).toPlainString()
}
/**
 * Gets "true" if the argument is not empty, "false" otherwise.
 * Execution mode: All values of context
 *
 * @param contextValues Context values
 * @param output Output
 * @param context Mapping context
 * @return context with boolean values.
 */
public def void existsAndHasValue(String[] contextValues, Output output, MappingContext context) {
	if (contextValues != null && contextValues.length > 0) {
		for (int i = 0; i < contextValues.length; i++) {
			if (contextValues[i] != null && contextValues[i].trim().length() > 0 && !output.isSuppress(contextValues[i])) {
				output.addValue("true")
			} else {
				output.addValue("false")
			}
		}
	} else {
		output.addValue("false")
	}
}
