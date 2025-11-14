import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message prepareFilterQuery(Message message) {
   
       def map = message.getProperties();
      
       def LastChangeDateTimeFrom = map.get("p_LastChangeDateTimeFrom");
       def query = "ValidityEndDate eq datetime'9999-12-31T00:00:00' and LastChangeDateTime ge datetimeoffset"
       query = query + "'" + LastChangeDateTimeFrom + "' and LastChangeDateTime le datetimeoffset";
       def LastChangeDateTimeTo = map.get("p_LastChangeDateTimeTo");
       query = query + "'" + LastChangeDateTimeTo + "'";
       
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


def Message generateException(Message message) {
   
       def map = message.getProperties();
       def loopIndex = map.get("CamelLoopIndex");
       if (loopIndex >= 8)
        throw new Exception ("Explicit Exception");
       
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
        if (map.get("SkipRecords_S4.Equipment_R")){
            currentSkipCount = map.get("SkipRecords_S4.Equipment_R").toInteger();
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