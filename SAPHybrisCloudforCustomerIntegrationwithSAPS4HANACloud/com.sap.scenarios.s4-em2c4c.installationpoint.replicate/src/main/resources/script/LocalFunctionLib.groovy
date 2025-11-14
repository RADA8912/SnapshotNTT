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

def String getMessageID(String arg1,MappingContext context){
    String messageID = java.util.UUID.randomUUID().toString()
	return messageID.toUpperCase().replaceAll("-","") 
}

def String formatUUID(String uuid){	
	return (uuid.substring(0,8)+"-"+uuid.substring(8, 12)+"-"+uuid.substring(12,16)+"-"+uuid.substring(16,20)+"-"+uuid.substring(20));
}

def String SystemID(String systemID, MappingContext context){
	return context.getProperty(systemID);
}

def String determineStatus(String FuncnlLocIsMarkedForDeletion,String FuncnlLocIsDeleted,String FunctionalLocationIsActive,String FuncnlLocIsDeactivated,MappingContext context){
      if(FuncnlLocIsMarkedForDeletion == "true" || FuncnlLocIsDeleted == "true")
         return "4";
       else if (FuncnlLocIsDeactivated == "true") 
         return "3";  
       else if (FunctionalLocationIsActive == "true")
         return "2";
       else
         return "1";
}        

def String getSenderSequenceNo(String lastChangeDateTime,MappingContext context){
    //The etag from S/4 is of format W/"'SADL-020201207134153C~20201207134153'" and we extract the timestamp 20201207134153 using substring
    
    def _etag = context.getHeader('etag')
    if (_etag != null)
        return _etag.substring(26,40)
    else
        return lastChangeDateTime
}












