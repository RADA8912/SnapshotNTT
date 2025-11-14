import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    // Retrieve the payload as a string
    def body = message.getBody(String)

    // Parse the XML payload
    def xml = new XmlSlurper().parseText(body)

    // Get the list of all A_DocInfoRecdObjLinkProductType elements
    def allEntries = xml.'**'.findAll { it.name() == 'A_DocInfoRecdObjLinkProductType' }

    // Take the first entry if available
    def firstEntry = allEntries ? allEntries[0] : null

    // Check if at least one entry exists
    def hasEntries = firstEntry != null
    message.setProperty("Z_RecordFound", hasEntries.toString())

    // Check if A_DocInfoRecdObjLinkProduct tag is empty or missing
    def productTag = xml.'**'.find { it.name() == 'A_DocInfoRecdObjLinkProduct' }
    def isProductTagEmpty = (productTag == null || productTag.text().trim() == "")
    message.setProperty("Z_ProductTagEmpty", isProductTagEmpty.toString())

    // Log document class payload
    def messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog != null) {
        messageLog.addAttachmentAsString('Payload DocumentNumber', body, 'text/plain')
    }

    // Return the message
    return message
}
