import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

def Message processData(Message message) {

    def metadataPayload = message.getHeader("metadataPayload", String)
    def reader = message.getBody(Reader)

    def builder = new StringBuilder()

    for (String line in reader.readLines()) {
        builder.append(line)
    }

    def Metadata = new JsonSlurper().parseText(metadataPayload)

    def output = new JsonBuilder()

    output.DocMetadata {
        'Original'
            {
                 'BusinessObjectTypeName'  Metadata.DocMetadata.Originals.BusinessObjectTypeName
                 'ContentType'  Metadata.DocMetadata.Originals.ContentType
                 'LinkedSAPObjectKey'  Metadata.DocMetadata.Originals.LinkedSAPObjectKey
                 'Slug' Metadata.DocMetadata.Originals.Slug
                 'DocumentInfoRecordDocType'  Metadata.DocMetadata.Originals.DocumentInfoRecordDocType
                 'DocumentInfoRecordDocNumber'  Metadata.DocMetadata.Originals.DocumentInfoRecordDocNumber
                 'DocumentInfoRecordDocVersion'  Metadata.DocMetadata.Originals.DocumentInfoRecordDocVersion
                 'DocumentInfoRecordDocPart'  Metadata.DocMetadata.Originals.DocumentInfoRecordDocPart
                 'LogicalDocument'  Metadata.DocMetadata.Originals.LogicalDocument
                 'ArchiveDocumentID'  Metadata.DocMetadata.Originals.ArchiveDocumentID
                 'blobPayload'  builder.toString()
            }
        }

    message.setBody(output.toPrettyString())

    return message
}
