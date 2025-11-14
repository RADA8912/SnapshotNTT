import com.sap.it.api.mapping.*;

//Add MappingContext as an additional argument to read or set Headers and properties.

def String getIsNet(String property, MappingContext context){
    
    String isNet= context.getProperty("isNet");
    
    if(isNet == 'true'){
         return  'true';
    }  else {
        return 'false';
    }
}