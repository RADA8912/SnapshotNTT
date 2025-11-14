import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap

def Message processData(Message message) {
    
    def body = message.getBody(String)

   
    def decodedString = decodeUnicodeEscapes(body)

   
    message.setBody(decodedString)
    return message
}


def decodeUnicodeEscapes(String input) {
    return input.replaceAll(/\\u([\da-fA-F]{4})/) { match, hex ->
        new String(Character.toChars(Integer.parseInt(hex, 16)))
    }
}
