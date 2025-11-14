import java.io.IOException;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

@Step(name = "Extract Attachment from PDF")
public class XmlFromPdfExtractor {

	@StepProcess //das hier wird ausgeführt und entspricht auf SAP PI den process-Block
	public void extractZugferdXml(CustomStepData message) throws Exception { //message wird durch Cloud Integration object ausgetauscht

		final PDDocument pdf = loadPdfDocument(message); //message kann mehrere Payloads enthalten daher muss der richtige geladen werden - Methode ab Zeile 67
		final byte[] zugferdXml = readZugferdXml(pdf);

		message.addPayload(0, "text/xml", zugferdXml); //Das hier wird nicht gebraucht, hier sollte für die Cloud Integration das XML in lesbarer Form in den output gelegt werden
	}

	private byte[] readZugferdXml(PDDocument pdf) throws Exception { //diese Methode kann 1:1 übernommen werden nur die Exception muss anders sein

		final PDDocumentNameDictionary names = pdf.getDocumentCatalog().getNames();
		nullcheck("The PDF document catalog is empty", names);
		nullcheck("The PDF contains no embedded files", names.getEmbeddedFiles());

		try {
			final PDEmbeddedFilesNameTreeNode embeddedFilesNodes = names.getEmbeddedFiles();

			final Map<String, PDComplexFileSpecification> embeddedFiles = embeddedFilesNodes.getNames();
			nullcheck("The PDF file contains no attachments", names.getEmbeddedFiles());

			PDComplexFileSpecification zugferdXmlSpecification = embeddedFiles.get("ZUGFeRD-invoice.xml");
			if (zugferdXmlSpecification == null) {
				zugferdXmlSpecification = embeddedFiles.get("zugferd-invoice.xml");
				if (zugferdXmlSpecification == null) {
					zugferdXmlSpecification = embeddedFiles.get("factur-x.xml");
				}
			}
			nullcheck("The PDF file contains no valid zugferd xml", zugferdXmlSpecification);

			final PDEmbeddedFile embeddedZugferdFile = zugferdXmlSpecification.getEmbeddedFile();
			nullcheck("The PDF file contains a valid zugferd xml specification but no embedded file", embeddedZugferdFile);

			return embeddedZugferdFile.getCOSObject().createInputStream().readAllBytes();
		} catch (IOException e) {
			throw new Exception("Cannot extract attachment names from PDF", e);
		}
	}

	private static void nullcheck(String errormessage, Object... objects) throws ZugferdException { //diese Methode kann 1:1 übernommen werden nur die Exception muss anders sein
		for (Object object : objects) {
			if (object == null) {
				throw new Exception(errormessage);
			}
		}
	}

	private static PDDocument loadPdfDocument(CustomStepData message) throws ZugferdException { //Exception muss geändert werden
		try {
			final byte[] pdfContent = message.getFirstPayload().getContent(); // laden des PDFs als bytearray
			return PDDocument.load(pdfContent); //generischer Aufruf der pdfbox library
		} catch (IOException e) {
			throw new Exception("Cannot parse PDF document", e);
		}
	}
}
