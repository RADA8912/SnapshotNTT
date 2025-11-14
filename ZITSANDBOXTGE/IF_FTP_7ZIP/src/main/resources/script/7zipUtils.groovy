import com.sap.gateway.ip.core.customdev.util.Message
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import net.sf.sevenzipjbinding.IInStream;
import net.sf.sevenzipjbinding.ArchiveFormat
import net.sf.sevenzipjbinding.ExtractOperationResult
import net.sf.sevenzipjbinding.IInArchive
import java.nio.file.Files
import java.nio.file.Paths
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.BufferedReader
import java.io.BufferedWriter

def Message processData(Message message) 
{  
    def messageLog = messageLogFactory.getMessageLog(message);
    String archiveFilename = message.getHeaders().get("pathZipFile");
    
    messageLog.addCustomHeaderProperty("Dateiname:", archiveFilename);
    
    byte[] fileContent = message.getBody(byte[].class);
    String tempFilePath = "/tmp/tempfile.txt";
    
    
    
    ByteArrayInStream  inStream = new ByteArrayInStream(fileContent);
    IInArchive archive = SevenZip.openInArchive(ArchiveFormat.SEVEN_ZIP, inStream);
    ISimpleInArchive simpleInArchive = archive.getSimpleInterface();
    
    simpleInArchive.archiveItems.each 
    { 
        ISimpleInArchiveItem item ->
        if (!item.isFolder())
        {
            def byteStream = new ByteArrayOutputStream()
            ExtractOperationResult result = item.extractSlow(
    		{ 
    			byte[] data ->
                byteStream.write(data)
                data.length
            })
            if (result == ExtractOperationResult.OK)
            {
                // Den extrahierten Inhalt als String (bei Textdateien) speichern
               String content = new String(byteStream.toByteArray(), "UTF-8");
               
                extractedFiles[item.getPath()] = content;
    
            }
            else
            {
                throw new RuntimeException("Fehler beim Extrahieren des Elements: " + item.path);
            }
        }
    }
    message.setProperty("extractedFiles", extractedFiles);
   
    return message;
}


class ByteArrayInStream implements IInStream {
    private ByteArrayInputStream byteArrayInputStream

    ByteArrayInStream(byte[] byteArray) {
        this.byteArrayInputStream = new ByteArrayInputStream(byteArray)
    }

    long seek(long offset, int seekOrigin) {
        if (seekOrigin == SEEK_SET) {
            byteArrayInputStream.reset()
            byteArrayInputStream.skip(offset)
        } else if (seekOrigin == SEEK_CUR) {
            byteArrayInputStream.skip(offset)
        } else if (seekOrigin == SEEK_END) {
            byteArrayInputStream.reset()
            byteArrayInputStream.skip(byteArrayInputStream.available() - offset)
        }
        return byteArrayInputStream.available()
    }

    int read(byte[] data) {
        return byteArrayInputStream.read(data)
    }

    void close() {
        byteArrayInputStream.close()
    }
}

