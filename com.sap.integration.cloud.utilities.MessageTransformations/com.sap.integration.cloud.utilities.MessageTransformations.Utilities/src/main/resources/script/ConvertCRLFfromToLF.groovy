import com.sap.gateway.ip.core.customdev.util.Message;

//ConvertCRLFfromToLF
//Version 1.0.0

def Message processData(Message message) {
    Map<String, Object> props = message.getProperties();
    String targetEnding = props.get("Conversion.mode");

    String body = message.getBody(String);
    String result;

    if (targetEnding == 'CRLF') {
        result = body.replaceAll('\r?\n', '\r\n');
    } else if (targetEnding == 'LF') {
        result = body.replaceAll('\r\n', '\n');
    } else {
        throw new IllegalArgumentException("Invalid target ending: $targetEnding")
    }

    message.setBody(result);
    return message;
}
