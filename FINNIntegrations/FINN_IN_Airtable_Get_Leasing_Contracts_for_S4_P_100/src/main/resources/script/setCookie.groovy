import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.*;
import java.io.*;
 
 
/* This little scripts sets the cookie that is needed for the subsuqent 
*  calls to SAP.
*/
 
def Message processData(Message message) 
{
    def headers = message.getHeaders();
    def cookie = headers.get("Set-Cookie");
    StringBuffer bufferedCookie = new StringBuffer();
    for (Object item : cookie) 
    {
        bufferedCookie.append(item + "; ");      
    }
    message.setHeader("Cookie", bufferedCookie.toString());
    

    
    
    return message;
}
 