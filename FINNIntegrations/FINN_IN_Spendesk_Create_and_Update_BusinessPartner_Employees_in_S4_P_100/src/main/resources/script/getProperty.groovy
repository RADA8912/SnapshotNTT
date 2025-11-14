
import com.sap.it.api.mapping.* //Lesen von Exchange Properties
def String getProperty(String propertyName,MappingContext context) 
{ String PropertyValue= context.getProperty(propertyName); 
PropertyValue= PropertyValue.toString(); return PropertyValue; }
//Lesen von Header Variablen 
def String getHeader(String headerName,MappingContext context) 
{ String HeaderValue = context.getHeader(headerName); 
HeaderValue= HeaderValue.toString();
return HeaderValue; }


def String formatValueBySpace(String value, String length, String cutLengthDirection, String fillSpace, String fillSpaceDirection) {
	String output = null
	if (value != null) {
		int lengthInt
		try {
			lengthInt = Integer.parseInt(length)
		} catch (Exception ex) {
			throw new RuntimeException("Custom Function formatValueBySpace: length '" + length + "' is not a numeric value.")
		}

		int lengthValue = value.length()

		if (lengthValue > 0 && lengthValue != lengthInt) {
			if (lengthValue > lengthInt) {
				if ("left".equalsIgnoreCase(cutLengthDirection)) {
					int offset = lengthValue - lengthInt
					output = value.substring(offset, lengthValue)
				} else if ("right".equalsIgnoreCase(cutLengthDirection)) {
					output = value.substring(0, lengthInt)
				} else {
					throw new RuntimeException("Custom Function formatValueBySpace: unexpected value '" + cutLengthDirection + "' for the cutDirection")
				}
			} else {
				if ("true".equalsIgnoreCase(fillSpace)) {
					// now lengthValue < lengthInt
					int offset = lengthInt - lengthValue
					String SpaceString = "\u00A0"
					for (int i = 0; i < offset - 1; i++) {
						SpaceString += "\u00A0"
					}
					if ("left".equalsIgnoreCase(fillSpaceDirection)) {
						output = SpaceString + value
					} else if ("right".equalsIgnoreCase(fillSpaceDirection)) {
						output = value + SpaceString
					} else {
						throw new RuntimeException("Custom Function formatValueBySpace: unexpected value '" + fillSpaceDirection + "' for the fillDirection")
					}
				}
			}
		}
		if (output == null) {
			output = value
		}
	} else {
		output = value
	}

	return output
}