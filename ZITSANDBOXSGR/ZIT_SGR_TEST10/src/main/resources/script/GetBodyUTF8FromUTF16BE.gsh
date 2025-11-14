import com.sap.gateway.ip.core.customdev.util.Message
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.CharacterCodingException
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder
import java.nio.charset.CodingErrorAction
import java.nio.charset.StandardCharsets

def Message processData(Message message) {
	// Get body
	byte[] bStrUtf16le = message.getBody(byte[].class)
	String strUtf8 = getUTF8StringFrom16LE(bStrUtf16le)

	// Set body
	message.setBody(strUtf8)
	return message
}
	
private def String getUTF8StringFrom16LE(byte[] bytes) {
	ByteBuffer buffer = ByteBuffer.wrap(bytes)
	CharsetDecoder decoder = StandardCharsets.UTF_16BE.newDecoder()
	decoder.onMalformedInput(CodingErrorAction.REPORT)
	decoder.onUnmappableCharacter(CodingErrorAction.REPORT)
	
	CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder()
	encoder.onMalformedInput(CodingErrorAction.REPORT)
	encoder.onUnmappableCharacter(CodingErrorAction.REPORT)

	try {
		char[] b16 = decoder.decode(buffer).getChars()
		CharBuffer buffer8 = CharBuffer.wrap(b16)
		byte[] s8 = encoder.encode(buffer8).array()

// Original erzeugt Array mit der Nummer des Zeichens
//		return s8.toString() // Original

        // Neu hinzugefügt erzeugt String
        String newContent = new String(s8, "utf-8") as String
		return newContent.toString()

	} catch (CharacterCodingException e) {
		return new String("Decoder Error: " + e)
	} catch (Exception e1) {
		return new String("Error: " + e1)
	}
}

/**	Beispiel mit Replace
        CharsetDecoder decoder2 = StandardCharsets.UTF_8.newDecoder()
        decoder2.onMalformedInput(CodingErrorAction.REPLACE)
          .onUnmappableCharacter(CodingErrorAction.REPLACE)
          .replaceWith("?")
*/