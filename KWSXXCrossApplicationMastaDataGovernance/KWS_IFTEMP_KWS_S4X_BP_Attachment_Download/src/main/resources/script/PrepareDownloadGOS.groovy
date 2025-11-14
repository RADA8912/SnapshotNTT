import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    def DIRDocType = message.getProperty("DocumentInfoRecordDocType")
    def DIRDocNumber = message.getProperty("DocumentInfoRecordDocNumber")
    def DIRDocVersion = message.getProperty("DocumentInfoRecordDocVersion")
    def DIRDocPart = message.getProperty("DocumentInfoRecordDocPart")
    def LogicalDocument = message.getProperty("LogicalDocument")
    def ArchiveDocumentID = message.getProperty("ArchiveDocumentID")
    def LinkedSAPObjectKey = message.getProperty("LinkedSAPObjectKey")
    def BusinessObjectTypeName = message.getProperty("BusinessObjectTypeName")

    def downloadUri = String.format("http://vhkwss4xci.hec.kws.com:44300/sap/opu/odata/sap/API_CV_ATTACHMENT_SRV/AttachmentContentSet(DocumentInfoRecordDocType='%s',DocumentInfoRecordDocNumber='%s',DocumentInfoRecordDocVersion='%s',DocumentInfoRecordDocPart='%s',LogicalDocument='%s',ArchiveDocumentID='%s',LinkedSAPObjectKey='%s',BusinessObjectTypeName='%s')/\$value?sap-client=200",
                                            DIRDocType,
                                            DIRDocNumber,
                                            DIRDocVersion,
                                            DIRDocPart,
                                            LogicalDocument,
                                            ArchiveDocumentID,
                                            LinkedSAPObjectKey,
                                            BusinessObjectTypeName)

    message.setHeader("CamelHttpUri", downloadUri)

    return message
}
