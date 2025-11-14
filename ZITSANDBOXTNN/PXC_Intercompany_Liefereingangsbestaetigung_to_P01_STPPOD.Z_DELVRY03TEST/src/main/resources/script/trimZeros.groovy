public def String trimRightAfterComma(String value) {
	String output = null
	int length = value.length()

	if (length > 0  && value.contains(",000")) {
	    //  \044 is the octal representation of $
		output = value.indexOf(",") < 0 ? s : value.replaceAll("0*\044", "").replaceAll("\\,\044", "");
		
		  // remove all dots from number
        output = output.replaceAll("[.]", "");
        
          // replace comma with dot for displaying decimal number 
        output = output.replaceAll(",", ".")
	} else {
		output = value
		
		// remove all dots from number
        output = output.replaceAll("[.]", "");
	}
	return output
}
