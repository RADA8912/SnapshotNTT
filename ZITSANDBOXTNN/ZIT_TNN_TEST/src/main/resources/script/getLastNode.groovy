import com.sap.it.api.mapping.*;

//Add Output parameter to assign the output value.
def void custFunc2(String[] is, Output output, MappingContext context) {      
       String conText="" 
       int len=is.length;
       conText=is[len-1];
       output.addValue(conText);
}