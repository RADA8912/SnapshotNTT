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
* ConvertCharset
* This Groovy script convert the message (body) from one enconding (e.g. UTF16-BE to UTF-8) to another
* 
*
* Groovy script parameters (Header Parameters)
* - ConvertCharset.ReplacementChar = characters which is used to replace unknown characters
* - ConvertCharset.SetBomTargetFile = True, sets the ByteOrderMark
* - ConvertCharset.SourceEncoding = Encoding (Charset) of incoming message
* - ConvertCharset.TargetEncoding = Encoding (Charset) of outgoing message
* @author nttdata-solutions.com
* @version 1.0.0
*/

def Message processData(Message message) 
{
 	def bodyString = message.getBody(String);
    MessageLog messageLog = messageLogFactory.getMessageLog(message);
    
    final String DEFAULT_SET_BOM_UTF8 = false;
    final char DEFAULT_REPLACEMENTCHAR= '?';
    String sourceEncoding;
    String targetEncoding;
    Boolean setBomUtf8;
    
    replacementChar = getExchangeProperty(message, "ConvertCharset.ReplacementChar", false)
    
    if (replacementChar == null || replacementChar.length() == 0) 
    {
                       replacementChar = DEFAULT_REPLACEMENTCHAR;
    }
    messageLog.addCustomHeaderProperty("ConvertCharset.ReplacementChar", replacementChar);

    sourceEncoding = getExchangeProperty(message, "ConvertCharset.SourceEncoding", true);
    messageLog.addCustomHeaderProperty("ConvertCharset.SourceEncoding", sourceEncoding);

	targetEncoding = getExchangeProperty(message, "ConvertCharset.TargetEncoding", true);
    messageLog.addCustomHeaderProperty("ConvertCharset.TargetEncoding", targetEncoding);
   
   
    try
    {
        def boolConv = getExchangeProperty(message, "ConvertCharset.SetBomTargetFile", false);
       
        
        if (boolConv == null || boolConv.length() == 0) 
        {
            setBomUtf8 = DEFAULT_SET_BOM_UTF8;
        }
        setBomUtf8 = Boolean.parseBoolean(boolConv);
  	    messageLog.addCustomHeaderProperty("ConvertCharset.SetBomTargetFile", setBomUtf8.toString());
    }
    catch(Exception e)
    {
         messageLog.addCustomHeaderProperty("Exception", e.toString());
    } 
    
    bodyString = ReplaceByteOrderMark(bodyString, sourceEncoding);
    byte[]  fileBytes = bodyString.bytes;
    
    /*
    // Can be used to display the incoming bytes
    def hexDumpInput = new StringBuilder();
   
    for (byte b in fileBytes) 
    {
        hexDumpInput.append(String.format("%02X ", b))
    }
    messageLog.addCustomHeaderProperty("Incoming", hexDumpInput.toString());
    */
    
    String sourceString = new String(fileBytes, sourceEncoding);

    if(setBomUtf8)
    {
       sourceString = SetByteOrderMark(sourceString, targetEncoding)
    }
    
    for (int i = 0; i < sourceString.length(); i++) 
    {
        char currentCharacter = sourceString[i];
        int intValue = (int) currentCharacter;
        messageLog.addCustomHeaderProperty("Integer", intValue.toString());
    }
    
    byte[] targetBytes = ConvertToTargetEncodingWithReplacement(sourceString, replacementChar.charAt(0), targetEncoding);
    String testString = new String(targetBytes, targetEncoding)
    

    /*
    // Can be used to display the outgoing bytes
    def hexDumpOutput = new StringBuilder();
    for (byte b in targetBytes) 
    {
        hexDumpOutput.append(String.format("%02X ", b))
    }
    messageLog.addCustomHeaderProperty("Outgoing", hexDumpOutput.toString());
    */
    
    message.setBody(new String(targetBytes, targetEncoding));
   
	return message;
}

/*
* Removes the BOM identifier
* @param message This is message.
* @param sourceEncoding This is the sourceEncoding of the message.
* @return message Returns the message.
*/
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

/*
* Set the BOM identifier for the targetEncoding
* @param message This is message.
* @param targetEncoding This is the targetEncoding of the message.
* @return message Returns the message value.
*/
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

/**
* getExchangeProperty
* @param message This is message.
* @param propertyName This is name of property.
* @param mandatory This is parameter if property is mandatory.
* @return propertyValue Return property value.
*/
private def getExchangeProperty(Message message, String propertyName, boolean mandatory) {
                String propertyValue = message.properties.get(propertyName) as String
                if (mandatory) {
                               if (propertyValue == null || propertyValue.length() == 0) {
                                               throw Exception("Mandatory exchange property '$propertyName' is missing.")
                               }
                }
                return propertyValue
}

/*
* Converts the message to the corresponding target character set and replaces 
* unknown characters with the replacement character.
* @param input The source message for the conversion.
* @param replacementChar The char that replaces unknown characters in the conversion.
* @param targetEncoding The target encoding fpr the message.
* @return result Returns the converted result as string.
*/
byte[] ConvertToTargetEncodingWithReplacement(String input, char replacementChar, String targetEncoding)
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
        if(targetEncoding == "ISO-8859-1")
        {
            
            int intValue = (int) currentCharacter;
            if(intValue <= 127)
            {
                 result.append(currentCharacter);
            }
            else
            {
                  result.append(replacementChar);
            }
            continue;
        }
        if(isValidCharInTarget(currentCharacter, targetEncoding))
        {
            result.append(currentCharacter);
        }
        else
        {
            result.append(replacementChar);
        }
    }
    return result.toString().getBytes(targetEncoding);
}

/*
* Checks whether all characters in the target character set are valid. 
* If this is not the case, the replacement character is returned 
* instead of the current character.
* @param c This is the current character.
* @param targetEncoding This is the target charset for the character.
* @return  Returns true if the current char is valid in the target charset. 
*           False, if the current char is not valid in the target charset.
*/
boolean isValidCharInTarget(char c, String targetEncoding) {
    try 
    {
        String.valueOf(c).getBytes(targetEncoding);
        return true;
    } 
    catch (Exception e) 
    {
        return false;
    }
}