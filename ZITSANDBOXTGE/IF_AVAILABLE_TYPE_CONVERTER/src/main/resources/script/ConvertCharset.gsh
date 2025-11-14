import com.sap.gateway.ip.core.customdev.util.Message
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets
import com.sap.it.api.msglog.MessageLog;
import com.sap.it.api.msglog.MessageLogFactory;
import java.nio.charset.Charset

  	
/*
* ConvertEncoding
* This Groovy script convert the message (body) from one enconding (e.g. UTF16-BE to UTF-8) to another
* 
*
* Groovy script parameters (Header Parameters)
* - ConvertEncoding.ReplacementChar = characters which is used to replace unknown characters
* - ConvertEncoding.SetBom = True, sets the ByteOrderMark
* - ConvertEncoding.SourceEncoding = Encoding (Charset) of incoming message
* - ConvertEncoding.TargetEncoding = Encoding (Charset) of outgoing message
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) 
{
 	def bodyString = message.getBody(String)
    MessageLog messageLog = messageLogFactory.getMessageLog(message);
    
    def DEFAULT_SET_BOM_UTF8 = false;
    char replacementChar= '?';
    String sourceEncoding;
    String targetEncoding;
    
    try
    {
        replacementChar = message.getHeaders().get("ReplacementChar");
  	    messageLog.addCustomHeaderProperty("ReplacementChar", replacementChar);
    }
    catch(Exception e)
    {
        messageLog.addCustomHeaderProperty("Standardersetzungszeichen", replacementChar.toString());
    }
    
    try
    {
        sourceEncoding = message.getHeaders().get("SourceEncoding");
  	    messageLog.addCustomHeaderProperty("SourceEncoding", sourceEncoding);
    }
    catch(Exception e)
    {
        throw new IllegalArgumentException("Der Headerparameter 'SourceEncoding' darf nicht null sein.");
    }
   
    try
    {
    	targetEncoding = message.getHeaders().get("TargetEncoding");
        messageLog.addCustomHeaderProperty("TargetEncoding", targetEncoding);
    }
    catch(Exception e)
    {
        throw new IllegalArgumentException("Der Headerparameter 'TargetEncoding' darf nicht null sein.");
    }
   
    try
    {
        def boolConv = message.getHeaders().get("SetBomUTF8");
        DEFAULT_SET_BOM_UTF8 = Boolean.parseBoolean(boolConv);
        
  	    messageLog.addCustomHeaderProperty("SetBomUTF8", DEFAULT_SET_BOM_UTF8.toString());
    }
    catch(Exception e)
    {
       throw new IllegalArgumentException(e);
    } 
    
    bodyString = ReplaceByteOrderMark(bodyString, sourceEncoding);
    byte[]  fileBytes = bodyString.bytes;
    
    //Kann genutzt werden um sich die eingehenden Bytes anzuzeigen
    def hexDumpInput = new StringBuilder();
   
    for (byte b in fileBytes) 
    {
        hexDumpInput.append(String.format("%02X ", b))
    }
    messageLog.addCustomHeaderProperty("Inhalt Eingang", hexDumpInput.toString());
    
  
    String sourceString = new String(fileBytes, sourceEncoding);
    
    if(DEFAULT_SET_BOM_UTF8)
    {
       sourceString = SetByteOrderMark(sourceString, targetEncoding)
    }
    byte[] targetBytes = ConvertToTargetEncodingWithReplacement(sourceString, replacementChar, targetEncoding, messageLog);
    
    // Kann genutzt werden um sich die ausgehenden Bytes anzuzeigen
    def hexDumpOutput = new StringBuilder();
    for (byte b in targetBytes) 
    {
        hexDumpOutput.append(String.format("%02X ", b))
    }
    messageLog.addCustomHeaderProperty("Inhalt Ausgang", hexDumpOutput.toString());
    
    
    message.setBody(new String(targetBytes, targetEncoding));
   
	return message;
}


String ReplaceByteOrderMark(String message, String sourceEncoding)
{
    switch(sourceEncoding)
    {
        case "UTF-16BE":
        	message = message.replace("\u00FE", "")
            message = message.replace("\u00FF", "")
            break;
        
        case "UTF-16LE":
            message = message.replace("\u00FF", "")
    	    message = message.replace("\u00FE", "")
    	    break;
    	 
        case "UTF-32BE":
            message = message.replace("\u0000", "")
    	    message = message.replace("\u00FE", "")
    	    message = message.replace("\u00FF", "")
            break;
        case "UTF-32LE":
            message = message.replace("\u00FF", "")
    	    message = message.replace("\u00FE", "")
    	    message = message.replace("\u0000", "")
            break;
    }
        return message;
}

String SetByteOrderMark(String message, String targetEncoding)
{
    switch(targetEncoding)
    {
        case "UTF-8":
            message = "\u00EF\u00BB\u00BF${message}";
            break;
        case "UTF16-LE":
            message = "\u00FF\u00FE${message}";
            break;
        case "UTF-16BE":
            message = "\u00FE\u00FF${message}";
            break;
        case "UTF-32LE":
            message = "\u00FF\u00FE\u0000\u0000${message}";
            break;
        case "UTF-32BE":
            message = "\u0000\u0000\u00FE\u00FF${message}";
            break;
    }
    return message;
   
}

byte[] ConvertToTargetEncodingWithReplacement(String input, char replacementChar, String targetEncoding, MessageLog messageLog)
{
   /* Charset tgtCharset = Charset.forName(targetEncoding)
    CharsetEncoder encoder = tgtCharset.newEncoder()
    encoder.onUnmappableCharacter(CodingErrorAction.REPLACE)
    encoder.replaceWith("?".getBytes(tgtCharset))

    ByteBuffer byteBuffer = encoder.encode(CharBuffer.wrap(input))
    byte[] targetBytes = new byte[byteBuffer.remaining()]
    byteBuffer.get(targetBytes)
    
    return new String(targetBytes, tgtCharset)
    
    */

    
    def result = new StringBuilder();
    for (int i = 0; i < input.length(); i++) 
    {
       
        char currentCharacter = input[i];
       
        if(isValidCharInTarget(currentCharacter, targetEncoding, messageLog))
        {
            result.append(currentCharacter);
        }
        else
        {
            result.append(replacementChar);
        }
    }
   
    // Ergebnis in UTF-8 Byte-Array umwandeln
    return result.toString().getBytes(targetEncoding);
    
    
}

boolean isValidCharInTarget(char c, String targetEncoding, MessageLog messageLog) {
    try 
    {
        String.valueOf(c).getBytes(targetEncoding);
        //messageLog.addCustomHeaderProperty("Aktueller Buchstabe", c);
        return true;
    } 
    catch (Exception e) 
    {
        //messageLog.addCustomHeaderProperty("Nicht umwandelbar:", c);
        return false;
    }
}