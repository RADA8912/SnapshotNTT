import com.sap.it.api.mapping.*;


//custom substring function
//beginIndex = startpoint where the string has to be cut off. Starting from zero
//endIndex = endpoint where the string has to be cut off. Starting from zero
//input = the input String

def String substringCustomFunction(int beginIndex, int endIndex, String input, MappingContext context) {
	
	//if the then lenght of the input string is greater than then the endIndex the string will be cut off at the endIndex
        if(input.length() > endIndex){
        	result = input.substring(beginIndex,endIndex);


	//if the input string length is less than the given beginIndex a empty string will be returned
    	}else if(input.length() < beginIndex){
        	result = ""

	//if the endIndex is less than then the given string it will be cut off at the input string length
    	}else{
        	result = input.substring(beginIndex,input.length());

    	}

	return result;
}