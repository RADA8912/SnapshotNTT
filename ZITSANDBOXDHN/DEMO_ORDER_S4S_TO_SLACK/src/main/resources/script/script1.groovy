import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap
import java.util.ArrayList
import java.util.Map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import groovy.xml.*
import java.io.*;

def Message processData(Message message){
      
     def headers = message.getHeaders();
     def cookie = headers.get("Set-Cookie");
     StringBuffer bufferedCookie = new StringBuffer();
    
     for(Object item : cookie){
          bufferedCookie.append(item + ";");
     }
    
     message.setHeader("Cookie", bufferedCookie.toString());
     Logger log = LoggerFactory.getLogger(this.getClass());
     log.error("cookie"+ bufferedCookie);
     
      return message;
}