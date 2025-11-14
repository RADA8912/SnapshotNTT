import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import static java.util.Calendar.*;
import java.util.Map;
import com.sap.it.api.mapping.*;
import com.sap.it.api.mapping.MappingContext;
import java.text.SimpleDateFormat;
import java.util.Date;

def Message processData(Message message) {   
    
    def properties = message.getProperties();
    
    def currentTime = properties.get("CurrentTime");
    def initLogStart = properties.get("initLogStart");
    
    def f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    def d = f.parse(currentTime);
    def x = message.properties.get("minutes");
    
    def minutes = x.toInteger();
    
    def cal = Calendar.getInstance();
    cal.setTime(d);
    cal.add(Calendar.MINUTE, - minutes);
  
    
    def dateEnd = f.format(cal.getTime());
    
    message.setProperty("initLogStart", dateEnd);    
    return message;
}