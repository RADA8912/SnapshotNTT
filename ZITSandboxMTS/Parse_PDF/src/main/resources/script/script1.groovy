/* Refer the link below to learn more about the use cases of script.
https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/148851bf8192412cba1f9d2c17f4bd25.html

If you want to know more about the SCRIPT APIs, refer the link below
https://help.sap.com/doc/a56f52e1a58e4e2bac7f7adbf45b2e26/Cloud/en-US/index.html */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;

def Message processData(Message message) {
   
    def pdfContent = message.getFirstPayload().getContent(); // laden des PDFs als bytearray
    PDDocument pdf = PDDocument.load(pdfContent); 
    PDDocumentNameDictionary names = pdf.getDocumentCatalog().getNames();
    PDEmbeddedFilesNameTreeNode embeddedFilesNodes = names.getEmbeddedFiles();
//	Map<String, PDComplexFileSpecification> embeddedFiles = embeddedFilesNodes.getNames();
	

   
   
   	def messageLog = messageLogFactory.getMessageLog(message);
    messageLog.addAttachmentAsString("Payload", bodyAsString, "text/xml");
   
    return message;
}
