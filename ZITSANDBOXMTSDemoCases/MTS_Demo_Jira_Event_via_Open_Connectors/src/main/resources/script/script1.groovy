import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonSlurper;
def Message processData(Message message) {
        //Parse Json Body
        def body = message.getBody(java.lang.String) as String;
        def slurper = new JsonSlurper();
        def parsedJson = slurper.parseText(body);     
        def time=parsedJson.message.raw.worklog.timeSpent;
        def comment=parsedJson.message.raw.worklog.comment;
       //Headers 
       
        message.setProperty("time", time);
        message.setProperty("comment", comment);
        
        
        return message;
}