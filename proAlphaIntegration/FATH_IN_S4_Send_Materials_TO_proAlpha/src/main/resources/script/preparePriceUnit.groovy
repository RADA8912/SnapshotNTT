import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.mapping.ValueMappingApi
import com.sap.it.api.ITApiFactory
import groovy.json.*



Message processData(Message message) {
    // Parse payload
    def body = message.getBody(String)
    def jsonSlurper = new JsonSlurper()
    def originalPayload = jsonSlurper.parseText(body)
    def messageLog = messageLogFactory.getMessageLog(message)


    // Read and clean material number
    def rawMaterialNumber = message.getProperty("Z_MaterialNumber") as String
    def materialNumber = rawMaterialNumber?.replaceFirst('^0+(?!$)', '') ?: "UNKNOWN"

    // Extract classification
    def classificationArray = originalPayload.classification
    def preiseinheitEntry = classificationArray.find { it.classification_type == 'Preiseinheit' }
    classificationArray.remove(preiseinheitEntry)

    // Store rest as Z_Class_Payload
    def updatedPayload = [classification: classificationArray]
    def updatedPayloadJson = JsonOutput.toJson(updatedPayload)
    message.setProperty("Z_Class_Payload", updatedPayloadJson)

    // Get price unit value
    def sourceValue = preiseinheitEntry?.specification ?: ""
    def mappedValue = sourceValue

    try {
        def vmApi = ITApiFactory.getApi(ValueMappingApi.class, null)
        if (vmApi && sourceValue) {
            mappedValue = vmApi.getMappedValue("SAP", "PREIS", sourceValue, "PROALPHA", "PREIS")
        }
    } catch (Exception e) {
        mappedValue = sourceValue // fallback
    }

    // Construct transformed Preiseinheit payload
    def transformed = [
        part: [[
            id: materialNumber,
            part_no: materialNumber,
            price_unit: mappedValue,
        ]]
    ]
    def transformedJson = JsonOutput.toJson(transformed)

    // Set output body
    message.setBody(transformedJson)

    //Add transformed payload as attachment
     if (messageLog != null) {
            //Add Payload as Attachment, but make it dynamic as this script is used multiple times
            messageLog.addAttachmentAsString('Payload Preiseinheit', transformedJson, 'text/plain')
        }

    return message
}
