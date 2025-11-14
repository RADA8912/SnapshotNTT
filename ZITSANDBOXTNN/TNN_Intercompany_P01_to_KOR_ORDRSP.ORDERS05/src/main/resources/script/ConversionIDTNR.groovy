import com.sap.it.api.mapping.*

def void customFunc(String[] input, Output output, MappingContext context) {
    
    def IDTNR = input[0]
    def splitString;
    def stringLength;
    String convIDTNR;
    int IDTNRNo;
    
    // If IDTNR is a number 
    if(IDTNR.isNumber()){
        def number = Long.valueOf(IDTNR)
        output.addValue(number)
    }
    // If IDTNR contains "/"
    else if (IDTNR.contains("/")) {
        splitString = IDTNR.split("/")
        stringLength = splitString[0].length()
        IDTNRNo = Long.valueOf(splitString[0])
        
        if (stringLength < 7) {
            convIDTNR = String.format("%07d", IDTNRNo);
            output.addValue(convIDTNR)
        }else{
            output.addValue(IDTNRNo)
        }
    }
    // If IDTNR contains ":"
    else if (IDTNR.contains(":")) {
        splitString = IDTNR.split(":")
        stringLength = splitString[0].length()
        IDTNRNo = Long.valueOf(splitString[0])

        if (stringLength < 7) {
            convIDTNR = String.format("%07d", IDTNRNo);
            output.addValue(convIDTNR)
        }else{
         output.addValue(IDTNRNo)
        }
    }
    // If IDTNR contains "-"
    else if (IDTNR.contains("-")) {
        splitString = IDTNR.split("-")
        stringLength = splitString[0].length()
        IDTNRNo = Long.valueOf(splitString[0])

        if (stringLength < 7) {
            convIDTNR = String.format("%07d", IDTNRNo);
            output.addValue(convIDTNR)
        }else{
              output.addValue(IDTNRNo)
        }
    }else{
          output.addValue(IDTNR)
    }
}