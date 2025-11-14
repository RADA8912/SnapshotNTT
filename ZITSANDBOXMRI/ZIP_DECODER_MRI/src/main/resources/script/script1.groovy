/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


def Message processData(Message message) {
    def password = "myPassword"
    def constantString = "This is a constant string."

    def outputStream = new ByteArrayOutputStream()
    def zipOutputStream = new ZipArchiveOutputStream(outputStream)
    zipOutputStream.setFallbackToUTF8(true)
    zipOutputStream.setEncoding("Cp437") // Required for non-ASCII characters

    byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8)
    zipOutputStream.setPassword(passwordBytes)

    def entry = new ZipArchiveEntry("constant.txt")
    entry.setSize(constantString.length())
    zipOutputStream.putArchiveEntry(entry)

    zipOutputStream.write(constantString.getBytes(StandardCharsets.UTF_8))

    zipOutputStream.closeArchiveEntry()
    zipOutputStream.finish()

    def zipBytes = outputStream.toByteArray()

    // Do something with the zipBytes array
    // ...
    
    message.setBody(zipBytes)
    return message
    
}