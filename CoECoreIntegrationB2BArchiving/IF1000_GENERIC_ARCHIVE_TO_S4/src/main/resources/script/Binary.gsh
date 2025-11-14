import com.sap.gateway.ip.core.customdev.util.Message; 
import java.util.HashMap; 
import com.sap.gateway.ip.core.customdev.util.Message;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
//End of importing Builtin Library
def Message processData(Message message) 
{ 
    //Convert Binart Format to String Format
    def binarydata = message.getBody(String)             // Read the message body into a variable
    byte[] decoded = binarydata.decodeBase64()           // Decode the Binary Data
    def text = new String(decoded)                       // Store the decoded data into String format
    
    //Store String into a PDF File
    OutputStream out = new ByteArrayOutputStream()
    PdfWriter writer = new PdfWriter(out)
    PdfDocument pdfDoc = new PdfDocument(writer)
    Document document = new Document(pdfDoc)
    document.add(new Paragraph(text))
    document.close()
    message.setHeader('Content-Type', 'application/pdf')
    message.setBody(out.toByteArray())
    return message 
    
}