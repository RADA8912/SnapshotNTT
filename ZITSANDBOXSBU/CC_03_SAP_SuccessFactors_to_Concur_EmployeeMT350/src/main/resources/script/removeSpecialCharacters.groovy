import com.sap.it.api.mapping.*;

public def String removeAllSpecialCharacters(String value) {
	String returnValue = ""

	if (value != null) {
		// Use RegEx for remove all special characters
		returnValue = value.replaceAll("[^a-zA-Z0-9/.,: _+-]+","")
	}

	return returnValue
}