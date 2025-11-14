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

def void getNames(String[] names, String[] position, Output output, MappingContext context) {
    
    if (!(names == null || names.length == 0))
    {
        
    if (position[0] == 'last')
    {
       output.addValue(names.last()); 
    }
    else 
    {
        def firstname = ""
        for(int j=0; j<(names.length -1) ; j++)
    {
        firstname = firstname + " " + names[j]
    }
         output.addValue(firstname.trim());
    }

    }
}
