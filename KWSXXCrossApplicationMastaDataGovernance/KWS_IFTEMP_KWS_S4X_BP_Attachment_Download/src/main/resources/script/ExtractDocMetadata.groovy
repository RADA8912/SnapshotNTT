import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def Message processData(Message message) {

    def reader = message.getBody(Reader)
    def Metadata = new JsonSlurper().parse(reader)

    def builder = new StringBuilder();

    def output = new JsonBuilder()

    output.DocMetadata {
        'Originals' Metadata.d.results.collect { entry ->
            [
                    'BusinessObjectTypeName' : entry.BusinessObjectTypeName,
                    'ContentType' : entry.MimeType,
                    'LinkedSAPObjectKey' : entry.LinkedSAPObjectKey,
                    'Slug' : entry.FileName,
                    'DocumentURL' : entry.DocumentURL,
                    'DocumentInfoRecordDocType' : entry.DocumentInfoRecordDocType,
                    'DocumentInfoRecordDocNumber' : entry.DocumentInfoRecordDocNumber,
                    'DocumentInfoRecordDocVersion' : entry.DocumentInfoRecordDocVersion,
                    'DocumentInfoRecordDocPart' : entry.DocumentInfoRecordDocPart,
                    'LogicalDocument' : entry.LogicalDocument,
                    'ArchiveDocumentID' : entry.ArchiveDocumentID,
                    'blobPayload' : null
            ]
        }
    }

    /*for (LazyMap singleDoc in Metadata.d.results) {
        builder.append(singleDoc.LogicalDocument)
        builder.append("\n")
    }*/

    message.setBody(output.toPrettyString())

    return message
}