
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
 
 def lineNo = 0;
 def lines = message.getBody(java.lang.String) as String;
    lines.eachLine {
    lineNo++} 
 
 message.setProperty("RowCount",lineNo);
 return message;
}