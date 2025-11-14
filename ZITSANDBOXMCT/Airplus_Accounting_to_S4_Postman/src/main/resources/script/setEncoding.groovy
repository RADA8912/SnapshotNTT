import java.io.*;
import java.nio.charset.Charset;
import com.sap.gateway.ip.core.customdev.util.Message
    
def Message processData(Message message) {
    try{
        // Get the payload as a byte array
        byte[] inputByteArray = message.getBody(byte[].class)        
        String inputString = new String(inputByteArray, Charset.forName("ISO-8859-1"))
        
        // set message string message to body
        message.setBody(inputString)
        return message
        
    } catch(Exception e){
        e.printStackTrace()
        throw e
    }
  }