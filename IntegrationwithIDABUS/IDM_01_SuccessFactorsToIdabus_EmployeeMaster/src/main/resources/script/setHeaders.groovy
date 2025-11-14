import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
    map = message.getHeaders();
    def top = map.get("top");
	def skip = map.get("skip");
	def personIdExternal = map.get("personIdExternal");
	def effectiveDate = map.get("effectiveDate");
	
	
    def params = [:]
    (message.getHeaders().CamelHttpQuery =~ /(\w+)=?([^&]+)?/)[0..-1].each{
        params[it[1]] = it[2]
    }
    
    if (effectiveDate == null && params.effectiveDate != null) 
        message.setHeader("effectiveDate", params.effectiveDate);
    
    if (personIdExternal == null && params.personIdExternal != null) 
        message.setHeader("personIdExternal", params.personIdExternal);
    
    if (top == null && params.top != null) 
        message.setHeader("top", params.top);
	
	if (skip == null && params.skip != null) 
	    message.setHeader("skip", params.skip);
	
    return message;
}