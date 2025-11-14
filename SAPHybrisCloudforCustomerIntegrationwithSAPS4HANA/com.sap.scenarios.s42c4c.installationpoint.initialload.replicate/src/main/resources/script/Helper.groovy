import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message prepareFilterQuery(Message message) {
   
       def map = message.getProperties();
      
       def LastChangeDateTimeFrom = map.get("p_LastChangeDateTimeFrom");
       def LastChangeDateTimeTo = map.get("p_LastChangeDateTimeTo");
       def query = "LastChangeDateTime ge datetimeoffset" + "'" + LastChangeDateTimeFrom + "' and LastChangeDateTime le datetimeoffset"+ "'" + LastChangeDateTimeTo + "'";
       
       def additionalFilterCondition = map.get("P_OptionalFilter");
       if (additionalFilterCondition.length() > 0){
           query = query + ' and ' + additionalFilterCondition
       }
            
       if (map.get("p_skiprecords")){
            Integer skiprecords = map.get("p_skiprecords").toInteger();
            if (skiprecords > 0) {
                query = query + "&" + "\$" + "skip=" + skiprecords.toString();
            }
       }
        
       message.setProperty("filterQuery", query);
       
       return message;
}



def Message calculateSkipIndex(Message message) {
    
     Integer  lastSkipCount = 0;
     Integer  currentSkipCount = 0;
     Integer  pageSize = 0;
  
       def map = message.getProperties();
       
       if (map.get("p_skiprecords")){
           lastSkipCount = map.get("p_skiprecords").toInteger();
        }
        if (map.get("SkipRecords_S4.FunctionalLoc_R")){
            currentSkipCount = map.get("SkipRecords_S4.FunctionalLoc_R").toInteger();
        }
        if (map.get("p_pageSize")){
            pageSize = map.get("p_pageSize").toInteger();
        }
       
       Integer  skipIndex = (lastSkipCount + currentSkipCount) - pageSize;
       if (skipIndex < 101){
            skipIndex = 0;
       }
       message.setProperty("skipIndexNew" , skipIndex.toString());
       return message;
}