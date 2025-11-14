import com.sap.gateway.ip.core.customdev.util.Message;
import java.nio.charset.Charset

//TextCodepageConversionBean
//Version 1.0.1

def Message processData(Message message) {
    Map<String, Object> props = message.getProperties();
    Map<String, Object> headers = message.getHeaders();
    String targetEncoding = props.get("Conversion.charset");
    String sourceContentType = headers.get("Content-Type");

    inputValidation(sourceContentType, targetEncoding);
    sourceContentType = sourceContentType.toUpperCase();
    targetEnconding = targetEncoding.toUpperCase();

    String sourceEncoding = parseContentEncoding(sourceContentType);
    if (!charsetIsValid(sourceEncoding)){
        throw new Exception("Charset value (source) from Content-Type header is not a valid charset.");
    }

    if (targetEncoding != sourceEncoding){
        String sourceText = message.getBody(byte[].class);
        // Convert the text from the source code page to the target code page
        Charset sourceCharset = Charset.forName(sourceEncoding)
        Charset targetCharset = Charset.forName(targetEncoding)

        byte[] byteArray = sourceText.getBytes(sourceCharset)
        String convertedText = new String(byteArray, targetCharset)

        String newContentType = sourceContentType.replace(sourceEncoding, targetEncoding);
        message.setHeader("Content-Type", newContentType);
        message.setBody(convertedText);
    }
    return message;
}

private static String parseContentEncoding(String contentType){
    //TEXT/PLAIN; charset=UTF-8; name=input.txt
    String encoding = "";
    List<String> oct_array = contentType.split(';');
    for (int i = 0; i < oct_array.size(); i++){
        String item = oct_array[i];
        if (item.contains("CHARSET")){
            encoding = item.split('=')[1];
            break;
        }
    }
    return encoding;
}

private static void inputValidation(String sourceContentType, String targetEncoding){
    if (sourceContentType == null || sourceContentType == ''){
        throw new Exception("Content-Type header must be available. Only TEXT type is supported.");
    }
    sourceContentType = sourceContentType.toUpperCase();
    if (!sourceContentType.startsWith("TEXT")){
        throw new Exception("Content-Type not supported. Only TEXT type is supported.");
    }
    if (!sourceContentType.contains("CHARSET")){
        throw new Exception("CHARSET must be keyword must exist in Content-Type header.");
    }
    if (targetEncoding == null || targetEncoding == ''){
        throw new Exception("Conversion.charset property must exist.");
    }
    if (!charsetIsValid(targetEncoding)){
        throw new Exception("Conversion.charset (target) value is not a valid charset.");
    }
}

private static boolean charsetIsValid(String charset){
    String[] charsetList = ["US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16"];
    return charsetList.contains(charset);
}
