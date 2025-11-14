import com.sap.it.api.mapping.*;

def String customFunc1(String P1,String P2,MappingContext context) {
         String value1 = context.getHeader(P1);
         String value2 = context.getProperty(P2);
         return value1+value2;
}

def void custFunc2(String[] is,String[] ps, Output output, MappingContext context) {
        String value1 = context.getHeader(is[0]);
        String value2 = context.getProperty(ps[0]);
        output.addValue(value1);
        output.addValue(value2);
}

/*def String customFunc(String arg1){
	return arg1 
}*/

def void copyPerValue(String[] propertyName, String[] HeaderName, Output output, MappingContext context){
	for (int i=0; i<HeaderName.length; i++){
	    output.addValue(propertyName[0]);
	}
}