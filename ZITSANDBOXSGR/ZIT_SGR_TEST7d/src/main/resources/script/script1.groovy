import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.commons.compress.compressors.CompressorInputStream
import org.apache.commons.compress.compressors.CompressorStreamFactory
import java.io.ByteArrayOutputStream

def Message processData(Message message) {

//	def in = message.getBody(java.io.InputStream)
    def bodyIn = message.getBody(java.io.InputStream)

    BufferedInputStream inputBuffer = new BufferedInputStream(bodyIn)
    OutputStream out = new ByteArrayOutputStream()
    CompressorInputStream decompressor = new CompressorStreamFactory()
        .createCompressorInputStream(inputBuffer) {
        IOUtils.copy(decompressor, out)
        }

	message.setBody(new String(out.toByteArray()))

	return message
}
