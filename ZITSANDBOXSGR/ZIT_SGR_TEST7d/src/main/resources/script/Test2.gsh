import com.sap.gateway.ip.core.customdev.util.Message;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.Coders;
import java.io.File;
import org.apache.commons.io.FileUtils;

def Message processData(Message message) {

	def body = message.getBody(InputStream.class);
	File file = new File("test")
	FileUtils.copyInputStreamToFile(body, file);
	SevenZFile sevenZFile = new SevenZFile(file);
	SevenZArchiveEntry entry;
//	while ((entry = sevenZFile.getNextEntry()) != null){
//		if (entry.getName().endsWith(".json")) {
			entry = sevenZFile.getNextEntry()

			ByteArrayOutputStream out=new ByteArrayOutputStream();
			byte[] content = new byte[(int) entry.getSize()];
			sevenZFile.read(content, 0, content.length);
			out.write(content);
			out.close();
			message.setBody(new String(out.toByteArray()));
//			break;
//		}
//	}
	return message;
}
